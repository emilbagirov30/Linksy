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

    override suspend fun getUserPageData(token:String,id: Long): Response<UserPageDataResponse> {
        val response = RetrofitUserInstance.apiService.getUserPageData("Bearer $token",id)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModel())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun subscribe(token: String, id: Long): Response<Unit> {
     return  RetrofitUserInstance.apiService.subscribe("Bearer $token",id)
    }

    override suspend fun unsubscribe(token: String, id: Long): Response<Unit> {
        return  RetrofitUserInstance.apiService.unsubscribe("Bearer $token",id)
    }

    override suspend fun getUserSubscribers(token: String): Response<List<UserResponse>> {
        val response = RetrofitUserInstance.apiService.getUserSubscribers("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getUserSubscriptions(token: String): Response<List<UserResponse>> {
        val response = RetrofitUserInstance.apiService.getUserSubscriptions("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getOutsiderUserSubscribers(id: Long): Response<List<UserResponse>> {
        val response = RetrofitUserInstance.apiService.getOutsiderUserSubscribers(id)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getOutsiderSubscriptions(id: Long): Response<List<UserResponse>> {
        val response = RetrofitUserInstance.apiService.getOutsiderUserSubscriptions(id)
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun addToBlackList(token: String, userId: Long): Response<Unit> {
        return RetrofitUserInstance.apiService.addToBlackList("Bearer $token",userId)
    }

    override suspend fun removeFromBlackList(token: String, userId: Long): Response<Unit> {
        return RetrofitUserInstance.apiService.removeBlackList("Bearer $token",userId)
    }


}