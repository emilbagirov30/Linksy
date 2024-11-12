package com.emil.data.model

import com.emil.domain.model.Token
import com.emil.domain.model.UserProfileData
import com.squareup.moshi.Json

class TokenDto (@Json(name = "accessToken")val accessToken: String,
                @Json(name = "refreshToken")val refreshToken: String)


fun TokenDto.toDomainModel(): Token {
    return Token(
       accessToken = this.accessToken, refreshToken = this.refreshToken
    )
}