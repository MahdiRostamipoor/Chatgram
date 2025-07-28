package com.mahdi.rostamipour.chatgram.presenter.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdi.rostamipour.chatgram.data.service.MyPreferences
import com.mahdi.rostamipour.chatgram.domain.models.GetMessage
import com.mahdi.rostamipour.chatgram.domain.models.Typing
import com.mahdi.rostamipour.chatgram.domain.models.User
import com.mahdi.rostamipour.chatgram.domain.usecase.SocketUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SocketViewModel(private val socketUseCase: SocketUseCase) : ViewModel(){

    private val _message = MutableStateFlow<GetMessage?>(null)
    val message : StateFlow<GetMessage?> = _message

    private val _updateUsers = MutableStateFlow<List<User>?>(null)
    val updateUsers : StateFlow<List<User>?> = _updateUsers

    private val _typing = MutableStateFlow<Typing?>(null)
    val typing : StateFlow<Typing?> = _typing

    init {
        updateUsers()
        getMessage()
    }

    fun connectSocket(userId : Int){
        viewModelScope.launch {
            if (MyPreferences.userId != null){
                socketUseCase.connectSocket(userId)
            }
        }
    }

    fun getMessage(){
        viewModelScope.launch {
            socketUseCase.getMessages().collect {
                _message.value = it
            }
        }
    }

    fun updateUsers() {
        viewModelScope.launch {
            socketUseCase.getUsersList().collect {
                _updateUsers.value = it
            }


        }
    }

    fun getTyping() {
        viewModelScope.launch {
            socketUseCase.getTyping().collect {
                _typing.value = it
            }
        }
    }

    fun isTyping(senderId: Int , getterId: Int){
        viewModelScope.launch {
            socketUseCase.isTyping(senderId,getterId)
        }
    }

    fun stopTyping(senderId: Int , getterId: Int){
        viewModelScope.launch {
            socketUseCase.stopTyping(senderId,getterId)
        }
    }

}