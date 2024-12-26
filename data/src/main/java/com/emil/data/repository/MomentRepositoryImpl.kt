package com.emil.data.repository

import com.emil.data.model.MomentBody
import com.emil.data.model.PostBody
import com.emil.data.model.toDomainModel
import com.emil.data.model.toDomainModelList
import com.emil.data.network.RetrofitCloudInstance
import com.emil.data.network.RetrofitUserInstance
import com.emil.domain.model.MomentData
import com.emil.domain.model.MomentResponse
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

    override suspend fun getUserMoments(token: String): Response<List<MomentResponse>> {
        val response = RetrofitUserInstance.apiService.getUserMoments("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun deleteMoment(token: String, momentId: Long): Response<Unit> {
        return RetrofitUserInstance.apiService.deleteMoment("Bearer $token",momentId)
    }
}