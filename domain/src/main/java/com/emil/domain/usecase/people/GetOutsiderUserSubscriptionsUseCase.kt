package com.emil.domain.usecase.people

import com.emil.domain.model.UserResponse
import com.emil.domain.repository.PeopleRepository
import retrofit2.Response

class GetOutsiderUserSubscriptionsUseCase(private val peopleRepository: PeopleRepository) {

    suspend fun execute(id:Long): Response<List<UserResponse>> {
        return peopleRepository.getOutsiderSubscriptions(id)
    } }
