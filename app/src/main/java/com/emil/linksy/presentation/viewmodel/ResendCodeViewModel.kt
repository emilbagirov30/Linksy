package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.usecase.ResendCodeUseCase
import kotlinx.coroutines.launch

class ResendCodeViewModel (private val resendCodeUseCase:ResendCodeUseCase): ViewModel (){
    fun resend (email:String,
                onError: () -> Unit){
        viewModelScope.launch {
            try {
                resendCodeUseCase.execute(email)
            }catch (e:Exception){
                onError()
            }
        }
    }
}