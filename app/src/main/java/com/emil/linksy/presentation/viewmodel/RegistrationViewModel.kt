package com.emil.linksy.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.UserRegistrationData
import com.emil.domain.usecase.RegisterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationViewModel(private val registerUseCase: RegisterUseCase ) : ViewModel() {
    fun register(username: String,
                 email: String,
                 password: String,
                 onAccepted: () -> Unit,
                 onConflict: () -> Unit,
                 onEnd: () -> Unit,
                 onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                val response = registerUseCase.execute(UserRegistrationData(username, email, password))
                when (response.code()) {
                    202 ->  withContext(Dispatchers.Main) {onAccepted()}
                    409 ->  withContext(Dispatchers.Main) {onConflict()}
                    else -> onError(Exception("Unknown response code: ${response.code()}"))
                }
            } catch (e: Exception) {
                onError(e)
            }finally {
                withContext(Dispatchers.Main) {onEnd ()}
            }
        }
    }
}