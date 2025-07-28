package com.mahdi.rostamipour.chatgram.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class StateTyping (
    val type : String,
    val senderId : Int,
    val getterId : Int,
)