package com.mahdi.rostamipour.chatgram.data.repositoryImpl

import com.mahdi.rostamipour.chatgram.data.service.ApiService
import com.mahdi.rostamipour.chatgram.domain.models.GetMessage
import com.mahdi.rostamipour.chatgram.domain.models.SendMessage
import com.mahdi.rostamipour.chatgram.domain.repository.MessageRepository

class MessagesRepositoryImpl(val apiService: ApiService) : MessageRepository {

    override suspend fun getMessages(senderId: Int, getterId: Int): List<GetMessage> {
        return apiService.getMessages(senderId,getterId)
    }

    override suspend fun sendMessage(sendMessage: SendMessage): GetMessage {
        return apiService.sendMessage(sendMessage)
    }

}