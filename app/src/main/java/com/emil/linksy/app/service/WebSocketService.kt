package com.emil.linksy.app.service


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.LifecycleService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.emil.domain.usecase.websocket.ConnectToWebSocketUseCase
import com.emil.domain.usecase.websocket.DisconnectFromWebSocketUseCase
import com.emil.domain.usecase.websocket.SubscribeToUserChatsCountUseCase
import com.emil.linksy.util.Linksy
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
        val sharedPref: SharedPreferences = getSharedPreferences(Linksy.SHAREDPREF_MAIN_KEY, Context.MODE_PRIVATE)
        val userId = sharedPref.getLong(Linksy.SHAREDPREF_ID_KEY,Linksy.DEFAULT_ID)
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
        val intent = Intent(Linksy.INTENT_ACTION_NEW_CHAT)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
          disconnectFromWebSocketUseCase.invoke()
    }
}