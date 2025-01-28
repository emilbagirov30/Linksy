package com.emil.domain.usecase

import com.emil.domain.repository.MomentRepository
import retrofit2.Response

class ViewMomentUseCase (private val momentRepository: MomentRepository){
    suspend fun execute (token:String,momentId:Long):Response<Unit>{
        return momentRepository.viewMoment(token, momentId)
    }
}