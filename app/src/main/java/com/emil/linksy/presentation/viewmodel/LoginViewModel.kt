package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.UserLoginData
import com.emil.domain.usecase.ConfirmCodeUseCase
import com.emil.domain.usecase.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val loginUseCase: LoginUseCase):ViewModel () {

     fun login (email:String, password:String,onSuccess: () -> Unit,onIncorrect: () -> Unit, onError: () -> Unit, onEnd: () -> Unit){
        viewModelScope.launch {
            try {
                val response = loginUseCase.execute(UserLoginData(email, password))
                when (response.code()){
                    200 -> withContext(Dispatchers.Main) {onSuccess ()}
                    401 -> withContext(Dispatchers.Main) {onIncorrect ()}
                }
            }catch (e:Exception){
                onError()
            }finally {
                withContext(Dispatchers.Main) {onEnd ()}
            }
        }
    }
}