package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.ChannelData
import com.emil.domain.model.GroupData
import com.emil.domain.usecase.CreateChannelUseCase
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ChannelViewModel(private val createChannelUseCase: CreateChannelUseCase):ViewModel() {

    fun createChannel(token:String,name:String, link:String, description:String, type:String,avatar: MultipartBody.Part?, onSuccess: ()->Unit = {}, onError: ()->Unit = {}){
        viewModelScope.launch {
            try {
                val response = createChannelUseCase.execute(token, ChannelData(name, link, description, type, avatar))
                if(response.isSuccessful){
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }
    }
}