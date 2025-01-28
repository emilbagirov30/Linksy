package com.emil.data.model

import com.emil.domain.model.UserProfileData
import com.squareup.moshi.Json

data class UserProfileDataDto(
    @Json(name = "id") val id: Long,
    @Json(name = "username") val username: String,
    @Json(name = "link") val link: String?,
    @Json(name = "avatarUrl") val avatarUrl: String,
    @Json(name = "confirmed") val confirmed: Boolean

)
fun UserProfileDataDto.toDomainModel(): UserProfileData {
    return UserProfileData(
        id=id,
        username = username,
        link = link,
        avatarUrl = avatarUrl,
        confirmed = confirmed
    )
}