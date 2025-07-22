package com.mahdi.rostamipour.chatgram.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class IdentifyMessage(
    val userId: Int
)
