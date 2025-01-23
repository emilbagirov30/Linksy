package com.emil.linksy.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.emil.linksy.app.service.WebSocketService

class TokenManager(context: Context) {
    private var masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private var sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "TokenData",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )


    fun saveTokens(accessToken: String, refreshToken: String,wsToken:String) {
        val editor = sharedPreferences.edit()
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.putString("REFRESH_TOKEN", refreshToken)
        editor.putString("WS_TOKEN", wsToken)
        editor.apply()
    }

    fun getAccessToken(): String {
        return sharedPreferences.getString("ACCESS_TOKEN", null).toString()
    }

    fun getRefreshToken(): String {
        return sharedPreferences.getString("REFRESH_TOKEN", null).toString()
    }

    fun getWsToken(): String {
        return sharedPreferences.getString("WS_TOKEN", null).toString()
    }

    fun clearTokens() {
        val editor = sharedPreferences.edit()
        editor.remove("ACCESS_TOKEN")
        editor.remove("REFRESH_TOKEN")
        editor.remove("WS_TOKEN")
        editor.apply()
    }
}
