package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.UserProfileData
import com.emil.domain.usecase.UserDataUseCase
import kotlinx.coroutines.launch

class UserProfileDataViewModel (private val userDataUseCase: UserDataUseCase): ViewModel () {

    private val _userData = MutableLiveData<UserProfileData> ()
    val userData:LiveData<UserProfileData>  = _userData

    fun getData(
        token: String,
        onSuccess: () -> Unit,
        onIncorrect: () -> Unit,
        onError: () -> Unit,
        onEnd: () -> Unit
    ) {

        viewModelScope.launch {
            try {
              val response = userDataUseCase.execute(token)
            if (response.isSuccessful){
                _userData.value = response.body()
                onSuccess ()
            }else {
                onIncorrect ()
            }
            }catch (e:Exception){
                onError ()
            }finally {
                onEnd ()
            }
        }

    }
}