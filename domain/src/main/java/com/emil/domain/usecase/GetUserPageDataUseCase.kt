package com.emil.domain.usecase

import com.emil.domain.model.UserPageDataResponse
import com.emil.domain.model.UserResponse
import com.emil.domain.repository.PeopleRepository
import retrofit2.Response

class GetUserPageDataUseCase ( private val peopleRepository: PeopleRepository) {

    suspend fun execute(id: Long): Response<UserPageDataResponse> {
        return peopleRepository.getUserPageData(id)
    }
}
