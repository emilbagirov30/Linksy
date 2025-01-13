package com.emil.linksy.presentation.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.ChannelData
import com.emil.domain.model.ChannelPageDataResponse
import com.emil.domain.model.ChannelPostData
import com.emil.domain.model.ChannelPostResponse
import com.emil.domain.model.ChannelResponse
import com.emil.domain.model.ChatResponse
import com.emil.domain.model.GroupData
import com.emil.domain.model.PostData
import com.emil.domain.model.UserPageDataResponse
import com.emil.domain.model.UserResponse
import com.emil.domain.usecase.AcceptUserToChannelUseCase
import com.emil.domain.usecase.CreateChannelPostUseCase
import com.emil.domain.usecase.CreateChannelUseCase
import com.emil.domain.usecase.DeleteChannelPostUseCase
import com.emil.domain.usecase.DeleteRequestUseCase
import com.emil.domain.usecase.GetChannelMembersUseCase
import com.emil.domain.usecase.GetChannelPageDataUseCase
import com.emil.domain.usecase.GetChannelPostsUseCase
import com.emil.domain.usecase.GetChannelSubscriptionsRequestUseCse
import com.emil.domain.usecase.GetChannelsUseCase
import com.emil.domain.usecase.RejectSubscriptionRequestUseCase
import com.emil.domain.usecase.SubmitRequestUseCase
import com.emil.domain.usecase.SubscribeChannelUseCase
import com.emil.domain.usecase.UnsubscribeChannelUseCase
import com.emil.domain.usecase.VoteUseCase
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ChannelViewModel(private val createChannelUseCase: CreateChannelUseCase,
    private val getChannelsUseCase: GetChannelsUseCase,
    private val getChannelPageDataUseCase: GetChannelPageDataUseCase,
    private val createChannelPostUseCase: CreateChannelPostUseCase,
    private val acceptUserToChannelUseCase: AcceptUserToChannelUseCase,
    private val deleteChannelPostUseCase: DeleteChannelPostUseCase,
    private val deleteRequestUseCase: DeleteRequestUseCase,
    private val getChannelMembersUseCase: GetChannelMembersUseCase,
    private val getChannelPostsUseCase: GetChannelPostsUseCase,
    private val getChannelSubscriptionsRequestUseCse: GetChannelSubscriptionsRequestUseCse,
    private val rejectSubscriptionRequestUseCase: RejectSubscriptionRequestUseCase,
    private val submitRequestUseCase: SubmitRequestUseCase,
    private val subscribeChannelUseCase: SubscribeChannelUseCase,
    private val unsubscribeChannelUseCase: UnsubscribeChannelUseCase,
    private val voteUseCase: VoteUseCase
    ):ViewModel() {

    private val _channelList = MutableLiveData<List<ChannelResponse>> ()
    val channelList: LiveData<List<ChannelResponse>> = _channelList

    private val _memberList = MutableLiveData<List<UserResponse>> ()
    val memberList: LiveData<List<UserResponse>> = _memberList

    private val _postList = MutableLiveData<List<ChannelPostResponse>> ()
    val postList: LiveData<List<ChannelPostResponse>> = _postList

    private val _subscriptionsRequestList = MutableLiveData<List<UserResponse>> ()
    val subscriptionsRequestList: LiveData<List<UserResponse>> = _subscriptionsRequestList

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


    fun publishPost (token:String,channelId:Long, postText:String, image: MultipartBody.Part?,
                     video: MultipartBody.Part?,
                     audio: MultipartBody.Part?,
                     pollTitle:String,options:List<String>,onSuccess: ()->Unit ){
        viewModelScope.launch {
            try{
                val response = createChannelPostUseCase.execute(token, ChannelPostData(channelId,postText,image, video, audio, pollTitle, options))
                if(response.isSuccessful){
                    onSuccess ()
                }
            }catch (e:Exception){
                Log.e("ChannelVM",e.toString())
            }
        }
    }


    fun acceptUserToChannel (token: String,channelId:Long, candidateId:Long,onSuccess: ()->Unit = {}, onConflict: ()->Unit, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = acceptUserToChannelUseCase.execute(token, channelId, candidateId)
                if (response.isSuccessful) {
                    onSuccess()
                }
            } catch (e: Exception) {
                onError()
            }
        }}


        fun rejectSubscriptionRequest (token: String,channelId:Long, candidateId:Long,onSuccess: ()->Unit = {}, onConflict: ()->Unit, onError: ()->Unit = {}) {
            viewModelScope.launch {
                try {
                    val response = rejectSubscriptionRequestUseCase.execute(token, channelId, candidateId)
                    if (response.isSuccessful) {
                        onSuccess()
                    }
                } catch (e: Exception) {
                    onError()
                }
            }
        }



    fun deleteChannelPost (token: String,channelId:Long, onSuccess: ()->Unit = {}, onConflict: ()->Unit, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = deleteChannelPostUseCase.execute(token, channelId)
                if (response.isSuccessful) {
                    onSuccess()
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }



    fun deleteRequestUseCase (token: String,channelId:Long, onSuccess: ()->Unit = {}, onConflict: ()->Unit, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = deleteRequestUseCase.execute(token, channelId)
                if (response.isSuccessful) {
                    onSuccess()
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }

    fun submitRequest (token: String,channelId:Long, onSuccess: ()->Unit = {}, onConflict: ()->Unit, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = submitRequestUseCase.execute(token, channelId)
                if (response.isSuccessful) {
                    onSuccess()
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }



    fun getChannelMembers (token: String,channelId:Long, onSuccess: ()->Unit = {}, onConflict: ()->Unit, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getChannelMembersUseCase.execute(token, channelId)
                if (response.isSuccessful) {
                    _memberList.value = response.body()
                    onSuccess()
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }

    fun getChannelSubscription (token: String,channelId:Long, onSuccess: ()->Unit = {}, onConflict: ()->Unit, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getChannelSubscriptionsRequestUseCse.execute(token, channelId)
                if (response.isSuccessful) {
                    _subscriptionsRequestList.value = response.body()
                    onSuccess()
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }

    fun getChannelPosts (token: String,channelId:Long, onSuccess: ()->Unit = {}, onConflict: ()->Unit, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getChannelPostsUseCase.execute(token, channelId)
                if (response.isSuccessful) {
                    _postList.value = response.body()
                    onSuccess()
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }
    fun subscribe (token:String,id:Long, onSuccess: ()->Unit = {}, onError: ()->Unit = {}){
        viewModelScope.launch {
            try{
                val response = subscribeChannelUseCase.execute(token, id)
                if (response.isSuccessful){
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }
    }
    fun unsubscribe (token:String,id:Long, onSuccess: ()->Unit = {}, onError: ()->Unit = {}){
        viewModelScope.launch {
            try{
                val response = unsubscribeChannelUseCase.execute(token, id)
                if (response.isSuccessful){
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }
    }


    fun vote (token:String,id:Long, onSuccess: ()->Unit = {}, onError: ()->Unit = {}){
        viewModelScope.launch {
            try{
                val response = voteUseCase.execute(token, id)
                if (response.isSuccessful){
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }
    }
    }