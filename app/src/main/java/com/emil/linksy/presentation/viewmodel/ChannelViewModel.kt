package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.ChannelData
import com.emil.domain.model.ChannelResponse
import com.emil.domain.model.ChatResponse
import com.emil.domain.model.GroupData
import com.emil.domain.usecase.CreateChannelUseCase
import com.emil.domain.usecase.GetChannelsUseCase
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ChannelViewModel(private val createChannelUseCase: CreateChannelUseCase,
    private val getChannelsUseCase: GetChannelsUseCase):ViewModel() {

    private val _channelList = MutableLiveData<List<ChannelResponse>> ()
    val channelList: LiveData<List<ChannelResponse>> = _channelList



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
    fun getChannels(token:String, onSuccess: ()->Unit = {}, onError: ()->Unit = {}){
        viewModelScope.launch {
            try {
                val response = getChannelsUseCase.execute(token)
                if(response.isSuccessful){
                    onSuccess()
                    _channelList.value = response.body()
                }
            }catch (e:Exception){
                onError()
            }
        }
    }
}