package com.emil.linksy.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emil.linksy.app.service.TokenService
import com.emil.linksy.util.replaceFragment
import com.emil.presentation.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView:BottomNavigationView
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
        val tokenServiceIntent = Intent(this, TokenService::class.java)
        startService(tokenServiceIntent)
    }
    override fun onDestroy() {
        super.onDestroy()
    }

}