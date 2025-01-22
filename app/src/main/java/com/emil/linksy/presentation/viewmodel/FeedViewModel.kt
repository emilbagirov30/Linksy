package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.ChannelPostResponse
import com.emil.domain.model.PostResponse
import com.emil.domain.usecase.GetAllChannelsPostsUseCase
import com.emil.domain.usecase.GetAllSubscriptionsPostsUseCase
import kotlinx.coroutines.launch

class FeedViewModel(private val getAllChannelsPostsUseCase: GetAllChannelsPostsUseCase,
                    private val getAllSubscriptionsPostsUseCase: GetAllSubscriptionsPostsUseCase):ViewModel() {

    private val _subscriptionPostList = MutableLiveData<List<PostResponse>> ()
    val subscriptionPostList: LiveData<List<PostResponse>> = _subscriptionPostList

    private val _channelPostList = MutableLiveData<List<ChannelPostResponse>> ()
    val channelPostList: LiveData<List<ChannelPostResponse>> = _channelPostList

    fun getAllSubscriptionsPosts(token: String,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {

        viewModelScope.launch {
            try {
                val response = getAllSubscriptionsPostsUseCase.execute(token)
                if (response.isSuccessful){
                    _subscriptionPostList.value = response.body()
                    onSuccess ()
                }
            }catch (e:Exception){
                onError ()
            }
        }


    }


    fun getAllChannelPosts (token: String, onSuccess: ()->Unit = {}, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getAllChannelsPostsUseCase.execute(token)
                if (response.isSuccessful) {
                    _channelPostList.value = response.body()
                    onSuccess()
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }


}