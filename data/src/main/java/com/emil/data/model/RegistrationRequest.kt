package com.emil.data.model

import com.emil.domain.model.UserRegistrationData

data class RegistrationRequest(
    val username: String = "",
    val email: String= "",
    val password: String = ""
)

fun RegistrationRequest.toDomainModel (domainModel:UserRegistrationData): RegistrationRequest{
    return RegistrationRequest(username = domainModel.username,email = domainModel.email,password = domainModel.password)
}