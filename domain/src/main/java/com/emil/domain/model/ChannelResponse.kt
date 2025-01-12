package com.emil.domain.model

data class ChannelResponse (val channelId:Long,val ownerId:Long,val name:String,val link:String?,val avatarUrl:String,val rating:Double,
                            val type:String)