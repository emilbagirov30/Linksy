package com.emil.domain.usecase

import com.emil.domain.model.MomentResponse
import com.emil.domain.repository.MomentRepository
import retrofit2.Response

class GetUserUnseenMomentsUseCase (private val momentRepository: MomentRepository){
    suspend fun execute(token:String,userId:Long): Response<List<MomentResponse>>{
        return  momentRepository.getUserUnseenMoments(token, userId)
    }
}