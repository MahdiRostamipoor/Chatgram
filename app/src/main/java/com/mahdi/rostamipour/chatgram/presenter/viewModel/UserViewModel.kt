package com.mahdi.rostamipour.chatgram.presenter.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahdi.rostamipour.chatgram.domain.models.User
import com.mahdi.rostamipour.chatgram.domain.usecase.UserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userUseCase : UserUseCase) : ViewModel() {

    private val _userID = MutableStateFlow<String?>(null)
    val userID : StateFlow<String?> = _userID

    private val _usersList = MutableStateFlow<List<User>>(emptyList())
    val usersList : StateFlow<List<User>> = _usersList

    fun registerUser(name : String , bio : String){
        viewModelScope.launch {
            val id = userUseCase.registerUser(name,bio)
            _userID.value = id
        }
    }

    fun fetchUsers(userId : Int){
        viewModelScope.launch {
            _usersList.value = userUseCase.fetchUsersList(userId)
        }
    }

}