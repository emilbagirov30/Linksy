package com.emil.domain.usecase

import com.emil.domain.repository.PeopleRepository
import retrofit2.Response

class AddToBlackListUseCase (private val peopleRepository: PeopleRepository) {
    suspend fun execute (token:String,userId:Long):Response<Unit>{
        return  peopleRepository.addToBlackList(token, userId)
    }
}