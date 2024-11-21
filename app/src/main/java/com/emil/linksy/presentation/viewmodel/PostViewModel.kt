package com.emil.linksy.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.PostData
import com.emil.domain.model.PostResponse
import com.emil.domain.model.UserProfileData
import com.emil.domain.usecase.GetUserPostsUseCase
import com.emil.domain.usecase.PublishPostUseCase
import kotlinx.coroutines.launch
import retrofit2.Response

class PostViewModel (private val publishPostUseCase: PublishPostUseCase,
                     private val getUserPostsUseCase: GetUserPostsUseCase,

):ViewModel() {
    private val _postList = MutableLiveData<List<PostResponse>> ()
    val postList: LiveData<List<PostResponse>> = _postList


    fun publishPost (token:String,postText:String){
        viewModelScope.launch {
            try{
                publishPostUseCase.execute(token, PostData(postText))
            }catch (e:Exception){
                Log.e("PostVM",e.toString())
            }
        }
    }

    fun getUserPosts(token: String) {

        viewModelScope.launch {
            val response = getUserPostsUseCase.execute(token)
            if (response.isSuccessful){
                _postList.value = response.body()
            }
        }

}}
