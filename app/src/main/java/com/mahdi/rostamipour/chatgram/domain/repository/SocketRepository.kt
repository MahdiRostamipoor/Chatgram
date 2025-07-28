package com.mahdi.rostamipour.chatgram.domain.repository

import com.mahdi.rostamipour.chatgram.domain.models.GetMessage
import com.mahdi.rostamipour.chatgram.domain.models.Typing
import com.mahdi.rostamipour.chatgram.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface SocketRepository {
    suspend fun connect(userId : Int)
    suspend fun disconnect(userId : Int)
    fun observeMessages(): Flow<GetMessage>
    fun observeUsers(): Flow<List<User>>
    fun observeTyping(): Flow<Typing>
    suspend fun sendMessage(message: String)
    suspend fun isTyping(senderId: Int , getterId: Int)
    suspend fun stopTyping(senderId: Int , getterId: Int)
}