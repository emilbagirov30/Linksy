package com.emil.domain.repository

import com.emil.domain.model.ConfirmCodeParam
import com.emil.domain.model.UserRegistrationData
import retrofit2.Response

interface UserRepository {
    suspend fun registerUser(request: UserRegistrationData): Response<Unit>
    suspend fun confirmCode(param: ConfirmCodeParam): Response<Unit>
}