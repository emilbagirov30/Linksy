package com.emil.domain.usecase

import com.emil.domain.model.Status
import com.emil.domain.repository.WebSocketRepository

class SendStatusUseCase  (private val repository: WebSocketRepository){
    operator fun invoke (status: Status){
        repository.sendStatus(status)
    }
}