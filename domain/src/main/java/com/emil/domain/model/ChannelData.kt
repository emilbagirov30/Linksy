package com.emil.domain.model

import okhttp3.MultipartBody

data class ChannelData (val name:String,val channelId:Long?,val link:String,val description:String,val type:String,
                        val oldAvatarUrl:String?, val avatar: MultipartBody.Part?)