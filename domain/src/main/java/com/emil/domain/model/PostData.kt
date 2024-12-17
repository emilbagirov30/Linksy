package com.emil.domain.model

import okhttp3.MultipartBody

data class PostData (val text:String?,
                     val image:MultipartBody.Part?,
                     val video:MultipartBody.Part?,
                     val audio:MultipartBody.Part?,
                     val voice:MultipartBody.Part?)
