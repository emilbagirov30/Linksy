package com.emil.domain.model

import okhttp3.MultipartBody

data class PostData (val postId:Long?,
                     val text:String?,
                     val oldImageUrl:String?,
                     val oldVideoUrl:String?,
                     val oldAudioUrl:String?,
                     val oldVoiceIrl:String?,
                     val image:MultipartBody.Part?,
                     val video:MultipartBody.Part?,
                     val audio:MultipartBody.Part?,
                     val voice:MultipartBody.Part?)
