package com.emil.linksy.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.ReportRequest
import com.emil.domain.model.UserPageDataResponse
import com.emil.domain.model.UserResponse
import com.emil.domain.usecase.AddToBlackListUseCase
import com.emil.domain.usecase.FindUsersByLinkUseCase
import com.emil.domain.usecase.FindUsersByUsernameUseCase
import com.emil.domain.usecase.GetOutsiderUserSubscriptionsUseCase
import com.emil.domain.usecase.GetOutsiderUserSubscribersUseCase
import com.emil.domain.usecase.GetUserPageDataUseCase
import com.emil.domain.usecase.GetUserSubscribersUseCase
import com.emil.domain.usecase.GetUserSubscriptionsUseCase
import com.emil.domain.usecase.RemoveFromBlackListUseCase
import com.emil.domain.usecase.SendReportUseCase
import com.emil.domain.usecase.SubscribeUseCase
import com.emil.domain.usecase.UnsubscribeUseCase
import kotlinx.coroutines.launch

class PeopleViewModel(private val findUsersByUsernameUseCase: FindUsersByUsernameUseCase,
                      private val findUsersByLinkUseCase: FindUsersByLinkUseCase,
                      private val getUserPageDataUseCase: GetUserPageDataUseCase,
                      private val subscribeUseCase: SubscribeUseCase,
                      private val unsubscribeUseCase: UnsubscribeUseCase,
                      private val getUserSubscriptionsUseCase: GetUserSubscriptionsUseCase,
                      private val getUserSubscribersUseCase: GetUserSubscribersUseCase,
                      private val getOutsiderUserSubscriptionsUseCase: GetOutsiderUserSubscriptionsUseCase,
                      private val getOutsiderUserSubscribersUseCase: GetOutsiderUserSubscribersUseCase,
                      private val addToBlackListUseCase: AddToBlackListUseCase,
                      private val removeFromBlackListUseCase: RemoveFromBlackListUseCase,
                      private val sendReportUseCase: SendReportUseCase
    ):ViewModel() {

    private val _userList = MutableLiveData<List<UserResponse>> ()
    val userList: LiveData<List<UserResponse>> = _userList

    private val _pageData = MutableLiveData<UserPageDataResponse> ()
    val pageData: LiveData<UserPageDataResponse> = _pageData



    fun findByUsername(token: String, startsWith: String,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
            val response = findUsersByUsernameUseCase.execute(token, startsWith)
            if (response.isSuccessful){
                _userList.value = response.body()
                onSuccess()
            }
            }catch (e:Exception){
                onError()
            }
        }

    }

    fun findByLink(token: String, startsWith: String,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = findUsersByLinkUseCase.execute(token, startsWith.substring(1))
                if (response.isSuccessful){
                    _userList.value = response.body()
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }

    }
    @SuppressLint("SuspiciousIndentation")
    fun getUserPageData (token: String,id:Long, onSuccess: ()->Unit = {}, onConflict: ()->Unit,  noAccess: ()->Unit,
                         onError: ()->Unit = {},onBlocked: ()->Unit = {}){
        viewModelScope.launch {
            try {
                val response = getUserPageDataUseCase.execute(token,id)
                if (response.isSuccessful){
                  _pageData.value = response.body()
                    onSuccess()
                }else if (response.code() == 404) onConflict()
                     else if (response.code()==403) noAccess ()
                else if (response.code() == 409) onBlocked()
            }catch (e:Exception){
                onError()
            }
        }
    }


    fun subscribe (token:String,id:Long, onSuccess: ()->Unit = {}, onError: ()->Unit = {}){
        viewModelScope.launch {
            try{
                val response = subscribeUseCase.execute(token, id)
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
                val response = unsubscribeUseCase.execute(token, id)
                if (response.isSuccessful){
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }
    }

    fun getUserSubscribers(token: String,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getUserSubscribersUseCase.execute(token)
                if (response.isSuccessful){
                    _userList.value = response.body()
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }

    }

    fun getUserSubscriptions(token: String,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getUserSubscriptionsUseCase.execute(token)
                if (response.isSuccessful){
                    _userList.value = response.body()
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }

    }

    fun getOutsiderUserSubscribers(id:Long,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response =getOutsiderUserSubscribersUseCase.execute(id)
                if (response.isSuccessful){
                    _userList.value = response.body()
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }

    }

    fun getOutsiderUserSubscriptions(id:Long,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = getOutsiderUserSubscriptionsUseCase.execute(id)
                if (response.isSuccessful){
                    _userList.value = response.body()
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }

    }
    fun addToBlackList(token:String,id:Long,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = addToBlackListUseCase.execute(token,id)
                if (response.isSuccessful){
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }

    }
    fun removeBlackList(token:String,id:Long,onSuccess: ()->Unit = {},onError: ()->Unit = {}) {
        viewModelScope.launch {
            try {
                val response = removeFromBlackListUseCase.execute(token,id)
                if (response.isSuccessful){
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }

    }

    fun sendReport(token:String,reportRequest: ReportRequest,onSuccess: ()->Unit = {},onError: ()->Unit = {}){
        viewModelScope.launch {
            try {
                val response = sendReportUseCase.execute(token,reportRequest)
                if (response.isSuccessful){
                    onSuccess()
                }
            }catch (e:Exception){
                onError()
            }
        }
    }

}