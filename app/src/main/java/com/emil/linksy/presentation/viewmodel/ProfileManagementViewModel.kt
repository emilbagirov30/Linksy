package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.AllUserData
import com.emil.domain.usecase.AllUserDataUseCase
import com.emil.domain.usecase.UpdateBirthdayUseCase
import com.emil.domain.usecase.UploadAvatarUseCase
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ProfileManagementViewModel (private val allUserDataUseCase: AllUserDataUseCase,
                                  private val uploadAvatarUseCase: UploadAvatarUseCase,
                                  private val updateBirthdayUseCase: UpdateBirthdayUseCase
    ): ViewModel() {

    private val _userData = MutableLiveData<AllUserData> ()
    val userData: LiveData<AllUserData> = _userData

    fun getData(
        token: String,
        onIncorrect: () -> Unit,
        onError: () -> Unit
    ) {

        viewModelScope.launch {
            try {
                val response = allUserDataUseCase.execute(token)
                if (response.isSuccessful){
                    _userData.value = response.body()

                }else {
                    onIncorrect ()
                }
            }catch (e:Exception){
                onError ()
            }
        }

    }

    fun uploadAvatar (token:String,avatar:MultipartBody.Part, onIncorrect: () -> Unit, onError: () -> Unit){
        viewModelScope.launch {
            try{
               uploadAvatarUseCase.execute(token, avatar)
            }catch (e:Exception){
                onError ()
            }
        }


    }

    fun updateBirthday (token:String,birthday:String, onIncorrect: () -> Unit, onError: () -> Unit){
        viewModelScope.launch {
            try{
              updateBirthdayUseCase.execute(token,birthday)
            }catch (e:Exception){
                onError ()
            }
        }


    }

}