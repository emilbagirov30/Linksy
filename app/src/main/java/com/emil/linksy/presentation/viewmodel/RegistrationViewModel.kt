package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.UserRegistrationData
import com.emil.domain.usecase.RegisterUseCase
import com.emil.linksy.util.BackgroundState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationViewModel(private val registerUseCase: RegisterUseCase) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _passwordConfirm = MutableLiveData<String>()
    val passwordConfirm: LiveData<String> = _passwordConfirm

    private val _errorStates = MutableLiveData<Map<String, Boolean>>()
    val errorStates: LiveData<Map<String, Boolean>> = _errorStates

    private val _backgroundStates = MutableLiveData<Map<String, BackgroundState>>()
    val backgroundStates: LiveData<Map<String, BackgroundState>> = _backgroundStates

    init {
        _errorStates.value = mapOf(
            "emailExist" to false,
            "emailInvalidFormat" to false,
            "passwordMismatch" to false,
            "passwordShort" to false
        )

        _backgroundStates.value = mapOf(
            "username" to BackgroundState.DEFAULT,
            "email" to BackgroundState.DEFAULT,
            "password" to BackgroundState.DEFAULT,
            "passwordConfirm" to BackgroundState.DEFAULT
        )
    }
    fun setUsername(value: String) { _username.value = value }
    fun setEmail(value: String) { _email.value = value }
    fun setPassword(value: String) { _password.value = value }
    fun setPasswordConfirm(value: String) { _passwordConfirm.value = value }
    fun setErrorState(errorKey: String, isVisible: Boolean) {
        _errorStates.value = _errorStates.value?.toMutableMap()?.apply {
            this[errorKey] = isVisible
        }
    }

    fun setBackgroundState(key: String, state: BackgroundState) {
        _backgroundStates.value = _backgroundStates.value?.toMutableMap()?.apply {
            this[key] = state
        }
    }

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