package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.UserResponse
import com.emil.domain.usecase.GetUsersByLinkUseCase
import com.emil.domain.usecase.GetUsersByUsernameUseCase
import kotlinx.coroutines.launch

class PeopleViewModel(private val getUsersByUsernameUseCase: GetUsersByUsernameUseCase,
                      private val getUsersByLinkUseCase: GetUsersByLinkUseCase):ViewModel() {

    private val _userList = MutableLiveData<List<UserResponse>> ()
    val userList: LiveData<List<UserResponse>> = _userList

    fun findByUsername(token: String, startsWith: String,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
            val response = getUsersByUsernameUseCase.execute(token, startsWith)
            if (response.isSuccessful){
                _userList.value = response.body()
                onSuccess()
            }
            }catch (e:Exception){
                onError()
            }
        }

    }


    fun findByLink(token: String, startsWith: String,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getUsersByLinkUseCase.execute(token, startsWith.substring(1))
                if (response.isSuccessful){
                    _userList.value = response.body()
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }

    }

}