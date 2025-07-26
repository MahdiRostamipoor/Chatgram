package com.mahdi.rostamipour.chatgram.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
data class GetMessage(
    val id : Int,
    val bio: String,
    val date: String,
    val getterId: Int,
    val message: String,
    val name: String,
    val senderId: Int,
    val typeMessage: String,
    val seen : Int
)

@Serializable
@JsonClassDiscriminator("type")
sealed class SocketEvent {
    @Serializable
    @SerialName("userListUpdate")
    data class UserListUpdate(val data: List<User>) : SocketEvent()

    @Serializable
    @SerialName("newMessage")
    data class MessageReceived(val data: GetMessage) : SocketEvent()
}