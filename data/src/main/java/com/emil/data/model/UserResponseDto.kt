package com.emil.data.model

import com.emil.domain.model.UserResponse

class UserResponseDto (val id:Long = 0,
                       val avatarUrl:String="",
                       val username:String="",
                       val link:String?=null,
                       val online:Boolean = false,
                       val confirmed:Boolean=false)

fun UserResponseDto.toDomainModel(): UserResponse {
    return UserResponse(id, avatarUrl,username, link,online,confirmed)
}
fun List<UserResponseDto>.toDomainModelList(): List<UserResponse> {
    return this.map { it.toDomainModel() }
}