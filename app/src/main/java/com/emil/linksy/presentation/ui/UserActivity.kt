package com.emil.linksy.presentation.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emil.linksy.presentation.viewmodel.LoginViewModel
import com.emil.linksy.presentation.viewmodel.TokenViewModel
import com.emil.linksy.util.replaceFragment
import com.emil.linksy.util.showToast
import com.emil.presentation.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView:BottomNavigationView
    private val tokenViewModel: TokenViewModel by viewModel<TokenViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val containerId = R.id.fl_fragment_container_user
        bottomNavigationView = findViewById(R.id.bn_main)
        replaceFragment(containerId,FeedFragment())
        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.page_feed -> {
                  replaceFragment(containerId,FeedFragment())
                    true
                }
                R.id.page_channels -> {

                    true
                }
                R.id.page_chats -> {

                    true
                }
                R.id.page_friends -> {
                    true
                }
                R.id.page_profile -> {
                   replaceFragment(containerId,ProfileFragment ())
                    true
                }
                else -> false
            }
        }
        tokenViewModel.startRefreshing(
            onIncorrect = {
                logoutUser()
            },
            onError = {
                showToast(this@UserActivity, R.string.failed_connection)
            }
        )
    }
    override fun onDestroy() {
        super.onDestroy()
        tokenViewModel.stopRefreshing()
    }
    private fun logoutUser() {
        val sharedPref: SharedPreferences = getSharedPreferences("appData", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("remember", false)
        editor.apply()
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}