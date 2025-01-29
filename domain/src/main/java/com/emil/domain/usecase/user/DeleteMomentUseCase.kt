package com.emil.domain.usecase.user

import com.emil.domain.repository.MomentRepository
import retrofit2.Response

class DeleteMomentUseCase (private val momentRepository: MomentRepository) {
    suspend fun execute (token:String,momentId:Long): Response<Unit> {
        return momentRepository.deleteMoment(token,momentId)
    }
}