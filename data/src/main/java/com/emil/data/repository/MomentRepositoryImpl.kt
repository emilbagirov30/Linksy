package com.emil.data.repository

import com.emil.data.model.MomentBody
import com.emil.data.model.PostBody
import com.emil.data.model.toDomainModel
import com.emil.data.network.RetrofitCloudInstance
import com.emil.domain.model.MomentData
import com.emil.domain.repository.MomentRepository
import retrofit2.Response

class MomentRepositoryImpl:MomentRepository {
    private val momentBody = MomentBody ()
    override suspend fun createMoment(token: String, moment: MomentData): Response<Unit> {
        return RetrofitCloudInstance.apiService.createMoment("Bearer $token",
            momentBody.toDomainModel(moment).image,
            momentBody.toDomainModel(moment).video,
            momentBody.toDomainModel(moment).audio,
            momentBody.toDomainModel(moment).text
            )
    }
}