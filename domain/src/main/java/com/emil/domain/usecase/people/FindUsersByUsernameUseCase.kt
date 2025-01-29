package com.emil.domain.usecase.people

import com.emil.domain.model.UserResponse
import com.emil.domain.repository.PeopleRepository
import retrofit2.Response

class FindUsersByUsernameUseCase (private val peopleRepository: PeopleRepository) {

   suspend fun execute (token:String, startsWith:String): Response<List<UserResponse>> {
       return peopleRepository.findByUsername(token,startsWith)
   }
}