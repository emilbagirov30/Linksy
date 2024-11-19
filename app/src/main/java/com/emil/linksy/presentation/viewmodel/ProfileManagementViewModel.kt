package com.emil.linksy.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.AllUserData
import com.emil.domain.usecase.AllUserDataUseCase
import com.emil.domain.usecase.DeleteAvatarUseCase
import com.emil.domain.usecase.UpdateBirthdayUseCase
import com.emil.domain.usecase.UpdateLinkUseCase
import com.emil.domain.usecase.UpdateUsernameUseCase
import com.emil.domain.usecase.UploadAvatarUseCase
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ProfileManagementViewModel (private val allUserDataUseCase: AllUserDataUseCase,
                                  private val uploadAvatarUseCase: UploadAvatarUseCase,
                                  private val updateBirthdayUseCase: UpdateBirthdayUseCase,
                                  private val updateUsernameUseCase: UpdateUsernameUseCase,
                                  private val updateLinkUseCase: UpdateLinkUseCase,
                                  private val deleteAvatarUseCase: DeleteAvatarUseCase
    ): ViewModel() {

    private val _userData = MutableLiveData<AllUserData>()
    val userData: LiveData<AllUserData> = _userData

    fun getData(
        token: String,
        onIncorrect: () -> Unit,
        onError: () -> Unit
    ) {

        viewModelScope.launch {
            try {
                val response = allUserDataUseCase.execute(token)
                if (response.isSuccessful) {
                    _userData.value = response.body()

                } else {
                    onIncorrect()
                }
            } catch (e: Exception) {
                onError()
            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    fun uploadAvatar(
        token: String,
        avatar: MultipartBody.Part,
        onSuccess: () -> Unit,
        onIncorrect: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
              val response=  uploadAvatarUseCase.execute(token, avatar)
                if (response.isSuccessful) onSuccess()
                else onIncorrect ()
            } catch (e: Exception) {
                onError()
            }
        }


    }

    fun updateBirthday(
        token: String,
        birthday: String,
        onSuccess: () -> Unit,
        onIncorrect: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = updateBirthdayUseCase.execute(token, birthday)
                if (response.isSuccessful) onSuccess()
                else onIncorrect ()
            } catch (e: Exception) {
                onError()
            }
        }
    }
        fun updateUsername(
            token: String,
            username: String,
            onSuccess: () -> Unit,
            onIncorrect: () -> Unit,
            onError: () -> Unit
        ) {
            viewModelScope.launch {
                try {
                   val response = updateUsernameUseCase.execute(token, username)
                    if (response.isSuccessful)onSuccess ()
                    else onIncorrect()
                } catch (e: Exception) {
                    onError()
                }
            }
        }

    fun updateLink(
        token: String,
        link: String,
        onSuccess: () -> Unit,
        onIncorrect: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
               val response = updateLinkUseCase.execute(token, link)
                if (response.isSuccessful) onSuccess()
                if (response.code()==409){
                    onIncorrect ()
                }
            } catch (e: Exception) {
                onError()
            }
        }
    }


    fun deleteAvatar(
        token: String,
        onSuccess: () -> Unit,
        onIncorrect: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response =deleteAvatarUseCase.execute(token)
                if (response.isSuccessful) onSuccess()
                else onIncorrect ()

            } catch (e: Exception) {
                onError()
            }
        }
    }

}



