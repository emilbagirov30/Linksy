package com.emil.domain.repository

import com.emil.domain.model.MomentData
import retrofit2.Response

interface MomentRepository {
    suspend fun createMoment(token:String,moment:MomentData):Response<Unit>
}