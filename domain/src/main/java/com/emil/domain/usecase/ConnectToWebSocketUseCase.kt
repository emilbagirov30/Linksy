package com.emil.domain.usecase

import com.emil.domain.repository.WebSocketRepository

class ConnectToWebSocketUseCase(private val repository: WebSocketRepository) {
    operator fun invoke() {
        repository.connect()
    }
}