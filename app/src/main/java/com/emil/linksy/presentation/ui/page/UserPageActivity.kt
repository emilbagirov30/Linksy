package com.emil.linksy.presentation.ui.page

import android.annotation.SuppressLint
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
import com.emil.linksy.presentation.ui.LoadingDialog
import com.emil.linksy.presentation.viewmodel.PeopleViewModel
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textview.MaterialTextView
import org.koin.androidx.viewmodel.ext.android.viewModel


class UserPageActivity (): AppCompatActivity() {
    private val peopleViewModel: PeopleViewModel by viewModel<PeopleViewModel>()

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
        val subscriberTextView = findViewById<MaterialTextView>(R.id.tv_subscriber)
        val avatarImageView = findViewById<ImageView>(R.id.iv_user_avatar)
        val tabLayout = findViewById<TabLayout>(R.id.tl_profile_navigation)
        toolBar.setNavigationOnClickListener {
            finish()
        }
        val userId = intent.getLongExtra("USER_ID", -1)
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

        peopleViewModel.pageData.observe(this){ pageData ->
            loading.dismiss()
            mainLinearLayout.show()
            val link = pageData.link
            if (link!=null){
                linkTextView.show()
                linkTextView.text = "@$link"
            }
            usernameTextView.text = pageData.username
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
            subscriptionsTextView.text = pageData.subscriptionsCount.toString()
            subscriberTextView.text = pageData.subscribersCount.toString()
        }

        peopleViewModel.getUserPageData(userId)

    }
}
