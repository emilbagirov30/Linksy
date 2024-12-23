package com.emil.domain.model

import okhttp3.MultipartBody

data class MomentData (val image:MultipartBody.Part?,val video:MultipartBody.Part?,
                       val audio:MultipartBody.Part?,val text:String?)
