package com.emil.data.repository

import com.emil.data.model.RegistrationRequest
import com.emil.data.model.toDomainModel
import com.emil.data.network.RetrofitInstance
import com.emil.domain.model.UserRegistrationData
import com.emil.domain.repository.UserRepository
import retrofit2.Response

class UserRepositoryImpl : UserRepository {
private val regRequest = RegistrationRequest ()
    override suspend fun registerUser(request: UserRegistrationData):Response<Unit> {
        regRequest.toDomainModel(request)
        return RetrofitInstance.apiService.registerUser(regRequest)
    }

}
