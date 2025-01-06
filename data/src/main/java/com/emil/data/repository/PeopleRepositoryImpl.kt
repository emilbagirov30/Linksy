package com.emil.data.repository

import com.emil.data.model.toDomainModel
import com.emil.data.model.toDomainModelList
import com.emil.data.network.RetrofitUserInstance
import com.emil.domain.model.UserPageDataResponse
import com.emil.domain.model.UserResponse
import com.emil.domain.repository.PeopleRepository
import retrofit2.Response

class PeopleRepositoryImpl:PeopleRepository {

    override suspend fun findByUsername(token: String, startsWith: String): Response<List<UserResponse>> {
        val response = RetrofitUserInstance.apiService.findUserByUsername("Bearer $token",startsWith)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun findByLink(token: String, startsWith: String): Response<List<UserResponse>> {
        val response = RetrofitUserInstance.apiService.findUserByLink("Bearer $token",startsWith)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getUserPageData(id: Long): Response<UserPageDataResponse> {
        val response = RetrofitUserInstance.apiService.getUserPageData(id)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModel())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }
}