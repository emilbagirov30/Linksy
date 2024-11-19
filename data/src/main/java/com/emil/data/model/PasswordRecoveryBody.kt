package com.emil.data.model

import com.emil.domain.model.PasswordRecoveryData

data class PasswordRecoveryBody (val email:String = "", val code:String = "", val newPassword:String= "")

fun PasswordRecoveryBody.toDomainModel (domainModel: PasswordRecoveryData): PasswordRecoveryBody{
    return PasswordRecoveryBody(email = domainModel.email,code = domainModel.code,newPassword = domainModel.newPassword)
}
