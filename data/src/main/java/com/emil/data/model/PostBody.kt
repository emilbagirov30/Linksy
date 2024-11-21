package com.emil.data.model

import com.emil.domain.model.PostData

data class PostBody (val text:String = "")


fun PostBody.toDomainModel (domainModel:PostData):PostBody{
    return PostBody (text = domainModel.text)

}