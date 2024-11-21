package com.emil.linksy.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.PostData
import com.emil.domain.usecase.PublishPostUseCase
import kotlinx.coroutines.launch

class PostViewModel (private val publishPostUseCase: PublishPostUseCase):ViewModel() {



    fun publishPost (token:String,postText:String){
        viewModelScope.launch {
            try{
                publishPostUseCase.execute(token, PostData(postText))
            }catch (e:Exception){
                Log.e("PostVM",e.toString())
            }



        }
    }

}
