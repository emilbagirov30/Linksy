package com.emil.data.repository

import com.emil.data.model.ReportBody
import com.emil.data.model.toDomainModel
import com.emil.data.model.toDomainModelList
import com.emil.data.network.RetrofitInstance
import com.emil.domain.model.ReportRequest
import com.emil.domain.model.UserPageDataResponse
import com.emil.domain.model.UserResponse
import com.emil.domain.repository.PeopleRepository
import retrofit2.Response

class PeopleRepositoryImpl:PeopleRepository {
          private val reportBody = ReportBody()
    override suspend fun findByUsername(token: String, startsWith: String): Response<List<UserResponse>> {
        val response = RetrofitInstance.apiService.findUserByUsername("Bearer $token",startsWith)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun findByLink(token: String, startsWith: String): Response<List<UserResponse>> {
        val response = RetrofitInstance.apiService.findUserByLink("Bearer $token",startsWith)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getUserPageData(token:String,id: Long): Response<UserPageDataResponse> {
        val response = RetrofitInstance.apiService.getUserPageData("Bearer $token",id)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModel())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun subscribe(token: String, id: Long): Response<Unit> {
     return  RetrofitInstance.apiService.subscribe("Bearer $token",id)
    }

    override suspend fun unsubscribe(token: String, id: Long): Response<Unit> {
        return  RetrofitInstance.apiService.unsubscribe("Bearer $token",id)
    }

    override suspend fun getUserSubscribers(token: String): Response<List<UserResponse>> {
        val response = RetrofitInstance.apiService.getUserSubscribers("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getUserSubscriptions(token: String): Response<List<UserResponse>> {
        val response = RetrofitInstance.apiService.getUserSubscriptions("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getOutsiderUserSubscribers(id: Long): Response<List<UserResponse>> {
        val response = RetrofitInstance.apiService.getOutsiderUserSubscribers(id)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getOutsiderSubscriptions(id: Long): Response<List<UserResponse>> {
        val response = RetrofitInstance.apiService.getOutsiderUserSubscriptions(id)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun addToBlackList(token: String, userId: Long): Response<Unit> {
        return RetrofitInstance.apiService.addToBlackList("Bearer $token",userId)
    }

    override suspend fun removeFromBlackList(token: String, userId: Long): Response<Unit> {
        return RetrofitInstance.apiService.removeBlackList("Bearer $token",userId)
    }

    override suspend fun sendReport(token: String, report: ReportRequest): Response<Unit> {
        return RetrofitInstance.apiService.sendReport("Bearer $token",reportBody.toDomainModel(report))
    }


}