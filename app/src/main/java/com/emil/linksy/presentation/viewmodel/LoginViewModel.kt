package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.UserLoginData
import com.emil.domain.usecase.auth.LoginUseCase
import com.emil.linksy.util.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class LoginViewModel(private val loginUseCase: LoginUseCase, private val tokenManager: TokenManager):ViewModel () {
     fun login (email:String, password:String,onSuccess: () -> Unit,onIncorrect: () -> Unit,
                onError: () -> Unit, onEnd: () -> Unit, onBlock: () -> Unit){
        viewModelScope.launch {
            try {
                val response = loginUseCase.execute(UserLoginData(email, password))

                when (response.code()){
                    200 -> withContext(Dispatchers.Main) {
                        onSuccess ()
                        val accessToken = response.body()?.accessToken
                        val refreshToken = response.body()?.refreshToken
                        val wsToken = response.body()?.wsToken
                        if (accessToken != null && refreshToken != null && wsToken != null) {
                            tokenManager.saveTokens(accessToken, refreshToken, wsToken)
                        }
                    }
                    404 -> withContext(Dispatchers.Main) {onIncorrect ()}
                    403 -> onBlock()
                }
            }catch (e:Exception){
                onError()
            }finally {
                withContext(Dispatchers.Main) {onEnd ()}
            }
        }
    }
}