package com.mahdi.rostamipour.chatgram.presenter.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdi.rostamipour.chatgram.domain.models.GetMessage
import com.mahdi.rostamipour.chatgram.domain.usecase.MessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MessageViewModel(val messageUseCase: MessageUseCase) : ViewModel() {

    private val _messages = MutableStateFlow<List<GetMessage>>(emptyList())
    val messages : StateFlow<List<GetMessage>> = _messages

    fun getMessages(senderId : Int , getterId : Int){
        viewModelScope.launch {
            _messages.value = messageUseCase.getMessages(senderId,getterId)
        }
    }

}