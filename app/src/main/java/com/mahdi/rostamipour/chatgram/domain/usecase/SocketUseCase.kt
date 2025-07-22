package com.mahdi.rostamipour.chatgram.domain.usecase

import com.mahdi.rostamipour.chatgram.domain.models.GetMessage
import com.mahdi.rostamipour.chatgram.domain.models.User
import com.mahdi.rostamipour.chatgram.domain.repository.SocketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class SocketUseCase(private val socketRepository: SocketRepository) {

    suspend fun connectSocket(userId : Int){
        socketRepository.connect(userId)
    }


    fun getMessages() : Flow<GetMessage>{
        return socketRepository.observeMessages()
    }

    fun getUsersList() : Flow<List<User>>{
        return socketRepository.observeUsers()
    }

}