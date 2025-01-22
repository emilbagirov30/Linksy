package com.emil.linksy.presentation.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.ChannelData
import com.emil.domain.model.ChannelManagementResponse
import com.emil.domain.model.ChannelPageDataResponse
import com.emil.domain.model.ChannelPostData
import com.emil.domain.model.ChannelPostResponse
import com.emil.domain.model.ChannelResponse
import com.emil.domain.model.ChannelType
import com.emil.domain.model.CommentData
import com.emil.domain.model.CommentResponse
import com.emil.domain.model.UserResponse
import com.emil.domain.usecase.AcceptUserToChannelUseCase
import com.emil.domain.usecase.AddChannelPostCommentUseCase
import com.emil.domain.usecase.AddScoreUseCase
import com.emil.domain.usecase.CreateChannelPostUseCase
import com.emil.domain.usecase.CreateChannelUseCase
import com.emil.domain.usecase.DeleteChannelCommentUseCase
import com.emil.domain.usecase.DeleteChannelPostUseCase
import com.emil.domain.usecase.DeleteRequestUseCase
import com.emil.domain.usecase.DeleteScoreUseCase
import com.emil.domain.usecase.FindChannelByLinkUseCase
import com.emil.domain.usecase.FindChannelByNameUseCase
import com.emil.domain.usecase.GetChannelManagementDataUseCase
import com.emil.domain.usecase.GetChannelMembersUseCase
import com.emil.domain.usecase.GetChannelPageDataUseCase
import com.emil.domain.usecase.GetChannelPostCommentsUseCase
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
    private val voteUseCase: VoteUseCase,
    private val findChannelByNameUseCase: FindChannelByNameUseCase,
    private val findChannelByLinkUseCase: FindChannelByLinkUseCase, private val getChannelManagementDataUseCase: GetChannelManagementDataUseCase,
    private val addScoreUseCase: AddScoreUseCase,private val deleteScoreUseCase: DeleteScoreUseCase,
                       private val addChannelPostCommentUseCase: AddChannelPostCommentUseCase,
    private val getChannelPostCommentsUseCase: GetChannelPostCommentsUseCase,
    private val deleteChannelCommentUseCase: DeleteChannelCommentUseCase
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

    private val _managementData = MutableLiveData<ChannelManagementResponse> ()
    val managementData: LiveData<ChannelManagementResponse> = _managementData
    private val _commentList = MutableLiveData<List<CommentResponse>> ()
    val commentList: LiveData<List<CommentResponse>> = _commentList



    fun createOrUpdateChannel(token:String, name:String, channelId: Long? = null, link:String, description:String, type:ChannelType, oldAvatarUrl:String?, avatar: MultipartBody.Part?, onSuccess: ()->Unit = {}, onError: ()->Unit = {}){
        viewModelScope.launch {
            try {
                val response = createChannelUseCase.execute(token, ChannelData(name,channelId, link, description, type.name, oldAvatarUrl,avatar))
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


    fun publishPost (token:String,channelId:Long, postText:String,postId:Long?,
                     imageUrl:String?,videoUrl:String?,audioUrl:String?,
                     image: MultipartBody.Part?,
                     video: MultipartBody.Part?,
                     audio: MultipartBody.Part?,
                     pollTitle:String,options:List<String>,onSuccess: ()->Unit ){
        viewModelScope.launch {
            try{
                val response = createChannelPostUseCase.execute(token,
                    ChannelPostData(channelId,postText,postId,imageUrl,videoUrl,audioUrl,image, video, audio, pollTitle, options))
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



    fun deleteRequest (token: String,channelId:Long, onSuccess: ()->Unit = {}, onConflict: ()->Unit, onError: ()->Unit = {}) {
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

    fun getChannelSubscriptionRequest (token: String,channelId:Long, onSuccess: ()->Unit = {}, onConflict: ()->Unit, onError: ()->Unit = {}) {
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


    fun findChannelByLink (prefix:String, onSuccess: ()->Unit = {}, onError: ()->Unit = {}){
        viewModelScope.launch {
            try{
                val response = findChannelByLinkUseCase.execute(prefix.substring(1))
                if (response.isSuccessful){
                    onSuccess()
                    _channelList.value = response.body()
                }
            }catch (e:Exception){
                onError()
            }
        }
    }

    fun findChannelByName (prefix:String, onSuccess: ()->Unit = {}, onError: ()->Unit = {}){
        viewModelScope.launch {
            try{
                val response = findChannelByNameUseCase.execute(prefix)
                if (response.isSuccessful){
                    onSuccess()
                    _channelList.value = response.body()
                }
            }catch (e:Exception){
                onError()
            }
        }
    }


    fun getChannelManagementData(token:String,channelId:Long,onSuccess: ()->Unit = {}, onError: ()->Unit = {}){
        viewModelScope.launch {
            try{
                val response = getChannelManagementDataUseCase.execute(token, channelId)
                if (response.isSuccessful){
                    _managementData.value = response.body()
                    onSuccess()

                }
            }catch (e:Exception){
                onError()
            }
        }

    }



    fun addScore(token:String,postId:Long,score:Int,onSuccess: ()->Unit = {}, onError: ()->Unit = {}){
        viewModelScope.launch {
            try{
                val response = addScoreUseCase.execute(token, postId, score)
                if (response.isSuccessful){
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }
    }
    fun deleteScore(token:String,postId:Long,onSuccess: ()->Unit = {}, onError: ()->Unit = {}){
        viewModelScope.launch {
            try{
                val response = deleteScoreUseCase.execute(token, postId)
                if (response.isSuccessful){
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }

    }


    fun addComment(token:String, commentData: CommentData, onSuccess: ()->Unit, onError: ()->Unit){
        viewModelScope.launch {
            try {
                val response = addChannelPostCommentUseCase.execute(token, commentData)
                if (response.isSuccessful){
                    onSuccess()
                }
            }catch (e:Exception){
                onError ()
            }
        }
    }


    fun getComments(postId:Long,onSuccess: ()->Unit,onError: ()->Unit){
        viewModelScope.launch {
            try {
                val response = getChannelPostCommentsUseCase.execute(postId)
                if (response.isSuccessful){
                    _commentList.value = response.body()
                    onSuccess()
                }
            }catch (e:Exception){
                onError ()
            }
        }
    }



    fun deleteComment(token:String,commentId:Long,onSuccess: ()->Unit,onError: ()->Unit){
        viewModelScope.launch {
            try {
                val response = deleteChannelCommentUseCase.execute(token, commentId)
                if (response.isSuccessful){
                    onSuccess()
                }
            }catch (e:Exception){
                onError ()
            }
        }
    }



    }