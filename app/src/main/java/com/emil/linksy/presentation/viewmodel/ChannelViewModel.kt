package com.emil.linksy.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.ChannelData
import com.emil.domain.model.ChannelPageDataResponse
import com.emil.domain.model.ChannelResponse
import com.emil.domain.model.ChatResponse
import com.emil.domain.model.GroupData
import com.emil.domain.model.UserPageDataResponse
import com.emil.domain.usecase.CreateChannelUseCase
import com.emil.domain.usecase.GetChannelPageDataUseCase
import com.emil.domain.usecase.GetChannelsUseCase
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ChannelViewModel(private val createChannelUseCase: CreateChannelUseCase,
    private val getChannelsUseCase: GetChannelsUseCase,
    private val getChannelPageDataUseCase: GetChannelPageDataUseCase
    ):ViewModel() {

    private val _channelList = MutableLiveData<List<ChannelResponse>> ()
    val channelList: LiveData<List<ChannelResponse>> = _channelList

    private val _pageData = MutableLiveData<ChannelPageDataResponse> ()
    val pageData: LiveData<ChannelPageDataResponse> = _pageData

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


    @SuppressLint("SuspiciousIndentation")
    fun getChannelPageData (token: String,id:Long, onSuccess: ()->Unit = {}, onConflict: ()->Unit, onError: ()->Unit = {}){
        viewModelScope.launch {
            try {
                val response = getChannelPageDataUseCase.execute(token,id)
                if (response.isSuccessful){
                    _pageData.value = response.body()
                    onSuccess()
                }else onConflict()

            }catch (e:Exception){
                onError()
            }
        }
    }

}