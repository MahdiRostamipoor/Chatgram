package com.mahdi.rostamipour.chatgram.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class SendMessage (
    val senderId : Int ,
    val getterId : Int ,
    val message : String ,
    val typeMessage : String ,
    val date : String
)