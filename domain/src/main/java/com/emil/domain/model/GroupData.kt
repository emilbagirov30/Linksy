package com.emil.domain.model

import okhttp3.MultipartBody

data class GroupData (val participantIds:String, val name:String,  val avatar: MultipartBody.Part?)