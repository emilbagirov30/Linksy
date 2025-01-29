package com.emil.domain.usecase.people

import com.emil.domain.model.UserPageDataResponse
import com.emil.domain.repository.PeopleRepository
import retrofit2.Response

class GetUserPageDataUseCase ( private val peopleRepository: PeopleRepository) {

    suspend fun execute(token:String,id: Long): Response<UserPageDataResponse> {
        return peopleRepository.getUserPageData(token,id)
    }
}
