package com.emil.linksy.presentation.ui
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emil.presentation.R


class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val registrationFragment = RegistrationFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, registrationFragment)
            commit()
        }
    }
}