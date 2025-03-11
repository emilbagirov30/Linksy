package com.emil.linksy.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenManager(context: Context) {

    companion object {
        private const val FILE_NAME = "TokenData"
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val WS_TOKEN = "WS_TOKEN"
        const val DEFAULT_TOKEN = "null"
    }


    private var masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private var sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(context,
        FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )


    fun saveTokens(accessToken: String, refreshToken: String,wsToken:String) {
        val editor = sharedPreferences.edit()
        editor.putString(ACCESS_TOKEN, accessToken)
        editor.putString(REFRESH_TOKEN, refreshToken)
        editor.putString(WS_TOKEN, wsToken)
        editor.apply()
    }

    fun getAccessToken(): String {
        return sharedPreferences.getString(ACCESS_TOKEN, DEFAULT_TOKEN).toString()
    }

    fun getRefreshToken(): String {
        return sharedPreferences.getString(REFRESH_TOKEN, DEFAULT_TOKEN).toString()
    }

    fun getWsToken(): String {
        return sharedPreferences.getString(WS_TOKEN, DEFAULT_TOKEN).toString()
    }

    fun clearTokens() {
        val editor = sharedPreferences.edit()
        editor.remove(ACCESS_TOKEN)
        editor.remove(REFRESH_TOKEN)
        editor.remove(WS_TOKEN)
        editor.apply()
    }
}
