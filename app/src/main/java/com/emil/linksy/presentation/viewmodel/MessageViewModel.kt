package com.emil.linksy.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.MessageData
import com.emil.domain.model.MessageResponse
import com.emil.domain.model.toLocalModel
import com.emil.domain.model.toResponseModelList
import com.emil.domain.usecase.GetUserChatsFromLocalDb
import com.emil.domain.usecase.GetUserMessagesFromLocalDb
import com.emil.domain.usecase.GetUserMessagesUseCase
import com.emil.domain.usecase.InsertMessageInLocalDbUseCase
import com.emil.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class MessageViewModel(private val sendMessageUseCase: SendMessageUseCase,
                       private val getUserMessagesUseCase: GetUserMessagesUseCase,
                       private val insertMessageInLocalDbUseCase: InsertMessageInLocalDbUseCase,
                       private val getUserMessagesFromLocalDb: GetUserMessagesFromLocalDb
) :ViewModel(){

    private val _messageList = MutableLiveData<List<MessageResponse>> ()
    val messageList: LiveData<List<MessageResponse>> = _messageList

    fun sendMessage (token:String, recipientId:Long?,chatId: Long?,text:String, image: MultipartBody.Part?,
                     video: MultipartBody.Part?,
                     audio: MultipartBody.Part?,
                     voice: MultipartBody.Part?, onSuccess: ()->Unit){
        viewModelScope.launch {
            try{
                val response =sendMessageUseCase.execute(token,
                    MessageData(recipientId,chatId,text,image, video, audio, voice)
                )
                if(response.isSuccessful){
                    onSuccess ()
                }
            }catch (e:Exception){
                Log.e("MessageVM",e.toString())
            }
        }
    }

    fun getUserMessages(token: String,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {

        viewModelScope.launch {
            try {
                val response = getUserMessagesUseCase.execute(token)
                if (response.isSuccessful){
                    _messageList.value = response.body()
                    onSuccess ()
                }
            }catch (e:Exception){
                onError ()
            }
        }
    }

    fun insertMessage(messageResponse: MessageResponse,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                insertMessageInLocalDbUseCase.execute(messageResponse.toLocalModel())
            }catch (e:Exception){
                onError ()
            }
        }
    }
    fun getUserMessagesByChat(chatId:Long,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
        val messages = getUserMessagesFromLocalDb.execute(chatId)
            _messageList.value = messages.toResponseModelList()
        }
    }
}