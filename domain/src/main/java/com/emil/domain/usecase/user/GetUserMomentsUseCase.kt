package com.emil.domain.usecase.user

import com.emil.domain.model.MomentResponse
import com.emil.domain.repository.MomentRepository
import retrofit2.Response

class GetUserMomentsUseCase (private val momentRepository: MomentRepository) {
    suspend fun execute (token:String):Response<List<MomentResponse>>{
        return momentRepository.getUserMoments(token)
    }
}