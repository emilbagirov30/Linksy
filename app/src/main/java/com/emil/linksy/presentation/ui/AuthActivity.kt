package com.emil.linksy.presentation.ui
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emil.presentation.R


class AuthActivity : AppCompatActivity() {
    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, RegistrationFragment())
                .commit()
        }
    }
}