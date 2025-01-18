package com.emil.linksy.app.service


import android.content.Intent
import androidx.lifecycle.LifecycleService
import com.emil.domain.model.MessageResponse
import com.emil.domain.model.toLocalModel

import com.emil.domain.usecase.ConnectToWebSocketUseCase
import com.emil.domain.usecase.DisconnectFromWebSocketUseCase
import com.emil.domain.usecase.InsertMessageInLocalDbUseCase
import com.emil.domain.usecase.SubscribeToUserMessagesUseCase
import com.emil.linksy.presentation.viewmodel.MessageViewModel
import com.emil.linksy.util.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class WebSocketService : LifecycleService() {

    private val connectToWebSocketUseCase: ConnectToWebSocketUseCase by inject()
    private  val disconnectFromWebSocketUseCase: DisconnectFromWebSocketUseCase by inject()
    private val subscribeToUserMessagesUseCase: SubscribeToUserMessagesUseCase by inject()
    private val insertMessage:InsertMessageInLocalDbUseCase by inject ()
    val tokenManager:TokenManager by inject<TokenManager> ()

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            connectToWebSocketUseCase.invoke()
        //   subscribeToUserMessagesUseCase.invoke(tokenManager.getAccessToken()).collect { message ->
          //      saveMessage(message)
         //   }
        }
    }

    private fun saveMessage(message: MessageResponse) {
        message.run {
            CoroutineScope(Dispatchers.IO).launch {
            insertMessage.execute(message.toLocalModel())

        }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
     //  disconnectFromWebSocketUseCase.invoke()
    }
}