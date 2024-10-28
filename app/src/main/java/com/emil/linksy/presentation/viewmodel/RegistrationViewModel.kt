package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.UserRegistrationData
import com.emil.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch

class RegistrationViewModel(private val registerUseCase: RegisterUseCase ) : ViewModel() {
    fun register(username: String, email: String, password: String, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                registerUseCase.execute(UserRegistrationData(username, email, password))
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}