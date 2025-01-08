package com.emil.linksy.presentation.ui.page

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.linksy.adapters.OutsiderProfilePagerAdapter
import com.emil.linksy.presentation.ui.ErrorDialog
import com.emil.linksy.presentation.ui.LoadingDialog
import com.emil.linksy.presentation.ui.navigation.chat.MessageActivity
import com.emil.linksy.presentation.ui.navigation.people.OutsiderRelationsDialogFragment
import com.emil.linksy.presentation.viewmodel.PeopleViewModel
import com.emil.linksy.util.RelationType
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.show
import com.emil.linksy.util.showToast
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textview.MaterialTextView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates


class UserPageActivity (): AppCompatActivity() {
    private val peopleViewModel: PeopleViewModel by viewModel<PeopleViewModel>()
    private val tokenManager: TokenManager by inject()
    private lateinit var sub_unsubButton:MaterialButton
    private lateinit var subscriberTextView:MaterialTextView
    private var userId by Delegates.notNull<Long>()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_page)
        val mainLinearLayout = findViewById<LinearLayout>(R.id.ll_main)
        val toolBar = findViewById<MaterialToolbar>(R.id.tb)
        val linkTextView = findViewById<MaterialTextView>(R.id.tv_link)
        val usernameTextView = findViewById<MaterialTextView>(R.id.tv_username)
        val birthdayTextView = findViewById<MaterialTextView>(R.id.tv_birthday)
        val subscriptionsTextView = findViewById<MaterialTextView>(R.id.tv_subscriptions)
         subscriberTextView = findViewById(R.id.tv_subscriber)
        val avatarImageView = findViewById<ImageView>(R.id.iv_user_avatar)
        val subscriptionsLinerLayout = findViewById<LinearLayout>(R.id.ll_subscriptions)
        val subscribersLinerLayout = findViewById<LinearLayout>(R.id.ll_subscribers)
        val messageButton = findViewById<MaterialButton>(R.id.bt_message)
         sub_unsubButton = findViewById(R.id.bt_sub_unsub)
        val tabLayout = findViewById<TabLayout>(R.id.tl_profile_navigation)
        toolBar.setNavigationOnClickListener {
            finish()
        }
        userId = intent.getLongExtra("USER_ID", -1)
        val viewPager = findViewById<ViewPager2>(R.id.vp_profile_pager)
        val pagerAdapter = OutsiderProfilePagerAdapter(userId,this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.posts)
                1 -> getString(R.string.moments)
                else -> null
            }
        }.attach()
        val loading = LoadingDialog(this)
        loading.show()

        messageButton.setOnClickListener {
            val startMessageActivity = Intent(this,MessageActivity::class.java)
            startMessageActivity.putExtra("USER_ID",userId)
            startActivity(startMessageActivity)
        }




        peopleViewModel.pageData.observe(this){ pageData ->
            val link = pageData.link
            if (link!=null){
                linkTextView.show()
                linkTextView.text = "@$link"
            }
            val username = pageData.username
            usernameTextView.text = username

            subscriptionsLinerLayout.setOnClickListener {
                it.anim()
                OutsiderRelationsDialogFragment(RelationType.SUBSCRIPTIONS,userId,username).show(
                    supportFragmentManager, "OutsiderRelationsDialogFragment"
                )
            }
            subscribersLinerLayout.setOnClickListener {
                it.anim()
                OutsiderRelationsDialogFragment(RelationType.SUBSCRIBERS,userId,username).show(
                    supportFragmentManager, "OutsiderRelationsDialogFragment"
                )
            }


            val avatarUrl = pageData.avatarUrl
            if (avatarUrl!="null"){
                Glide.with(this)
                    .load(avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatarImageView)
            }

            val birthday = pageData.birthday
            if (birthday!=null){
                birthdayTextView.show()
                birthdayTextView.text = "$birthday"
            }
            var isSubscriber = pageData.isSubscriber
            if(isSubscriber) setUnSubscribeAction() else setSubscribeAction()

            subscriptionsTextView.text = pageData.subscriptionsCount.toString()
            subscriberTextView.text = pageData.subscribersCount.toString()
        }
         val token = tokenManager.getAccessToken()
        peopleViewModel.getUserPageData(token,userId, onSuccess = { loading.dismiss()
            mainLinearLayout.show()}, onConflict = {
            val errorDialog =  ErrorDialog(this,R.string.user_not_found)
            errorDialog.close(action = {finish()})
        })

    }
    @SuppressLint("SetTextI18n")
    private fun setSubscribeAction(){
        sub_unsubButton.text = getString(R.string.subscribe)
        sub_unsubButton.setOnClickListener {
            peopleViewModel.subscribe(tokenManager.getAccessToken(),userId, onSuccess = {
                sub_unsubButton.text = getString(R.string.unsubscribe)
                showToast(this,R.string.subscribed)
                setUnSubscribeAction()
                val count = subscriberTextView.text.toString().toLong()
                subscriberTextView.text = (count+1).toString()
            })
        }
    }
    @SuppressLint("SetTextI18n")
    private fun setUnSubscribeAction(){
        sub_unsubButton.text = getString(R.string.unsubscribe)
        sub_unsubButton.setOnClickListener {
            peopleViewModel.unsubscribe(tokenManager.getAccessToken(),userId, onSuccess = {
                sub_unsubButton.text = getString(R.string.subscribe)
                showToast(this,R.string.unsubscribed)
                setSubscribeAction()
                val count = subscriberTextView.text.toString().toLong()
                subscriberTextView.text = (count-1).toString()
            })
        }
    }

}
