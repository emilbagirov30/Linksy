package com.emil.data.model

import com.emil.domain.model.Token
import com.emil.domain.model.UserProfileData
import com.squareup.moshi.Json

class TokenDto (@Json(name = "accessToken")val accessToken: String,
                @Json(name = "refreshToken")val refreshToken: String,
                @Json(name = "wsToken")val wsToken: String)


fun TokenDto.toDomainModel(): Token {
    return Token(
       accessToken = accessToken, refreshToken = refreshToken,wsToken = wsToken
    )
}