package com.emil.data.model

import com.emil.domain.model.ConfirmCodeParam

data class ConfirmCodeQuery  (val email:String="",val code:String="")

fun ConfirmCodeQuery.toDomainModel (domainModel: ConfirmCodeParam): ConfirmCodeQuery{
    return ConfirmCodeQuery(email = domainModel.email,code = domainModel.code)
}