package com.emil.domain.repository

import com.emil.domain.model.MomentData
import com.emil.domain.model.MomentResponse
import retrofit2.Response

interface MomentRepository {
    suspend fun createMoment(token:String,moment:MomentData):Response<Unit>
    suspend fun getUserMoments(token:String):Response<List<MomentResponse>>
    suspend fun deleteMoment (token: String,momentId:Long):Response<Unit>
}