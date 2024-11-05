package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.PasswordChangeData
import com.emil.domain.usecase.ConfirmPasswordChangeUseCase
import com.emil.domain.usecase.RequestPasswordChangeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecoveryPasswordViewModel (private val requestPasswordChangeUseCase: RequestPasswordChangeUseCase
, private val confirmPasswordChangeUseCase: ConfirmPasswordChangeUseCase):ViewModel () {

    fun requestPasswordChange (email:String,onSuccess: () -> Unit,onIncorrect: () -> Unit,onError: () -> Unit,onEnd: () -> Unit){
        viewModelScope.launch {
            try{
                val response = requestPasswordChangeUseCase.execute(email)
                when (response.code()){
                    200 -> withContext(Dispatchers.Main) {onSuccess ()}
                    404 -> withContext(Dispatchers.Main) {onIncorrect ()}
                }
            }catch (e:Exception){
                 onError ()
            }finally {
                withContext(Dispatchers.Main) {onEnd ()}
            }
        }
    }

    fun confirmPasswordChange (code:String,email:String,newPassword:String,onSuccess: () -> Unit,onIncorrect: () -> Unit,onError: () -> Unit,onEnd: () -> Unit){
        viewModelScope.launch {
            try{
                val response = confirmPasswordChangeUseCase.execute(PasswordChangeData(email,code,newPassword))
                when (response.code()){
                    200 -> withContext(Dispatchers.Main) {onSuccess ()}
                    401 -> withContext(Dispatchers.Main) {onIncorrect ()}
                }
            }catch (e:Exception){
                onError ()
            }finally {
                withContext(Dispatchers.Main) {onEnd ()}
            }
        }
    }


}