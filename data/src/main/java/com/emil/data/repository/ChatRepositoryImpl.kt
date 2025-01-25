package com.emil.data.repository

import com.emil.data.model.ChatLocalDb
import com.emil.data.model.GroupBody
import com.emil.data.model.GroupEditBody
import com.emil.data.model.PostBody
import com.emil.data.model.toDomainModel
import com.emil.data.model.toDomainModelList
import com.emil.data.network.RetrofitCloudInstance
import com.emil.data.network.RetrofitUserInstance
import com.emil.domain.model.ChatLocal
import com.emil.domain.model.ChatResponse
import com.emil.domain.model.GroupData
import com.emil.domain.model.GroupEditData
import com.emil.domain.model.GroupResponse
import com.emil.domain.model.UserResponse
import com.emil.domain.repository.ChatRepository
import retrofit2.Response

class ChatRepositoryImpl(private val chatDao: ChatDao):ChatRepository {
    private val chatLocalDb = ChatLocalDb();
    private val groupBody = GroupBody ()
    private val groupEditBody = GroupEditBody ()
    override suspend fun getUserChats(token: String): Response<List<ChatResponse>> {
        val response = RetrofitUserInstance.apiService.getUserChats("Bearer $token")
        return if (response.isSuccessful) {
            Response.success(response.body()?.toDomainModelList())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getAllChats(): List<ChatLocal> {
      return chatDao.getAllChats().toDomainModelList()
    }

    override suspend fun getChatId(token: String, userId: Long): Response<Long> {
        return RetrofitUserInstance.apiService.getChatId("Bearer $token",userId)
    }

    override suspend fun insertChat(chat: ChatLocal) {
        return chatDao.insertChat(chatLocalDb.toDomainModel(chat))
    }

    override suspend fun isGroup(chatId:Long):Boolean {
        return chatDao.isGroup(chatId)
    }

    override suspend fun createGroup(token: String, groupData: GroupData): Response<Unit> {
        return RetrofitCloudInstance.apiService.createGroup("Bearer $token",
            groupBody.toDomainModel(groupData).participantIds,
            groupBody.toDomainModel(groupData).name,
            groupBody.toDomainModel(groupData).avatar
            )
    }

    override suspend fun getGroupMembers(token: String, groupId: Long): Response<List<UserResponse>> {
        val response = RetrofitUserInstance.apiService.getGroupMembers("Bearer $token",groupId)
        return if (response.isSuccessful)
            Response.success(response.body()?.toDomainModelList())
         else Response.error(response.code(), response.errorBody()!!)

    }

    override suspend fun getGroupData(token: String, groupId: Long): Response<GroupResponse> {
        val response = RetrofitUserInstance.apiService.getGroupData("Bearer $token",groupId)
        return if(response.isSuccessful)
            Response.success(response.body()?.toDomainModel())
        else Response.error(response.code(), response.errorBody()!!)
    }

    override suspend fun editGroup(token: String, editData: GroupEditData): Response<Unit> {
        return RetrofitCloudInstance.apiService.editGroup("Bearer $token",
            groupEditBody.toDomainModel(editData).groupId,
            groupEditBody.toDomainModel(editData).name,
            groupEditBody.toDomainModel(editData).oldAvatarUrl,
            groupEditBody.toDomainModel(editData).avatar
        )
    }

    override suspend fun deleteChat(token: String, chatId: Long): Response<Unit> {
        return  RetrofitUserInstance.apiService.deleteChat("Bearer $token",chatId)
    }

    override suspend fun leaveTheGroup(token: String, groupId: Long): Response<Unit> {
       return  RetrofitUserInstance.apiService.leaveTheGroup("Bearer $token",groupId)
    }

    override suspend fun addMembers(token: String, groupId: Long, newMembers: List<Long>, ): Response<Unit> {
        return  RetrofitUserInstance.apiService.addMembersToGroup("Bearer $token",groupId,newMembers)
    }

    override suspend fun clearChats() {
        return chatDao.deleteChats()
    }
}
