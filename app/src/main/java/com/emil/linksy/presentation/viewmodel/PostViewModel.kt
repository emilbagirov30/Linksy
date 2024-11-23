package com.emil.linksy.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.PostData
import com.emil.domain.model.PostResponse
import com.emil.domain.model.UserProfileData
import com.emil.domain.usecase.DeletePostUseCase
import com.emil.domain.usecase.GetUserPostsUseCase
import com.emil.domain.usecase.PublishPostUseCase
import kotlinx.coroutines.launch


class PostViewModel (private val publishPostUseCase: PublishPostUseCase,
                     private val getUserPostsUseCase: GetUserPostsUseCase,
                     private val deletePostUseCase: DeletePostUseCase

):ViewModel() {

    private val _postList = MutableLiveData<List<PostResponse>> ()
    val postList: LiveData<List<PostResponse>> = _postList


    fun publishPost (token:String,onSuccess: ()->Unit,postText:String){
        viewModelScope.launch {
            try{
               val response =publishPostUseCase.execute(token, PostData(postText))
                if(response.isSuccessful){
                    onSuccess ()
                }
            }catch (e:Exception){
                Log.e("PostVM",e.toString())
            }
        }
    }

    fun getUserPosts(token: String,onSuccess: ()->Unit,onError: ()->Unit) {

        viewModelScope.launch {
            try {
            val response = getUserPostsUseCase.execute(token)
            if (response.isSuccessful){
                _postList.value = response.body()
                onSuccess ()
            }
        }catch (e:Exception){
            onError ()
        }
        }

}

fun deletePost(token:String,postId:Long,onSuccess: ()->Unit,onError: ()->Unit){
    viewModelScope.launch {
        try {
           val response = deletePostUseCase.execute(token, postId)
            if (response.isSuccessful) onSuccess()
        }catch (e:Exception){
            onError ()
        }
    }
}



}
