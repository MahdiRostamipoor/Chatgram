package com.mahdi.rostamipour.chatgram.domain.repository

import com.mahdi.rostamipour.chatgram.domain.models.GetMessage

interface MessageRepository {
    suspend fun getMessages(senderId : Int , getterId : Int) : List<GetMessage>
    //fun sendMessage(senderId : Int , getterId : Int) : List<GetMessage>
}