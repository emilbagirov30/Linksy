package com.emil.linksy.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.CommentData
import com.emil.domain.model.CommentResponse
import com.emil.domain.model.PostData
import com.emil.domain.model.PostResponse
import com.emil.domain.model.UserProfileData
import com.emil.domain.usecase.AddCommentUseCase
import com.emil.domain.usecase.AddLikeUseCase
import com.emil.domain.usecase.DeleteLikeUseCase
import com.emil.domain.usecase.DeletePostUseCase
import com.emil.domain.usecase.GetCommentsUseCase
import com.emil.domain.usecase.GetOutsiderUserPostsUseCase
import com.emil.domain.usecase.GetUserPostsUseCase
import com.emil.domain.usecase.PublishPostUseCase
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


class PostViewModel (private val publishPostUseCase: PublishPostUseCase,
                     private val getUserPostsUseCase: GetUserPostsUseCase,
                     private val getOutsiderUserPostsUseCase: GetOutsiderUserPostsUseCase,
                     private val deletePostUseCase: DeletePostUseCase,
                     private val addLikeUseCase: AddLikeUseCase,
    private  val addCommentUseCase: AddCommentUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase,
    private val getCommentsUseCase: GetCommentsUseCase

):ViewModel() {

    private val _postList = MutableLiveData<List<PostResponse>> ()
    val postList: LiveData<List<PostResponse>> = _postList

    private val _commentList = MutableLiveData<List<CommentResponse>> ()
    val commentList: LiveData<List<CommentResponse>> = _commentList

    fun publishPost (token:String, postText:String, image: MultipartBody.Part?,
                     video: MultipartBody.Part?,
                     audio: MultipartBody.Part?,
                     voice: MultipartBody.Part?,onSuccess: ()->Unit ){
        viewModelScope.launch {
            try{
               val response =publishPostUseCase.execute(token, PostData(postText,image, video, audio, voice))
                if(response.isSuccessful){
                    onSuccess ()
                }
            }catch (e:Exception){
                Log.e("PostVM",e.toString())
            }
        }
    }

    fun getUserPosts(token: String,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {

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
    fun getOutsiderUserPosts(token: String,id:Long,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getOutsiderUserPostsUseCase.execute(token,id)
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
            if (response.isSuccessful){
                _postList.value = _postList.value?.filter { it.postId != postId }
                onSuccess()
            }
        }catch (e:Exception){
            onError ()
        }
    }
}


    fun addLike(token:String,postId:Long,onSuccess: ()->Unit,onError: ()->Unit){
        viewModelScope.launch {
            try {
                val response = addLikeUseCase.execute(token, postId)
                if (response.isSuccessful){
                    onSuccess()
                }
            }catch (e:Exception){
                onError ()
            }
        }
    }

    fun deleteLike(token:String,postId:Long,onSuccess: ()->Unit,onError: ()->Unit){
        viewModelScope.launch {
            try {
                val response = deleteLikeUseCase.execute(token, postId)
                if (response.isSuccessful){
                    onSuccess()
                }
            }catch (e:Exception){
                onError ()
            }
        }
    }

    fun addComment(token:String,commentData: CommentData,onSuccess: ()->Unit,onError: ()->Unit){
        viewModelScope.launch {
            try {
                val response = addCommentUseCase.execute(token, commentData)
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
                val response = getCommentsUseCase.execute(postId)
                if (response.isSuccessful){
                     _commentList.value = response.body()
                    onSuccess()
                }
            }catch (e:Exception){
                onError ()
            }
        }
    }

}
