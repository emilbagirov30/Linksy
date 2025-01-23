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
    override fun onCreate() {
        super.onCreate()
        val sharedPref: SharedPreferences = getSharedPreferences("appData", Context.MODE_PRIVATE)
        val userId = sharedPref.getLong("ID",-1)
        CoroutineScope(Dispatchers.IO).launch {
            connectToWebSocketUseCase.invoke()
            subscribeToUserChatsCountUseCase.invoke(tokenManager.getWsToken()).collect {chat ->
                if(chat.unreadMessagesCount!=null) {
                    if (chat.senderId == null || (chat.senderId != userId && chat.unreadMessagesCount!! > 0)) {
                        notifyNewChat()
                    }
                }
            }
        }
    }
    private fun notifyNewChat() {
        val intent = Intent("NEW_CHAT_RECEIVED")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
          //disconnectFromWebSocketUseCase.invoke()
    }
}