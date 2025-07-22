package com.mahdi.rostamipour.chatgram.data.repositoryImpl

import com.mahdi.rostamipour.chatgram.data.service.ApiService
import com.mahdi.rostamipour.chatgram.domain.models.User
import com.mahdi.rostamipour.chatgram.domain.repository.UserRepository

class UserRepositoryImpl(private val apiService: ApiService) : UserRepository{

    override suspend fun registerUser(name: String, bio: String): String {
        return apiService.registerUser(name,bio)
    }

    override suspend fun fetchUsersList(userId : Int): List<User> {
        return apiService.getUsers(userId)
    }


}