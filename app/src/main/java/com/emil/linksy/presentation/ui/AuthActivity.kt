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
        val containerId = R.id.fl_fragment_container_auth
        val fragment = supportFragmentManager.findFragmentById(containerId)
        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                .add(containerId, RegistrationFragment())
                .commit()
        }
    }
}