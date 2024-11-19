package com.emil.domain.usecase

import com.emil.domain.model.PasswordChangeData
import com.emil.domain.repository.UserRepository
import retrofit2.Response

class ChangePasswordUseCase(private val userRepository: UserRepository){
suspend fun execute (token:String,passwordChangeData: PasswordChangeData):Response<Unit>{
   return userRepository.changePassword(token,passwordChangeData)
}

}
