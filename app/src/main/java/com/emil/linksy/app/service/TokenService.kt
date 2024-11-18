package com.emil.linksy.app.service

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.LifecycleService
import com.emil.data.TemporaryKeyStore
import com.emil.domain.usecase.RefreshTokenUseCase
import com.emil.linksy.presentation.ui.AuthActivity
import com.emil.linksy.util.TokenManager
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class TokenService: LifecycleService() {

    private val refreshTokenUseCase: RefreshTokenUseCase by inject()
    private val tokenManager: TokenManager by inject()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var refreshJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        startRefreshing(
            onIncorrect = {
                logoutUser()
            },
            onError = {
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRefreshing()
    }

    private fun startRefreshing(onIncorrect: () -> Unit, onError: () -> Unit) {
        refreshJob = scope.launch {
            while (true) {
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
                delay(TimeUnit.MINUTES.toMillis(TemporaryKeyStore.REFRESH_DELAY))
            }
        }
    }

    private fun stopRefreshing() {
        refreshJob?.cancel()
    }

    private fun logoutUser() {
        val sharedPref: SharedPreferences =
            getSharedPreferences("appData", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("remember", false)
        editor.apply()
        val authIntent = Intent(this, AuthActivity::class.java)
        authIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(authIntent)
    }
}