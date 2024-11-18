package com.emil.linksy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emil.domain.model.AllUserData
import com.emil.domain.usecase.AllUserDataUseCase
import kotlinx.coroutines.launch

class AllUserDataViewModel (private val allUserDataUseCase: AllUserDataUseCase): ViewModel() {

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
                println(e)
            }
        }

    }
}