package com.emil.linksy.presentation.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.MessageData
import com.emil.domain.model.MessageResponse
import com.emil.domain.model.MessageStatus
import com.emil.domain.model.Status
import com.emil.domain.model.StatusResponse
import com.emil.domain.model.toLocalModel
import com.emil.domain.model.toResponseModelList
import com.emil.domain.usecase.ConnectToWebSocketUseCase
import com.emil.domain.usecase.DeleteMessageUseCase
import com.emil.domain.usecase.DisconnectFromWebSocketUseCase
import com.emil.domain.usecase.EditMessageUseCase
import com.emil.domain.usecase.GetUserMessagesByChat
import com.emil.domain.usecase.GetUserMessagesByChatFromLocalDb
import com.emil.domain.usecase.GetUserMessagesUseCase
import com.emil.domain.usecase.InsertMessageInLocalDbUseCase
import com.emil.domain.usecase.SendMessageUseCase
import com.emil.domain.usecase.SendStatusUseCase
import com.emil.domain.usecase.SubscribeToChatStatusUseCase
import com.emil.domain.usecase.SubscribeToEditMessagesUseCase
import com.emil.domain.usecase.SubscribeToMessagesDeleteUseCase
import com.emil.domain.usecase.SubscribeToUserChatViewedUseCase
import com.emil.domain.usecase.SubscribeToUserMessagesUseCase
import com.emil.domain.usecase.ViewedUseCase
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class MessageViewModel(private val sendMessageUseCase: SendMessageUseCase,
                       private val getUserMessagesUseCase: GetUserMessagesUseCase,
                       private val insertMessageInLocalDbUseCase: InsertMessageInLocalDbUseCase,
                       private val getUserMessagesByChatFromLocalDb: GetUserMessagesByChatFromLocalDb,
                       private val subscribeToUserMessagesUseCase: SubscribeToUserMessagesUseCase,
                       private val connectToWebSocketUseCase: ConnectToWebSocketUseCase,
                       private val disconnectFromWebSocketUseCase: DisconnectFromWebSocketUseCase,
                       private val getUserMessagesByChat: GetUserMessagesByChat,
                       private val viewedUseCase: ViewedUseCase,
                       private val subscribeToUserChatViewedUseCase: SubscribeToUserChatViewedUseCase,
                        private val subscribeToDeleteMessageUseCase: SubscribeToMessagesDeleteUseCase,
                         private val deleteMessageUseCase: DeleteMessageUseCase,
                         private val editMessageUseCase: EditMessageUseCase,
                          private val subscribeToEditMessagesUseCase: SubscribeToEditMessagesUseCase,
    private val sendStatusUseCase: SendStatusUseCase,
    private val subscribeToChatStatusUseCase: SubscribeToChatStatusUseCase
) :ViewModel(){

    private val _messageList = MutableLiveData<MutableList<MessageResponse>> ()
    val messageList: LiveData<MutableList<MessageResponse>> = _messageList

    private val _status = MutableLiveData<StatusResponse> ()
    val status: LiveData<StatusResponse> = _status




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

    fun getAllUserMessages(token: String, onSuccess: ()->Unit = {}, onError: ()->Unit = {}) {

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

    @SuppressLint("SuspiciousIndentation")
    fun subscribeToUserMessages(token: String, chatId: Long, onSuccess: ()->Unit = {}, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                connectToWebSocketUseCase.invoke()
              val message = subscribeToUserMessagesUseCase.invoke(token,chatId)
                message.collect { message ->
                    insertMessageInLocalDbUseCase.execute(message.toLocalModel())
                    val updatedList = _messageList.value ?: mutableListOf()
                    updatedList.add(message)
                    _messageList.postValue(updatedList)
                }
            }catch (e:Exception){
                onError ()
            }
        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun subscribeToViewed(token: String,chatId: Long, onSuccess: ()->Unit = {}, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                connectToWebSocketUseCase.invoke()
                val messageId= subscribeToUserChatViewedUseCase.invoke(token,chatId)
                messageId.collect { id ->
                    val updatedList = _messageList.value?.toMutableList() ?: mutableListOf()
                    val index = updatedList.indexOfFirst { it.messageId == id }
                    if (index != -1) {
                        val updatedMessage = updatedList[index].copy(viewed = true)
                        updatedList[index] = updatedMessage
                        _messageList.value = updatedList
                    }
                }
            }catch (e:Exception){
                onError ()
            }
        }
    }


    fun getUserMessagesByChatFromLocalDb(chatId:Long, onSuccess: ()->Unit = {}, onError: ()->Unit = {}) {
        viewModelScope.launch {
        val messages = getUserMessagesByChatFromLocalDb.execute(chatId)
            _messageList.value = messages.toResponseModelList()
        }
    }



    @SuppressLint("SuspiciousIndentation")
    fun getUserMessagesByChat(token: String, chatId:Long, onSuccess: ()->Unit = {}, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getUserMessagesByChat.execute(token, chatId)
                if (response.isSuccessful)
                _messageList.value = response.body()
                onSuccess()
            }catch (e:Exception){
                onError()
            }
        }
    }



    @SuppressLint("SuspiciousIndentation")
    fun viewed(token: String, chatId:Long, onSuccess: ()->Unit = {}, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = viewedUseCase.execute(token, chatId)
                if (response.isSuccessful)
                onSuccess()
            }catch (e:Exception){
                onError()
            }
        }
    }


    fun disconnectFromWebSocket() {
        viewModelScope.launch {
          disconnectFromWebSocketUseCase.invoke()
        }
    }


    fun deleteMessage(token: String, messageId:Long, onSuccess: ()->Unit = {}, onError: ()->Unit = {}) {

        viewModelScope.launch {
            try {
                val response = deleteMessageUseCase.execute(token, messageId)
                if (response.isSuccessful){
                    onSuccess ()
                }
            }catch (e:Exception){
                onError ()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun subscribeToDeleted(token: String,chatId: Long, onSuccess: ()->Unit = {}, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                connectToWebSocketUseCase.invoke()
                val messageId= subscribeToDeleteMessageUseCase.invoke(token, chatId)
                messageId.collect { id ->
                    val updatedList = _messageList.value?.toMutableList() ?: mutableListOf()
                    val index = updatedList.indexOfFirst { it.messageId == id }
                    if (index != -1) {
                        updatedList.removeAt(index)
                        _messageList.value = updatedList
                    }
                }
            }catch (e:Exception){
                onError ()
            }
        }
    }
    fun editMessage(token: String, messageId:Long,text:String, onSuccess: ()->Unit = {}, onError: ()->Unit = {}) {

        viewModelScope.launch {
            try {
                val response = editMessageUseCase.execute(token, messageId, text)
                if (response.isSuccessful){
                    onSuccess ()
                }
            }catch (e:Exception){
                onError ()
            }
        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun subscribeToEdited(token: String,chatId: Long, onSuccess: ()->Unit = {}, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                connectToWebSocketUseCase.invoke()
                val messageId= subscribeToEditMessagesUseCase.invoke(token, chatId)
                messageId.collect { response ->
                    val updatedList = _messageList.value?.toMutableList() ?: mutableListOf()
                    val index = updatedList.indexOfFirst { it.messageId == response.id }
                    if (index != -1) {
                        updatedList[index].text = response.text
                        updatedList[index].edited = true
                        _messageList.value = updatedList
                    }
                }
            }catch (e:Exception){
                onError ()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun subscribeToStatus(token: String,chatId: Long, onSuccess: ()->Unit = {}, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                connectToWebSocketUseCase.invoke()
                val response= subscribeToChatStatusUseCase.invoke(token, chatId)
                response.collect { status ->
                   _status.value = status

                }
            }catch (e:Exception){
                onError ()
            }
        }
    }


    @SuppressLint("SuspiciousIndentation")
    fun sendStatus( chatId: Long, userId:Long,status: MessageStatus, onSuccess: ()->Unit = {}, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                connectToWebSocketUseCase.invoke()
               sendStatusUseCase.invoke(Status(chatId,userId,status))
            }catch (e:Exception){
                onError ()
            }
        }
    }


}