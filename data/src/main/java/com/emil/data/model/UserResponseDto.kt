package com.emil.data.model

import com.emil.domain.model.MomentResponse
import com.emil.domain.model.UserResponse

class UserResponseDto (val id:Long = 0,val avatarUrl:String="",val username:String="",val link:String?=null)

fun UserResponseDto.toDomainModel(): UserResponse {
    return UserResponse(id, avatarUrl,username, link)
}
fun List<UserResponseDto>.toDomainModelList(): List<UserResponse> {
    return this.map { it.toDomainModel() }
}