package com.emil.domain.usecase.websocket

import com.emil.domain.repository.WebSocketRepository

class DisconnectFromWebSocketUseCase(private val repository: WebSocketRepository) {
    operator fun invoke() {
        repository.disconnect()
    }
}