package com.emil.data.repository

import com.emil.data.model.ConfirmCodeQuery
import com.emil.data.model.RegistrationBody
import com.emil.data.model.toDomainModel
import com.emil.data.network.RetrofitInstance
import com.emil.domain.model.ConfirmCodeParam
import com.emil.domain.model.UserRegistrationData
import com.emil.domain.repository.AuthRepository
import retrofit2.Response

class AuthRepositoryImpl : AuthRepository {
private val regRequest = RegistrationBody ()
    private val confirmCodeParam =ConfirmCodeQuery ()
    override suspend fun registerUser(request: UserRegistrationData):Response<Unit> {
        return RetrofitInstance.apiService.registerUser(regRequest.toDomainModel(request))
    }

    override suspend fun confirmCode(param: ConfirmCodeParam): Response<Unit> {
        return  RetrofitInstance.apiService.confirmCode(confirmCodeParam.toDomainModel(param).email,confirmCodeParam.toDomainModel(param).code)
    }

}
