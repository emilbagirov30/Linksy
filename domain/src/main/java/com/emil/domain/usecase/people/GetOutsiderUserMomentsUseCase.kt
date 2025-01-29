package com.emil.domain.usecase.people

import com.emil.domain.model.MomentResponse
import com.emil.domain.repository.MomentRepository
import retrofit2.Response

class GetOutsiderUserMomentsUseCase(private val momentRepository: MomentRepository) {
    suspend fun execute (id:Long): Response<List<MomentResponse>> {
        return momentRepository.getOutsiderUserMoments(id)
    } }