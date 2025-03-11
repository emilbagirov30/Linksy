package com.emil.linksy.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.MomentData
import com.emil.domain.model.MomentResponse
import com.emil.domain.usecase.user.CreateMomentUseCase
import com.emil.domain.usecase.user.DeleteMomentUseCase
import com.emil.domain.usecase.people.GetOutsiderUserMomentsUseCase
import com.emil.domain.usecase.user.GetUserMomentsUseCase
import com.emil.domain.usecase.feed.GetUserUnseenMomentsUseCase
import com.emil.domain.usecase.people.ViewMomentUseCase
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class MomentViewModel(private val createMomentUseCase: CreateMomentUseCase,
                      private val getUserMomentsUseCase: GetUserMomentsUseCase, private val getOutsiderUserMomentsUseCase: GetOutsiderUserMomentsUseCase,
                      private val deleteMomentUseCase: DeleteMomentUseCase, private val viewMomentUseCase: ViewMomentUseCase, private val getUserUnseenMomentsUseCase: GetUserUnseenMomentsUseCase
    ):ViewModel (){
    private val _momentList = MutableLiveData<List<MomentResponse>> ()
    val momentList: LiveData<List<MomentResponse>> = _momentList

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
    fun getUserMoments(token: String,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getUserMomentsUseCase.execute(token)
                if (response.isSuccessful){
                    _momentList.value = response.body()
                    onSuccess ()
                }
            }catch (e:Exception){
                onError ()
            }
        }

    }

    fun getOutsiderUserMoments(id:Long,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getOutsiderUserMomentsUseCase.execute(id)
                if (response.isSuccessful){
                    _momentList.value = response.body()
                    onSuccess ()
                }
            }catch (e:Exception){
                onError ()
            }
        }

    }
    fun deleteMoment(token:String,momentId:Long,onSuccess: ()->Unit,onError: ()->Unit){
        viewModelScope.launch {
            try {
                val response= deleteMomentUseCase.execute(token, momentId)
                if (response.isSuccessful){
                    _momentList.value = _momentList.value?.filter { it.momentId != momentId }
                    onSuccess()
                }
            }catch (e:Exception){
                onError ()
            }
        }
    }

    fun viewMoment(token:String,momentId:Long,onSuccess: ()->Unit = {},onError: ()->Unit = {}){
        viewModelScope.launch {
            try {
                val response= viewMomentUseCase.execute(token, momentId)
                if (response.isSuccessful){
                    onSuccess()
                }
            }catch (e:Exception){
                onError ()
            }
        }
    }



    fun getUnseenUserMoments(token:String,id:Long,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getUserUnseenMomentsUseCase.execute(token,id)
                if (response.isSuccessful){
                    _momentList.value = response.body()
                    onSuccess ()
                }
            }catch (e:Exception){
                onError ()
            }
        }

    }
}