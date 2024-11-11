package com.emil.linksy.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.UserLoginData
import com.emil.domain.usecase.LoginUseCase
import com.emil.linksy.util.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val loginUseCase: LoginUseCase, context: Context):ViewModel () {
    private val tokenManager = TokenManager(context)
     fun login (email:String, password:String,onSuccess: () -> Unit,onIncorrect: () -> Unit, onError: () -> Unit, onEnd: () -> Unit){
        viewModelScope.launch {
            try {
                val response = loginUseCase.execute(UserLoginData(email, password))

                when (response.code()){
                    200 -> withContext(Dispatchers.Main) {
                        onSuccess ()
                        val accessToken = response.body()?.accessToken
                        val refreshToken = response.body()?.refreshToken
                        if (accessToken != null && refreshToken != null) {
                            tokenManager.saveTokens(accessToken, refreshToken)
                        }
                    }
                    404 -> withContext(Dispatchers.Main) {onIncorrect ()}
                }
            }catch (e:Exception){
                onError()
            }finally {
                withContext(Dispatchers.Main) {onEnd ()}
            }
        }
    }
}