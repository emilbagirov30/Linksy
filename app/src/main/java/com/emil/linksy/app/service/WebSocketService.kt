package com.emil.linksy.app.service


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.LifecycleService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.emil.domain.model.MessageResponse

import com.emil.domain.usecase.ConnectToWebSocketUseCase
import com.emil.domain.usecase.DisconnectFromWebSocketUseCase
import com.emil.domain.usecase.SubscribeToUserChatsCountUseCase
import com.emil.linksy.util.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class WebSocketService : LifecycleService() {

    private val connectToWebSocketUseCase: ConnectToWebSocketUseCase by inject()
    private  val disconnectFromWebSocketUseCase: DisconnectFromWebSocketUseCase by inject()
    private val subscribeToUserChatsCountUseCase: SubscribeToUserChatsCountUseCase by inject()
    val tokenManager:TokenManager by inject<TokenManager> ()
    private val chatIdList:MutableSet<Long> =  mutableSetOf()
    override fun onCreate() {
        super.onCreate()
        val sharedPref: SharedPreferences = getSharedPreferences("AppData", Context.MODE_PRIVATE)
        val userId = sharedPref.getLong("ID",-1)
        CoroutineScope(Dispatchers.IO).launch {
            connectToWebSocketUseCase.invoke()
            subscribeToUserChatsCountUseCase.invoke(tokenManager.getAccessToken()).collect {chat ->
                if (chat.senderId == null || (chat.senderId!=userId  && !chatIdList.contains(chat.chatId))) {
                    notifyNewChat()
                    chatIdList.add(chat.chatId)
                }
            }
        }
    }
    private fun notifyNewChat() {
        val intent = Intent("NEW_CHAT_RECEIVED")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let { action ->
            when (action) {
                "CLEAR_CHAT_ID_LIST" -> {
                    chatIdList.clear()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onDestroy() {
        super.onDestroy()
          //disconnectFromWebSocketUseCase.invoke()
    }
}