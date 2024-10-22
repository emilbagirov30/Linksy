package com.emil.presentation.ui
import com.emil.presentation.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


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