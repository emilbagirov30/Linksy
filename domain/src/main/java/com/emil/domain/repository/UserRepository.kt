package com.emil.domain.repository

import com.emil.domain.model.UserRegistrationData

interface UserRepository {
    suspend fun registerUser(request: UserRegistrationData)
}