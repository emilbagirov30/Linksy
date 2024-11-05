package com.emil.data.model

import com.emil.domain.model.PasswordChangeData

data class PasswordChangeBody (val email:String = "",val code:String = "",val newPassword:String= "")

fun PasswordChangeBody.toDomainModel (domainModel: PasswordChangeData): PasswordChangeBody{
    return PasswordChangeBody(email = domainModel.email,code = domainModel.code,newPassword = domainModel.newPassword)
}
