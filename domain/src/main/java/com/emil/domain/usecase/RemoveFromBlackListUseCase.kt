package com.emil.domain.usecase

import com.emil.domain.repository.PeopleRepository
import retrofit2.Response

class RemoveFromBlackListUseCase(private val peopleRepository: PeopleRepository) {
    suspend fun execute (token:String,userId:Long): Response<Unit> {
        return  peopleRepository.removeFromBlackList(token, userId)
    }}