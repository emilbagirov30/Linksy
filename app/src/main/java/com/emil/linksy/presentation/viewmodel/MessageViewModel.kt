package com.emil.linksy.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.MessageData
import com.emil.domain.model.PostData
import com.emil.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class MessageViewModel(private val sendMessageUseCase: SendMessageUseCase) :ViewModel(){
    fun sendMessage (token:String, recipientId:Long,text:String, image: MultipartBody.Part?,
                     video: MultipartBody.Part?,
                     audio: MultipartBody.Part?,
                     voice: MultipartBody.Part?, onSuccess: ()->Unit){
        viewModelScope.launch {
            try{
                val response =sendMessageUseCase.execute(token,
                    MessageData(recipientId,text,image, video, audio, voice)
                )
                if(response.isSuccessful){
                    onSuccess ()
                }
            }catch (e:Exception){
                Log.e("MessageVM",e.toString())
            }
        }
    }
}