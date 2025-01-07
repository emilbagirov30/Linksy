package com.emil.domain.usecase

import com.emil.domain.model.UserResponse
import com.emil.domain.repository.PeopleRepository
import retrofit2.Response

class GetUserSubscribersUseCase(private val peopleRepository: PeopleRepository) {

    suspend fun execute(token:String): Response<List<UserResponse>> {
        return peopleRepository.getUserSubscribers(token)
    } }
