package com.mahdi.rostamipour.chatgram.domain.usecase

import com.mahdi.rostamipour.chatgram.domain.models.GetMessage
import com.mahdi.rostamipour.chatgram.domain.repository.MessageRepository

class MessageUseCase(val messageRepository: MessageRepository) {

    suspend fun getMessages(senderId : Int , getterId : Int) : List<GetMessage>{
        return messageRepository.getMessages(senderId,getterId)
    }

}