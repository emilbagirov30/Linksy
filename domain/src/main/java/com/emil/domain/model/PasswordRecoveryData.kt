package com.emil.domain.model

data class PasswordRecoveryData (val email:String, val code:String, val newPassword:String)