package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.UserPageDataResponse
import com.emil.domain.model.UserResponse
import com.emil.domain.usecase.FindUsersByLinkUseCase
import com.emil.domain.usecase.FindUsersByUsernameUseCase
import com.emil.domain.usecase.GetUserPageDataUseCase
import kotlinx.coroutines.launch

class PeopleViewModel(private val findUsersByUsernameUseCase: FindUsersByUsernameUseCase,
                      private val findUsersByLinkUseCase: FindUsersByLinkUseCase,
                      private val getUserPageDataUseCase: GetUserPageDataUseCase
    ):ViewModel() {

    private val _userList = MutableLiveData<List<UserResponse>> ()
    val userList: LiveData<List<UserResponse>> = _userList

    private val _pageData = MutableLiveData<UserPageDataResponse> ()
    val pageData: LiveData<UserPageDataResponse> = _pageData



    fun findByUsername(token: String, startsWith: String,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
            val response = findUsersByUsernameUseCase.execute(token, startsWith)
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
                val response = findUsersByLinkUseCase.execute(token, startsWith.substring(1))
                if (response.isSuccessful){
                    _userList.value = response.body()
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }

    }
    fun getUserPageData (id:Long,onSuccess: ()->Unit = {},onError: ()->Unit = {}){
        viewModelScope.launch {
            try {
                val response = getUserPageDataUseCase.execute(id)
                if (response.isSuccessful){
                  _pageData.value = response.body()
                }
            }catch (e:Exception){
                onError()
            }
        }
    }

}