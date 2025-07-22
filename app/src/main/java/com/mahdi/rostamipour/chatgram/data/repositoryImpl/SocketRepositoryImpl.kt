package com.mahdi.rostamipour.chatgram.data.repositoryImpl

import com.mahdi.rostamipour.chatgram.data.service.ApiService
import com.mahdi.rostamipour.chatgram.domain.models.GetMessage
import com.mahdi.rostamipour.chatgram.domain.models.User
import com.mahdi.rostamipour.chatgram.domain.repository.SocketRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class SocketRepositoryImpl(private val apiService: ApiService) : SocketRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            apiService.incomingMessages.filterNotNull().collect {
                println("event socketget -> 50 $it")
            }
        }
    }

    override suspend fun connect(userId : Int) {
        apiService.connectSocket(userId)
    }

    override suspend fun disconnect(userId : Int) {
        apiService.disconnectSocket()
    }

    override fun observeMessages(): Flow<GetMessage> = apiService.incomingMessages

    override fun observeUsers(): Flow<List<User>> = apiService.incomingUsers


    override suspend fun sendMessage(message: String) {

    }



}