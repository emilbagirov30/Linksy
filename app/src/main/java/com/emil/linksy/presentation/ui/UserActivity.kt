package com.emil.linksy.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emil.linksy.util.replaceFragment
import com.emil.presentation.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        bottomNavigationView = findViewById(R.id.bn_main)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.page_feed -> {

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
                   replaceFragment(ProfileFragment ())
                    true
                }
                else -> false
            }
        }

    }

}