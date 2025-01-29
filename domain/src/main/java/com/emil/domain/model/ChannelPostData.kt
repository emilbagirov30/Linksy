package com.emil.domain.model

import okhttp3.MultipartBody

class ChannelPostData(
    val channelId:Long,
    val text:String?,
    val postId: Long?,
    val imageUrl: String?,
    val videoUrl: String?,
    val audioUrl: String?,
    val image: MultipartBody.Part?,
    val video: MultipartBody.Part?,
    val audio: MultipartBody.Part?,
    val pollTitle: String?,
    val options: List<String>)