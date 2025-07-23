package com.mahdi.rostamipour.chatgram.data.service

import android.util.Log
import com.mahdi.rostamipour.chatgram.domain.models.GetMessage
import com.mahdi.rostamipour.chatgram.domain.models.IdentifyMessage
import com.mahdi.rostamipour.chatgram.domain.models.SocketEvent
import com.mahdi.rostamipour.chatgram.domain.models.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

class ApiService {

    private val httpClient = HttpClient{
        install(ContentNegotiation){
            json(Json{ignoreUnknownKeys = true})
        }
        install(WebSockets)
    }

    suspend fun registerUser(name : String , bio : String) : String{
        return httpClient.post("http://10.166.182.222:3000/register"){
            contentType(ContentType.Application.Json)
            setBody(mapOf("name" to name , "bio" to bio , "picProfile" to ""))
        }.body()
    }


    suspend fun getUsers(userId : Int) : List<User>{
        return httpClient.get("http://10.166.182.222:3000/users"){
            url {
                parameters.append("userId" , userId.toString())
            }
        }.body()
    }


    suspend fun getMessages(senderId : Int , getterId : Int) : List<GetMessage>{
        return httpClient.get("http://10.166.182.222:3000/getmessages"){
            url{
                parameters.append("senderId",senderId.toString())
                parameters.append("getterId",getterId.toString())
            }
        }.body()
    }



    private var socketSession: WebSocketSession? = null
    val incomingMessages = MutableSharedFlow<GetMessage>(replay = 0, extraBufferCapacity = 64, onBufferOverflow = BufferOverflow.DROP_LATEST)
    val incomingUsers = MutableSharedFlow<List<User>>(replay = 0, extraBufferCapacity = 64, onBufferOverflow = BufferOverflow.DROP_LATEST)

    suspend fun connectSocket(userId : Int) {
        httpClient.webSocket("ws://10.166.182.222:3000") {
            socketSession = this

            val identifyJson = Json.encodeToString(IdentifyMessage(userId = userId))

            send(Frame.Text(identifyJson))


            val json = Json {
                ignoreUnknownKeys = true
                serializersModule = SerializersModule {
                    polymorphic(SocketEvent::class) {
                        subclass(SocketEvent.UserListUpdate::class, SocketEvent.UserListUpdate.serializer())
                        subclass(SocketEvent.MessageReceived::class, SocketEvent.MessageReceived.serializer())
                    }
                }
                classDiscriminator = "type"
            }

            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    Log.d("WebSocket", "Received: $text")
                    try {
                        val event = json.decodeFromString<SocketEvent>(text)
                        when(event){
                            is SocketEvent.MessageReceived -> {
                                incomingMessages.tryEmit(event.data)
                            }

                            is SocketEvent.UserListUpdate -> {
                                incomingUsers.tryEmit(event.data)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("WebSocket", "Json decode error", e)
                    }
                }
            }
        }
    }

    suspend fun disconnectSocket(){
        socketSession?.close()
    }

}