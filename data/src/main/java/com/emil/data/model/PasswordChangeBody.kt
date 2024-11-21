package com.emil.data.model

import com.emil.domain.model.PasswordChangeData

class PasswordChangeBody(val oldPassword:String = "", val newPassword:String = "")

fun PasswordChangeBody.toDomainModel (domainModel: PasswordChangeData): PasswordChangeBody{
    return PasswordChangeBody(newPassword = domainModel.newPassword, oldPassword = domainModel.oldPassword )
}