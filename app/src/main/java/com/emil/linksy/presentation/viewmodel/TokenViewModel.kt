package com.emil.linksy.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.data.TemporaryKeyStore
import com.emil.domain.usecase.RefreshTokenUseCase
import com.emil.linksy.util.TokenManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TokenViewModel(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    context: Context
) : ViewModel() {
    private val tokenManager = TokenManager(context)
    private var refreshJob: Job? = null

    fun startRefreshing(onIncorrect: () -> Unit, onError: () -> Unit) {
        refreshJob = viewModelScope.launch {
            while (true) {
                delay(TimeUnit.MINUTES.toMillis(TemporaryKeyStore.REFRESH_DELAY))
                try {
                    val currentRefreshToken = tokenManager.getRefreshToken()
                    if (!currentRefreshToken.isNullOrEmpty()) {
                        val response = refreshTokenUseCase.execute(currentRefreshToken)
                        if (response.isSuccessful) {
                            response.body()?.let { body ->
                                tokenManager.saveTokens(body.accessToken, body.refreshToken)
                            }
                        } else if (response.code() == 401) {
                            onIncorrect()
                            cancel()
                        }
                    }
                } catch (e: Exception) {
                    onError()
                }
            }
        }
    }

    fun stopRefreshing() {
        refreshJob?.cancel()
    }
}
