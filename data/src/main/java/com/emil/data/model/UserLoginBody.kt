package com.emil.data.model


import com.emil.domain.model.UserLoginData


data class UserLoginBody (val email: String= "", val password: String = "")


fun UserLoginBody.toDomainModel (domainModel: UserLoginData): UserLoginBody {
    return UserLoginBody(email = domainModel.email, password = domainModel.password)
}
