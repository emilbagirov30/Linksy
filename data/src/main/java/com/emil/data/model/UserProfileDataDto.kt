package com.emil.data.model

import com.emil.domain.model.UserProfileData
import com.squareup.moshi.Json

data class UserProfileDataDto(
    @Json(name = "username") val username: String,
    @Json(name = "link") val link: String?,
    @Json(name = "avatarUrl") val avatarUrl: String
)
fun UserProfileDataDto.toDomainModel(): UserProfileData {
    return UserProfileData(
        username = this.username,
        link = this.link,
        avatarUrl = this.avatarUrl
    )
}