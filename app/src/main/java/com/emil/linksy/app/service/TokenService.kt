package com.emil.linksy.app.service

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import androidx.lifecycle.LifecycleService
import com.emil.data.TemporaryKeyStore
import com.emil.domain.usecase.RefreshTokenUseCase
import com.emil.linksy.presentation.ui.ErrorDialog
import com.emil.linksy.presentation.ui.auth.AuthActivity
import com.emil.linksy.util.TokenManager
import com.emil.presentation.R
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class TokenService: LifecycleService() {

    private val refreshTokenUseCase: RefreshTokenUseCase by inject()
    private val tokenManager: TokenManager by inject()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var refreshJob: Job? = null
    private lateinit var handler: Handler
    private val retryRunnable = Runnable {
        startRefreshing(
            onIncorrect = { logoutUser() },
            onError = { retryAfterError() },
            onBlocked = { logoutUser() }
        )
    }

    override fun onCreate() {
        super.onCreate()
        handler = Handler(mainLooper)
        startRefreshing(
            onIncorrect = { logoutUser() },
            onError = { retryAfterError() },
            onBlocked = { logoutUser() }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRefreshing()
        handler.removeCallbacks(retryRunnable)
    }

    private fun startRefreshing(onIncorrect: () -> Unit,onBlocked: () -> Unit, onError: () -> Unit) {
        if (refreshJob?.isActive == true) return
        refreshJob = scope.launch {
            while (true) {
                try {
                    val currentRefreshToken = tokenManager.getRefreshToken()
                    if (currentRefreshToken!="null") {
                        val response = refreshTokenUseCase.execute(currentRefreshToken)
                        val code = response.code()
                             println(code)
                        when(code){
                            200 ->  {
                                response.body()?.let { body ->
                                tokenManager.saveTokens(body.accessToken, body.refreshToken,body.wsToken)
                                val wsServiceIntent = Intent(applicationContext, WebSocketService::class.java)
                                startService(wsServiceIntent)
                            }}
                            401 -> {
                                onIncorrect()
                                cancel()
                            }
                            403 -> {
                                onBlocked()
                                cancel()
                            }
                        }
                    }else retryAfterError()
                } catch (e: Exception) {
                    stopRefreshing()
                    onError()
                }
                delay(TimeUnit.MINUTES.toMillis(TemporaryKeyStore.REFRESH_DELAY))
            }
        }
    }

    private fun stopRefreshing() {
        refreshJob?.cancel()
        refreshJob = null
    }
    private fun logoutUser() {
        println("вызван")
        val sharedPref: SharedPreferences =
            getSharedPreferences("appData", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("remember", false)
        editor.apply()
        tokenManager.clearTokens()
        val authIntent = Intent(this, AuthActivity::class.java)
        authIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(authIntent)
    }

    private fun retryAfterError() {
        handler.removeCallbacks(retryRunnable)
        handler.postDelayed(retryRunnable, TimeUnit.SECONDS.toMillis(30))
    }
}