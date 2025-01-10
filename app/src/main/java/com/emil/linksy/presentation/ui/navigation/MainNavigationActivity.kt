package com.emil.linksy.presentation.ui.navigation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emil.linksy.app.service.TokenService
import com.emil.linksy.presentation.ui.navigation.chat.ChatFragment
import com.emil.linksy.presentation.ui.navigation.feed.FeedFragment
import com.emil.linksy.presentation.ui.navigation.people.PeopleFragment
import com.emil.linksy.presentation.ui.navigation.profile.ProfileFragment
import com.emil.linksy.presentation.viewmodel.ChatViewModel
import com.emil.linksy.presentation.viewmodel.MessageViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.replaceFragment
import com.emil.presentation.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainNavigationActivity : AppCompatActivity() {
    companion object {
        const val TAG_FEED = "FeedFragment"
        const val TAG_PROFILE = "ProfileFragment"
        const val TAG_PEOPLE = "PeopleFragment"
        const val TAG_CHAT = "ChatFragment"
    }
    private lateinit var bottomNavigationView:BottomNavigationView
    private val messageViewModel: MessageViewModel by viewModel<MessageViewModel>()
    private val tokenManager: TokenManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigation)
        val containerId = R.id.fl_fragment_container_user
        bottomNavigationView = findViewById(R.id.bn_main)
        replaceFragment(containerId, FeedFragment(), TAG_FEED)
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
                    val currentFragmentTag = supportFragmentManager.findFragmentById(containerId)?.tag
                    if (currentFragmentTag != TAG_CHAT) {
                        replaceFragment(containerId,ChatFragment(), TAG_CHAT)
                    }
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
                    if (currentFragmentTag != TAG_PROFILE) {
                        replaceFragment(containerId, ProfileFragment(), TAG_PROFILE)
                    }
                    true
                }
                else -> false
            }
        }
        val tokenServiceIntent = Intent(this, TokenService::class.java)
        startService(tokenServiceIntent)

messageViewModel.messageList.observe(this){messagelist ->
    messagelist.map { m->
        messageViewModel.insertMessage(m)
    }
}
        messageViewModel.getUserMessages(tokenManager.getAccessToken())
    }
    override fun onDestroy() {
        super.onDestroy()
    }



}