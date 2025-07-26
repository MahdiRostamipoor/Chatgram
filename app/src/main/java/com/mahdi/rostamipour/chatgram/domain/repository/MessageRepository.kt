package com.mahdi.rostamipour.chatgram.domain.repository

import com.mahdi.rostamipour.chatgram.domain.models.GetMessage
import com.mahdi.rostamipour.chatgram.domain.models.SendMessage

interface MessageRepository {
    suspend fun getMessages(senderId : Int , getterId : Int) : List<GetMessage>
    suspend fun sendMessage(sendMessage: SendMessage) : GetMessage
}