package com.emil.domain.repository

import com.emil.domain.model.ConfirmCodeParam
import com.emil.domain.model.PasswordRecoveryData
import com.emil.domain.model.Token
import com.emil.domain.model.UserLoginData
import com.emil.domain.model.UserRegistrationData
import retrofit2.Response

interface AuthRepository {
    suspend fun registerUser(request: UserRegistrationData): Response<Unit>
    suspend fun confirmCode(param: ConfirmCodeParam): Response<Unit>
    suspend fun resendCode(emailParam: String): Response<Unit>
    suspend fun logIn(userLogin:UserLoginData): Response<Token>
    suspend fun requestPasswordChange (emailParam:String): Response<Unit>
    suspend fun confirmPasswordChange (passwordRecoveryData: PasswordRecoveryData):Response<Unit>
}