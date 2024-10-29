package com.emil.data.model

import com.emil.domain.model.UserRegistrationData

data class RegistrationBody(
    val username: String = "",
    val email: String= "",
    val password: String = ""
)

fun RegistrationBody.toDomainModel (domainModel:UserRegistrationData): RegistrationBody{
    return RegistrationBody(username = domainModel.username,email = domainModel.email,password = domainModel.password)
}