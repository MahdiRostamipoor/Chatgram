package com.mahdi.rostamipour.chatgram.domain.repository

import com.mahdi.rostamipour.chatgram.domain.models.User

interface UserRepository {
    suspend fun registerUser(name : String , bio : String) : String
    suspend fun fetchUsersList(userId : Int) : List<User>
}