package com.emil.linksy.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.PasswordChangeData
import com.emil.domain.usecase.ChangePasswordUseCase
import kotlinx.coroutines.launch

class PasswordChangeViewModel (private val changePasswordUseCase: ChangePasswordUseCase):ViewModel() {
   @SuppressLint("SuspiciousIndentation")
   fun changePassword (token:String, oldPassword:String, newPassword:String, onSuccess: () -> Unit, onIncorrect: () -> Unit, onError: () -> Unit, onEnd: () -> Unit) {
       viewModelScope.launch {
           try {
          val response = changePasswordUseCase.execute(token, PasswordChangeData(oldPassword, newPassword))
           if (response.isSuccessful) onSuccess()
               if (response.code()==404) onIncorrect ()
           }catch (e:Exception){
               onError()
           }finally {
               onEnd ()
           }

       }
   }



}