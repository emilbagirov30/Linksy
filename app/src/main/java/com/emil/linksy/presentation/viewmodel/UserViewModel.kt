package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.MessageMode
import com.emil.domain.model.UserProfileData
import com.emil.domain.model.UserResponse
import com.emil.domain.usecase.GetEveryoneOffTheBlacklistUseCase
import com.emil.domain.usecase.GetMessageModeUseCase
import com.emil.domain.usecase.SetMessageModeUseCase
import com.emil.domain.usecase.UserProfileDataUseCase
import kotlinx.coroutines.launch

class UserViewModel (private val userProfileDataUseCase: UserProfileDataUseCase,
                     private val getEveryoneOffTheBlacklistUseCase: GetEveryoneOffTheBlacklistUseCase,
                     private val getMessageModeUseCase: GetMessageModeUseCase,
                     private val setMessageModeUseCase: SetMessageModeUseCase): ViewModel () {

    private val _userData = MutableLiveData<UserProfileData> ()
    val userData:LiveData<UserProfileData>  = _userData

    private val _blacklist = MutableLiveData<List<UserResponse>> ()
    val blacklist:LiveData<List<UserResponse>>  = _blacklist

    private val _messageMode= MutableLiveData<MessageMode> ()
    val messageMode:LiveData<MessageMode>  = _messageMode


    fun getData(
        token: String,
        onIncorrect: () -> Unit,
        onError: () -> Unit,
    ) {
        viewModelScope.launch {
            try {
              val response = userProfileDataUseCase.execute(token)
            if (response.isSuccessful){
                _userData.value = response.body()
            }else {
                onIncorrect ()
            }
            }catch (e:Exception){
                onError ()
            }
        }

    }
    fun getBlackList(token:String,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getEveryoneOffTheBlacklistUseCase.execute(token)
                if (response.isSuccessful){
                    onSuccess()
                    _blacklist.value = response.body()
                }
            }catch (e:Exception){
                onError()
            }
        }
    }

    fun getMessageMode(token:String,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getMessageModeUseCase.execute(token)
                if (response.isSuccessful){
                    onSuccess()
                    _messageMode.value = response.body()
                }
            }catch (e:Exception){
                onError()
            }
        }
    }


    fun setMessageMode(token:String, mode: MessageMode,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = setMessageModeUseCase.execute(token,mode)
                if (response.isSuccessful){
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }
    }

}