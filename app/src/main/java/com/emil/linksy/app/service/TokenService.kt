package com.emil.linksy.app.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.IBinder
import com.emil.domain.usecase.room.ClearAllMessagesUseCase
import com.emil.domain.usecase.room.ClearChatsUseCase
import com.emil.domain.usecase.user.RefreshTokenUseCase
import com.emil.linksy.presentation.ui.auth.AuthActivity
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.TokenManager
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class TokenService: Service() {
    private val refreshTokenUseCase: RefreshTokenUseCase by inject()
    private val tokenManager: TokenManager by inject()
    private val clearChatsUseCase:ClearChatsUseCase by inject<ClearChatsUseCase> ()
    private val clearAllMessagesUseCase:ClearAllMessagesUseCase by inject<ClearAllMessagesUseCase> ()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var refreshJob: Job? = null
    private lateinit var handler: Handler
    private val retryRunnable = Runnable {
        startRefreshing(
            onIncorrect = { logoutUser() },
            onBlocked = { logoutUser() }
        )
    }

    override fun onCreate() {
        super.onCreate()
        handler = Handler(mainLooper)
        startRefreshing(
            onIncorrect = { logoutUser() },
            onBlocked = { logoutUser() }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRefreshing()
        handler.removeCallbacks(retryRunnable)
    }

    override fun onBind(p0: Intent?): IBinder? {
       return null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
    private fun startRefreshing(onIncorrect: () -> Unit,onBlocked: () -> Unit) {
        if (refreshJob?.isActive == true) return
        refreshJob = scope.launch {
            while (true) {
                try {
                    val currentRefreshToken = tokenManager.getRefreshToken()
                    if (currentRefreshToken != TokenManager.DEFAULT_TOKEN) {
                        val response = refreshTokenUseCase.execute(currentRefreshToken)
                        val code = response.code()
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
                    }
                } catch (e: Exception) {
                    stopRefreshing()
                    retryAfterError()
                }
                delay(TimeUnit.MINUTES.toMillis(Linksy.REFRESH_DELAY))
            }
        }
    }

    private fun stopRefreshing() {
        refreshJob?.cancel()
        refreshJob = null
    }
    private fun logoutUser() {
        val sharedPref: SharedPreferences = getSharedPreferences(Linksy.SHAREDPREF_MAIN_KEY, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(Linksy.SHAREDPREF_REMEMBER_KEY, false)
        editor.apply()
        tokenManager.clearTokens()
        CoroutineScope(Dispatchers.IO).launch {
            clearChatsUseCase.execute()
            clearAllMessagesUseCase.execute()
        }
        val authIntent = Intent(this, AuthActivity::class.java)
        authIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(authIntent)
    }

    private fun retryAfterError() {
        handler.removeCallbacks(retryRunnable)
        handler.postDelayed(retryRunnable, TimeUnit.SECONDS.toMillis(15))
    }
}