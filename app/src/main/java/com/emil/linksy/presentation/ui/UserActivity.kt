package com.emil.linksy.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emil.linksy.app.service.TokenService
import com.emil.linksy.util.replaceFragment
import com.emil.presentation.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserActivity : AppCompatActivity() {
    companion object {
        const val TAG_FEED = "FeedFragment"
        const val TAG_PROFILE = "ProfileFragment"
        const val TAG_PEOPLE = "PeopleFragment"
    }
    private lateinit var bottomNavigationView:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val containerId = R.id.fl_fragment_container_user
        bottomNavigationView = findViewById(R.id.bn_main)
        replaceFragment(containerId,FeedFragment(),TAG_FEED)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.page_feed -> {
                    val currentFragmentTag = supportFragmentManager.findFragmentById(containerId)?.tag
                    if (currentFragmentTag != TAG_FEED) {
                        replaceFragment(containerId, FeedFragment(), TAG_FEED)
                    }
                    true
                }
                R.id.page_channels -> {

                    true
                }
                R.id.page_chats -> {

                    true
                }
                R.id.page_people -> {
                    val currentFragmentTag = supportFragmentManager.findFragmentById(containerId)?.tag
                    if (currentFragmentTag != TAG_PEOPLE) {
                        replaceFragment(containerId, PeopleFragment(), TAG_PEOPLE)
                    }
                    true
                }
                R.id.page_profile -> {
                    val currentFragmentTag = supportFragmentManager.findFragmentById(containerId)?.tag
                    if (currentFragmentTag !=TAG_PROFILE) {
                        replaceFragment(containerId, ProfileFragment(),TAG_PROFILE)
                    }
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