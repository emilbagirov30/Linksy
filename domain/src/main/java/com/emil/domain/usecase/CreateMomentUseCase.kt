package com.emil.domain.usecase

import com.emil.domain.model.MomentData
import com.emil.domain.repository.MomentRepository
import retrofit2.Response

class CreateMomentUseCase (private val momentRepository: MomentRepository) {
   suspend fun execute (token:String,moment:MomentData):Response<Unit>{
      return momentRepository.createMoment(token,moment)
   }
}