package com.emil.data.repository

import com.emil.data.model.ConfirmCodeQuery
import com.emil.data.model.PasswordRecoveryBody
import com.emil.data.model.RegistrationBody
import com.emil.data.model.UserLoginBody
import com.emil.data.model.toDomainModel
import com.emil.data.network.RetrofitInstance
import com.emil.domain.model.ConfirmCodeParam
import com.emil.domain.model.PasswordRecoveryData
import com.emil.domain.model.Token
import com.emil.domain.model.UserLoginData
import com.emil.domain.model.UserRegistrationData
import com.emil.domain.repository.AuthRepository
import retrofit2.Response

class AuthRepositoryImpl : AuthRepository {
    private val regRequest = RegistrationBody ()
    private val confirmCodeParam = ConfirmCodeQuery ()
    private val loginRequest = UserLoginBody ()
    private val passwordRecoveryRequest = PasswordRecoveryBody ()
    override suspend fun registerUser(request: UserRegistrationData):Response<Unit> {
        return RetrofitInstance.apiService.registerUser(regRequest.toDomainModel(request))
    }

    override suspend fun confirmCode(param: ConfirmCodeParam): Response<Unit> {
        return  RetrofitInstance.apiService.confirmCode(confirmCodeParam.toDomainModel(param).email,confirmCodeParam.toDomainModel(param).code)
    }

    override suspend fun resendCode(emailParam: String): Response<Unit> {
        return RetrofitInstance.apiService.resendCode(emailParam)
    }

    override suspend fun logIn(userLogin: UserLoginData): Response<Token> {
        val response = RetrofitInstance.apiService.login(loginRequest.toDomainModel(userLogin))
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModel())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun requestPasswordChange(emailParam: String): Response<Unit> {
        return RetrofitInstance.apiService.requestPasswordChange(emailParam)
    }

    override suspend fun confirmPasswordChange(passwordRecoveryData: PasswordRecoveryData): Response<Unit> {
        return RetrofitInstance.apiService.confirmPasswordChange(passwordRecoveryRequest.toDomainModel(passwordRecoveryData))
    }

}
