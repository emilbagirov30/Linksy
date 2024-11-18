package com.emil.data.model

import com.emil.domain.model.AllUserData
import com.squareup.moshi.Json
import java.util.Date

data class AllUserDataDto(
    @Json(name = "username") val username: String,
    @Json(name = "avatarUrl") val avatarUrl: String,
    @Json(name = "email") val email: String,
    @Json(name = "birthday") val birthday:String?
)
fun AllUserDataDto.toDomainModel(): AllUserData {
    return AllUserData(
        username = this.username,
        avatarUrl = this.avatarUrl,
        email = this.email,
        birthday = this.birthday
    )
}