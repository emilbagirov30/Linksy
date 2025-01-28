package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.ChannelPostResponse
import com.emil.domain.model.PostResponse
import com.emil.domain.model.RecommendationResponse
import com.emil.domain.model.UnseenSubscriptionMomentResponse
import com.emil.domain.usecase.GetAllChannelsPostsUseCase
import com.emil.domain.usecase.GetAllSubscriptionsPostsUseCase
import com.emil.domain.usecase.GetRecommendationsUseCase
import com.emil.domain.usecase.GetAllUnseenMomentsUseCase
import kotlinx.coroutines.launch

class FeedViewModel(private val getAllChannelsPostsUseCase: GetAllChannelsPostsUseCase,
                    private val getAllSubscriptionsPostsUseCase: GetAllSubscriptionsPostsUseCase,
                    private val getRecommendationsUseCase: GetRecommendationsUseCase,
                    private val getAllUnseenMomentsUseCase: GetAllUnseenMomentsUseCase):ViewModel() {

    private val _subscriptionPostList = MutableLiveData<List<PostResponse>> ()
    val subscriptionPostList: LiveData<List<PostResponse>> = _subscriptionPostList

    private val _recommendationList = MutableLiveData<List<RecommendationResponse>> ()
    val recommendationList: LiveData<List<RecommendationResponse>> = _recommendationList


    private val _channelPostList = MutableLiveData<List<ChannelPostResponse>> ()
    val channelPostList: LiveData<List<ChannelPostResponse>> = _channelPostList

    private val _unseenMomentList = MutableLiveData<List<UnseenSubscriptionMomentResponse>> ()
    val unseenMomentList: LiveData<List<UnseenSubscriptionMomentResponse>> = _unseenMomentList

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

    fun getRecommendation (token: String,onSuccess: ()->Unit = {}, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getRecommendationsUseCase.execute(token)
                if (response.isSuccessful) {
                    _recommendationList.value = response.body()
                    onSuccess()
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }

    fun getAllUnseenMoments (token: String,onSuccess: ()->Unit = {}, onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getAllUnseenMomentsUseCase.execute(token)
                if (response.isSuccessful) {
                    _unseenMomentList.value = response.body()
                    onSuccess()
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }


}