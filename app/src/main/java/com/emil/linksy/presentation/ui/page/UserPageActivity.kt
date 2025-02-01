package com.emil.linksy.presentation.ui.page

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.MessageMode
import com.emil.linksy.adapters.OutsiderProfilePagerAdapter
import com.emil.linksy.presentation.ui.BigPictureDialog
import com.emil.linksy.presentation.ui.ErrorDialog
import com.emil.linksy.presentation.ui.LoadingDialog
import com.emil.linksy.presentation.ui.QrBottomSheet
import com.emil.linksy.presentation.ui.ReportDialog
import com.emil.linksy.presentation.ui.navigation.chat.MessageActivity
import com.emil.linksy.presentation.ui.navigation.people.RelationsDialogFragment
import com.emil.linksy.presentation.viewmodel.PeopleViewModel
import com.emil.linksy.util.RelationType
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.hide
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
    private lateinit var mainLinearLayout:LinearLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var tabLayout: TabLayout
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "SetTextI18n", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_page)
        mainLinearLayout = findViewById(R.id.ll_main)
        val toolBar = findViewById<MaterialToolbar>(R.id.tb)
        val linkTextView = findViewById<MaterialTextView>(R.id.tv_link)
        val usernameTextView = findViewById<MaterialTextView>(R.id.tv_username)
        val birthdayTextView = findViewById<MaterialTextView>(R.id.tv_birthday)
        val subscriptionsTextView = findViewById<MaterialTextView>(R.id.tv_subscriptions)
        val onlineTextView = findViewById<MaterialTextView>(R.id.tv_status)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        subscriberTextView = findViewById(R.id.tv_subscriber)
        val avatarImageView = findViewById<ImageView>(R.id.iv_user_avatar)
        val confirmedImageView = findViewById<ImageView>(R.id.iv_confirmed)
        val subscriptionsLinerLayout = findViewById<LinearLayout>(R.id.ll_subscriptions)
        val subscribersLinerLayout = findViewById<LinearLayout>(R.id.ll_subscribers)
        val messageButton = findViewById<MaterialButton>(R.id.bt_message)
        val qrImageButton = findViewById<ImageButton>(R.id.ib_qr)
        sub_unsubButton = findViewById(R.id.bt_sub_unsub)
        tabLayout = findViewById(R.id.tl_profile_navigation)
        toolBar.setNavigationOnClickListener {
            finish()
        }
        swipeRefreshLayout.setOnRefreshListener {
            getData()
        }
        userId = intent.getLongExtra("USER_ID", -1)
        qrImageButton.setOnClickListener {
            it.anim()
            val bsDialog = QrBottomSheet.newInstance(userId)
            bsDialog.show(supportFragmentManager,bsDialog.tag)
        }
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


        peopleViewModel.pageData.observe(this){ pageData ->
               if(pageData.confirmed) confirmedImageView.show()
                  if(pageData.online) {
                      onlineTextView.text = getString(R.string.online)
                  }else onlineTextView.text = "${getString(R.string.was_online)}: ${pageData.lastActive}"



            toolBar.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.user_page_menu, menu)
                    val actionBlacklist = menu.findItem(R.id.action_blacklist)
                    actionBlacklist.title = if (pageData.isPageOwnerBlockedByViewer) {
                        getString(R.string.remove_blacklist)
                    } else {
                        getString(R.string.add_to_blacklist)
                    }
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.action_blacklist -> {
                            if (pageData.isPageOwnerBlockedByViewer) {
                                peopleViewModel.removeBlackList(
                                    tokenManager.getAccessToken(),
                                    userId,
                                    onSuccess = {
                                        menuItem.title = getString(R.string.add_to_blacklist)
                                        pageData.isPageOwnerBlockedByViewer = false
                                    }
                                )
                            } else {
                                peopleViewModel.addToBlackList(
                                    tokenManager.getAccessToken(),
                                    userId,
                                    onSuccess = {
                                        menuItem.title = getString(R.string.remove_blacklist)
                                        pageData.isPageOwnerBlockedByViewer = true
                                    }
                                )
                            }
                            true
                        }
                        R.id.action_report ->{
                             ReportDialog.newInstance(context = this@UserPageActivity,userId = userId, channelId = null,tokenManager, peopleViewModel)
                            true
                        }
                        else -> false
                    }
                }
            }, this, Lifecycle.State.CREATED)

            val link = pageData.link
            if (link!=null){
                linkTextView.show()
                linkTextView.text = "@$link"
            }
            val username = pageData.username
            usernameTextView.text = username

            subscriptionsLinerLayout.setOnClickListener {
                it.anim()
                RelationsDialogFragment(RelationType.SUBSCRIPTIONS,userId =userId,username =username).show(
                    supportFragmentManager, "RelationsDialogFragment"
                )
            }
            subscribersLinerLayout.setOnClickListener {
                it.anim()
                RelationsDialogFragment(RelationType.SUBSCRIBERS,userId = userId,username = username).show(
                    supportFragmentManager, "RelationsDialogFragment"
                )
            }


            val avatarUrl = pageData.avatarUrl
            if (avatarUrl!="null"){
                Glide.with(this)
                    .load(avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatarImageView)
                avatarImageView.setOnClickListener { BigPictureDialog.newInstance(avatarUrl).show(supportFragmentManager,  "BigPictureDialog") }

            }

            if (pageData.messageMode == MessageMode.NOBODY ||
                (pageData.messageMode == MessageMode.SUBSCRIPTIONS_ONLY && !pageData.isSubscription )){
                    messageButton.isEnabled = false
                    messageButton.alpha = 0.5f
                }

            messageButton.setOnClickListener {
                val startMessageActivity = Intent(this,MessageActivity::class.java)
                startMessageActivity.putExtra("USER_ID",userId)
                startMessageActivity.putExtra("AVATAR_URL",avatarUrl)
                startMessageActivity.putExtra("NAME",username)
                startMessageActivity.putExtra("ISGROUP",false)
                startActivity(startMessageActivity)
            }
            val birthday = pageData.birthday
            if (birthday!=null){
                birthdayTextView.show()
                birthdayTextView.text = "$birthday"
            }
            val isSubscriber = pageData.isSubscriber
            if(isSubscriber) setUnSubscribeAction() else setSubscribeAction()

            subscriptionsTextView.text = pageData.subscriptionsCount.toString()
            subscriberTextView.text = pageData.subscribersCount.toString()
        }
        getData()
    }


    private fun getData(){
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
        val token = tokenManager.getAccessToken()
        peopleViewModel.getUserPageData(token,userId,
            onSuccess = {
            loading.dismiss()
            mainLinearLayout.show()
            swipeRefreshLayout.isRefreshing=false }, onConflict = {
            val errorDialog =  ErrorDialog(this,R.string.user_not_found)
            errorDialog.close(action = {finish()})
        }, noAccess = {
            val errorDialog =  ErrorDialog(this,R.string.blackList_info)
            errorDialog.close(action = {finish()})
        }, onBlocked = {
            val errorDialog =  ErrorDialog(this,R.string.outsider_blocked_info)
            errorDialog.close(action = {finish()})
        }, onError = {
            val errorDialog =  ErrorDialog(this,R.string.failed_connection)
            errorDialog.close(action = {finish()})})
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
