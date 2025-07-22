package com.mahdi.rostamipour.chatgram.domain.usecase

import com.mahdi.rostamipour.chatgram.domain.models.User
import com.mahdi.rostamipour.chatgram.domain.repository.UserRepository

class UserUseCase(private val userRepository: UserRepository) {
    suspend fun registerUser(name : String , bio : String) : String{
        return userRepository.registerUser(name , bio)
    }

    suspend fun fetchUsersList(userId : Int) : List<User>{
        return userRepository.fetchUsersList(userId)
    }
}