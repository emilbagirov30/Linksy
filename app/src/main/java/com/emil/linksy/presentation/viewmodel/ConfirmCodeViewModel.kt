package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.ConfirmCodeParam
import com.emil.domain.usecase.auth.ConfirmCodeUseCase
import com.emil.domain.usecase.auth.ResendCodeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfirmCodeViewModel(private val confirmUseCase: ConfirmCodeUseCase, private val resendCodeUseCase: ResendCodeUseCase) : ViewModel () {
    fun confirm (email:String,code:String,onSuccess: () -> Unit, onIncorrect: () -> Unit, onError: (Throwable) -> Unit){
        viewModelScope.launch {
            try{
              val response =  confirmUseCase.execute(ConfirmCodeParam(email,code))
              when (response.code()){
                  200 -> withContext(Dispatchers.Main) {onSuccess ()}
                  401 -> withContext(Dispatchers.Main) {onIncorrect ()}
                  else -> onError(Exception("Unknown response code: ${response.code()}"))
               }
            }catch (e: Exception) {
                onError(e)
            }
        }
    }
    fun resend (email:String, onError: () -> Unit){
        viewModelScope.launch {
            try {
                resendCodeUseCase.execute(email)
            } catch (e:Exception){
                onError()
            }
        }
    }
}