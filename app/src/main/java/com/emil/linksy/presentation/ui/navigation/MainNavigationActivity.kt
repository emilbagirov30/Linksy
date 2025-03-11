package com.emil.linksy.presentation.ui.navigation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.emil.linksy.presentation.ui.navigation.channel.ChannelFragment
import com.emil.linksy.presentation.ui.navigation.chat.ChatFragment
import com.emil.linksy.presentation.ui.navigation.feed.FeedFragment
import com.emil.linksy.presentation.ui.navigation.people.PeopleFragment
import com.emil.linksy.presentation.ui.navigation.profile.ProfileFragment
import com.emil.linksy.presentation.viewmodel.ChatViewModel
import com.emil.linksy.presentation.viewmodel.MessageViewModel
import com.emil.linksy.util.Linksy
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
        const val TAG_CHANNEL = "ChannelFragment"
    }
    private var chatBadgeCount = 0
    private lateinit var bottomNavigationView:BottomNavigationView
    private val messageViewModel: MessageViewModel by viewModel<MessageViewModel>()
    private val chatViewModel: ChatViewModel by viewModel<ChatViewModel>()
    private val tokenManager: TokenManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigation)
        val sharedPref = getSharedPreferences(Linksy.SHAREDPREF_MAIN_KEY, Context.MODE_PRIVATE)
        val theme = sharedPref.getString(Linksy.SHAREDPREF_THEME_KEY,null)
        val userId = sharedPref.getLong(Linksy.SHAREDPREF_ID_KEY,-1)
        if (theme==null)
            sharedPref.edit().putString(Linksy.SHAREDPREF_THEME_KEY,Linksy.SHAREDPREF_THEME_VALUE_LIGHT).apply()
        else {
            if(theme == Linksy.SHAREDPREF_THEME_VALUE_LIGHT)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        val containerId = R.id.fl_fragment_container_user
        bottomNavigationView = findViewById(R.id.bn_main)
        replaceFragment(containerId, ProfileFragment(), TAG_PROFILE)
        bottomNavigationView.selectedItemId = R.id.page_profile
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
                    val currentFragmentTag = supportFragmentManager.findFragmentById(containerId)?.tag
                    if (currentFragmentTag != TAG_CHANNEL) {
                        replaceFragment(containerId,ChannelFragment(), TAG_CHANNEL)
                    }
                    true
                }
                R.id.page_chats -> {
                    val currentFragmentTag = supportFragmentManager.findFragmentById(containerId)?.tag
                    clearChatBadge()

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


        LocalBroadcastManager.getInstance(this).registerReceiver(
            newChatReceiver,
            IntentFilter(Linksy.INTENT_ACTION_NEW_CHAT)
        )

messageViewModel.messageList.observe(this) { messagelist ->
    messagelist.map { m ->
        messageViewModel.insertMessage(m)
    }
}
        chatViewModel.getUserChats(tokenManager.getAccessToken())

        chatViewModel.chatList.observe(this){chatList ->
            if(chatList.isNotEmpty()) {
                chatList.map { chat ->
                    if(chat.unreadMessagesCount!=null) {
                        if (chat.senderId == null || (chat.senderId != userId && chat.unreadMessagesCount!! > 0)) {
                            chatBadgeCount++
                            updateChatBadge()
                        }
                    }
                }
            }
        }
        messageViewModel.getAllUserMessages(tokenManager.getAccessToken())
    }
    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(newChatReceiver)
    }
    private val newChatReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            chatBadgeCount++
            updateChatBadge()
        }
    }
    private fun updateChatBadge() {
        val badge = bottomNavigationView.getOrCreateBadge(R.id.page_chats)
        badge.isVisible = true
        badge.number = chatBadgeCount
    }

    private fun clearChatBadge(){
        val badge = bottomNavigationView.getOrCreateBadge(R.id.page_chats)
        chatBadgeCount = 0
        badge.number = 0
        badge.isVisible = false
    }

}