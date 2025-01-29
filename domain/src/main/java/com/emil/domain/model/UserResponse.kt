package com.emil.domain.model

data class UserResponse (val id:Long,
                         val avatarUrl:String,
                         val username:String,
                         val link:String?,
                         val online:Boolean,
                         val confirmed:Boolean)
