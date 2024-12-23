package com.emil.linksy.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.MomentData
import com.emil.domain.model.PostData
import com.emil.domain.usecase.CreateMomentUseCase
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class MomentViewModel(private val createMomentUseCase: CreateMomentUseCase):ViewModel (){


    fun createMoment (token:String, image: MultipartBody.Part?,
                     video: MultipartBody.Part?,
                     audio: MultipartBody.Part?,
                      text:String?, onSuccess: ()->Unit ){
        viewModelScope.launch {
            try{
                val response = createMomentUseCase.execute(token, MomentData(image, video, audio,text))
                if(response.isSuccessful){
                    onSuccess ()
                }
            }catch (e:Exception){
                Log.e("MomentVM",e.toString())
            }
        }
    }


}