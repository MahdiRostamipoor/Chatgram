package com.mahdi.rostamipour.chatgram.domain.repository

import com.mahdi.rostamipour.chatgram.domain.models.GetMessage
import com.mahdi.rostamipour.chatgram.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface SocketRepository {
    suspend fun connect(userId : Int)
    suspend fun disconnect(userId : Int)
    fun observeMessages(): Flow<GetMessage>
    fun observeUsers(): Flow<List<User>>
    suspend fun sendMessage(message: String)
}