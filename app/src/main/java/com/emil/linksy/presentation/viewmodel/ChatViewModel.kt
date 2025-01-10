package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.ChatResponse
import com.emil.domain.model.GroupData
import com.emil.domain.model.UserResponse
import com.emil.domain.model.toLocalModel
import com.emil.domain.model.toResponseModelList
import com.emil.domain.usecase.CreateGroupUseCase
import com.emil.domain.usecase.GetChatIdUseCase
import com.emil.domain.usecase.GetGroupMembersUseCase
import com.emil.domain.usecase.GetUserChatsFromLocalDb
import com.emil.domain.usecase.GetUserChatsUseCase

import com.emil.domain.usecase.InsertChatInLocalDbUseCase

import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ChatViewModel(private val getUserChatsUseCase: GetUserChatsUseCase,
                    private val insertChatInLocalDbUseCase: InsertChatInLocalDbUseCase,
                    private val  getUserChatsFromLocalDb: GetUserChatsFromLocalDb,
    private val getChatIdUseCase: GetChatIdUseCase, private val createGroupUseCase: CreateGroupUseCase,
    private val getGroupMembersUseCase: GetGroupMembersUseCase
) : ViewModel(){
    private val _chatList = MutableLiveData<List<ChatResponse>> ()
    val chatList: LiveData<List<ChatResponse>> = _chatList
    private var _chatId = MutableLiveData<Long> ()
    val chatId: LiveData<Long> = _chatId


    private val _memberList = MutableLiveData<List<UserResponse>> ()
    val memberList: LiveData<List<UserResponse>> = _memberList


    fun getUserChats(token: String,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getUserChatsUseCase.execute(token)
                if (response.isSuccessful) {
                    _chatList.value = response.body()
                    onSuccess()
                }
            } catch (e: Exception) {
                onError()
            }
        }}

        fun insertChat(chatResponse: ChatResponse,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
            viewModelScope.launch {
                try {
                   insertChatInLocalDbUseCase.execute(chatResponse.toLocalModel())
                }catch (e:Exception){
                    onError ()
                }
            }
        }

        fun getUserChatsFromLocalDb(onSuccess: ()->Unit = {},onError: ()->Unit = {}){
            viewModelScope.launch {
                    val chats = getUserChatsFromLocalDb.execute()
                    _chatList.value = chats.toResponseModelList()

            }
        }
    fun getChatId(token:String,userId:Long,onSuccess: ()->Unit = {},onError: ()->Unit = {}){
        viewModelScope.launch {
            val response = getChatIdUseCase.execute(token, userId)
          if(response.isSuccessful){
              _chatId.value = response.body()
          }

        }
    }
    fun createGroup(token:String, ids:String, name:String, avatar: MultipartBody.Part?, onSuccess: ()->Unit = {}, onError: ()->Unit = {}){
        viewModelScope.launch {
            try {
            val response = createGroupUseCase.execute(token, GroupData(ids,name,avatar))
            if(response.isSuccessful){
              onSuccess()

            }
            }catch (e:Exception){
                onError()
            }
        }
    }

    fun getGroupMembers(token:String, groupId:Long, onSuccess: ()->Unit = {}, onError: ()->Unit = {}){
        viewModelScope.launch {
            try {
                val response = getGroupMembersUseCase.execute(token,groupId)
                if(response.isSuccessful){
                    _memberList.value = response.body()
                }
            }catch (e:Exception){
                onError()
            }
        }
    }
}