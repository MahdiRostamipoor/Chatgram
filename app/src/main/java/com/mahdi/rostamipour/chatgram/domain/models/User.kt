package com.mahdi.rostamipour.chatgram.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val bio: String?,
    val date: String?,
    val id: Int,
    val isOnline: Boolean,
    val message: String?,
    val name: String,
    val seen: Int?,
    val typeMessage: String?
)