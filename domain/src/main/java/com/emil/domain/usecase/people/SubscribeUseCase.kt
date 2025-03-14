package com.emil.domain.usecase.people

import com.emil.domain.repository.PeopleRepository
import retrofit2.Response

class SubscribeUseCase (private val peopleRepository: PeopleRepository) {

    suspend fun execute(token:String,id: Long): Response<Unit> {
        return peopleRepository.subscribe(token,id)
    } }
