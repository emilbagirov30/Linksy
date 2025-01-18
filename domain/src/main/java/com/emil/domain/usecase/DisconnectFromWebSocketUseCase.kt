package com.emil.domain.usecase

import com.emil.domain.repository.WebSocketRepository

class DisconnectFromWebSocketUseCase(private val repository: WebSocketRepository) {
    operator fun invoke() {
        repository.disconnect()
    }
}