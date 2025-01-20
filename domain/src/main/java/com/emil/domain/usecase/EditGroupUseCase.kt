package com.emil.domain.usecase

import com.emil.domain.model.GroupEditData
import com.emil.domain.repository.ChatRepository
import retrofit2.Response

class EditGroupUseCase (private val chatRepository: ChatRepository) {
    suspend fun execute (token:String,groupEditData: GroupEditData):Response<Unit>{
        return  chatRepository.editGroup(token,groupEditData)
    }
}