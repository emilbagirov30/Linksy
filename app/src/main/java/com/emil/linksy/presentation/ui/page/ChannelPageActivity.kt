package com.emil.linksy.presentation.ui.page

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.domain.model.ChannelType
import com.emil.linksy.adapters.ChannelPostsAdapter
import com.emil.linksy.presentation.ui.BigPictureDialog
import com.emil.linksy.presentation.ui.ErrorDialog
import com.emil.linksy.presentation.ui.LoadingDialog
import com.emil.linksy.presentation.ui.QrBottomSheet
import com.emil.linksy.presentation.ui.ReportDialog
import com.emil.linksy.presentation.ui.navigation.channel.AddChannelPostDialogFragment
import com.emil.linksy.presentation.ui.navigation.channel.EditChannelActivity
import com.emil.linksy.presentation.ui.navigation.people.RelationsDialogFragment
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.presentation.viewmodel.PeopleViewModel
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.RelationType
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.colorByRating
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.showToast
import com.emil.presentation.R
import com.emil.presentation.databinding.ActivityChannelPageBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChannelPageActivity : AppCompatActivity(),AddChannelPostDialogFragment.AddChannelPostDialogListener {
    private lateinit var binding :ActivityChannelPageBinding
    private val channelViewModel: ChannelViewModel by viewModel<ChannelViewModel>()
    private val peopleViewModel: PeopleViewModel by viewModel<PeopleViewModel>()
    private val tokenManager: TokenManager by inject()
    val userId:Long = -1
    private var channelId = -1L
    private lateinit var loading:LoadingDialog
    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChannelPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val sharedPref: SharedPreferences = getSharedPreferences("appData", Context.MODE_PRIVATE)
        val userId = sharedPref.getLong("ID",-1)
        channelId = intent.getLongExtra("CHANNEL_ID",-1)
        loading = LoadingDialog(this)
        loading.show()
        val adapter = ChannelPostsAdapter (tokenManager, channelViewModel, userId, channelId)
        binding.rvPosts.layoutManager = LinearLayoutManager(this)
        binding.rvPosts.adapter = adapter
        channelViewModel.pageData.observe(this){pageData ->
            if (pageData.confirmed)
                binding.ivConfirmed.show()

            binding.tvName.text = pageData.name
            val avatarUrl = pageData.avatarUrl
            if (avatarUrl!= Linksy.EMPTY_AVATAR){
                Glide.with(this)
                    .load(avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivAvatar)
                binding.ivAvatar.setOnClickListener {
                    BigPictureDialog.newInstance(avatarUrl).show(supportFragmentManager,  "BigPictureDialog")
                }

            }
            pageData.link?.let { binding.tvLink.show()
            binding.tvLink.text = "@${pageData.link}"
            }

            val rating = pageData.rating
            binding.tvRating.text = rating.toString()
            binding.ivRating.colorByRating(rating)
            binding.tvSubscribers.text = pageData.subscribersCount.toString()
            binding.tvDescription.text = pageData.description
            val isSubscriber = pageData.isSubscriber
            if(isSubscriber) binding.btSub.text =getString( R.string.unsubscribe) else binding.btSub.text =getString( R.string.subscribe)

            if (userId == pageData.ownerId){
                binding.etNewPost.show()
                binding.btSubmit.hide()
                binding.btSub.hide()
                binding.ivEditChannel.show()

                binding.ivEditChannel.setOnClickListener {
                    it.anim()
                    val channelEditIntent = Intent(this,EditChannelActivity::class.java)
                    channelEditIntent.putExtra("CHANNEL_ID",channelId)
                    startActivity(channelEditIntent)
                }



                binding.etNewPost.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        val dialog =   AddChannelPostDialogFragment.newInstance(channelId = pageData.channelId)
                        dialog.setAddPostDialogListener(this)
                        dialog.show(supportFragmentManager, "AddPostDialogFragment")
                        true
                    } else {
                        false
                    }
                }}

                if (pageData.type == ChannelType.PRIVATE && pageData.ownerId == userId) {
                    binding.llRequest.show()
                    binding.tvRequests.text = pageData.requestsCount.toString()
                    binding.llRequest.setOnClickListener {
                        RelationsDialogFragment.newInstance(RelationType.REQUESTS, channelId = pageData.channelId).show(
                            supportFragmentManager, "RelationsDialogFragment"
                        )
                    }
                }

                   if(pageData.type == ChannelType.PUBLIC && pageData.ownerId!=userId){
                       if (pageData.isSubscriber){
                           setUnSubscribeAction()
                       }else setSubscribeAction()
                   }

                           if (pageData.type ==ChannelType.PRIVATE && !pageData.isSubscriber  && pageData.ownerId!=userId&& !pageData.isSubmitted){
                               binding.btSubmit.show()
                               binding.btSub.hide()
                               binding.rvPosts.hide()
                               binding.btSubmit.setOnClickListener {
                                setSubmitAction()
                                   }
                               }
                if (pageData.type == ChannelType.PRIVATE && pageData.isSubscriber && pageData.ownerId!=userId){
                binding.btSubmit.hide()
                binding.btSub.show()
                binding.btSub.setOnClickListener {
                   setUnSubscribeAction()
                    }
                 }
                 if(pageData.type == ChannelType.PRIVATE && pageData.isSubmitted){
                     binding.btSubmit.show()
                     binding.btSub.hide()
                     binding.rvPosts.hide()
                     binding.btSubmit.text = getString(R.string.delete_request)
                     binding.btSubmit.setOnClickListener {
                        setDeleteRequestAction()
                     }

                 }

                if (isSubscriber) {
                    binding.llSubscribers.setOnClickListener { RelationsDialogFragment.newInstance(RelationType.CHANNEL_MEMBERS, channelId = pageData.channelId).show(
                            supportFragmentManager, "RelationsDialogFragment"
                        )
                    }
                }

        }
           getData()
        binding.swipeRefreshLayout.setOnRefreshListener {
            getData()
        }

        channelViewModel.postList.observe(this){postlist ->
            adapter.submitList(postlist)
        }
        binding.ibQr.setOnClickListener {
            it.anim()
            val bsDialog = QrBottomSheet.newInstance(channelId)
            bsDialog.show(supportFragmentManager,bsDialog.tag)
        }

        binding.tb.setNavigationOnClickListener {
            finish()
        }




        binding.tb.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.channel_page_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_report ->{
                        ReportDialog.newInstance(context = this@ChannelPageActivity,userId = null, channelId = channelId,tokenManager, peopleViewModel)
                        true
                    }
                    else -> false
                }
            }
        }, this, Lifecycle.State.CREATED)
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    fun getData(){
        channelViewModel.getChannelPageData(tokenManager.getAccessToken(),channelId,
            onSuccess = {
                binding.swipeRefreshLayout.isRefreshing = false
                loading.dismiss()
                binding.root.show() },
            onConflict = {
                binding.root.hide()
                val errorDialog =  ErrorDialog(this,R.string.channel_not_found)
                errorDialog.close(action = {finish()})
            }, onBlocked = {
                binding.root.hide()
                val errorDialog =  ErrorDialog(this,R.string.channel_blocked)
                errorDialog.close(action = {finish()})
            }, onError = {
                binding.root.hide()
                val errorDialog =  ErrorDialog(this,R.string.failed_connection)
                errorDialog.close(action = {finish()})}
            )

        channelViewModel.getChannelPosts(tokenManager.getAccessToken(), channelId = channelId)
    }
    private fun setSubscribeAction(){
        binding.btSub.text = getString(R.string.subscribe)
        binding.btSub.setOnClickListener {
           channelViewModel.subscribe(tokenManager.getAccessToken(),channelId, onSuccess = {
               binding.btSub.text = getString(R.string.unsubscribe)
                showToast(this,R.string.subscribed)
                setUnSubscribeAction()
                val count = binding.tvSubscribers.text.toString().toLong()
               binding.tvSubscribers.text = (count+1).toString()
            })
        }
    }

    private fun setUnSubscribeAction(){
        binding.btSub.text = getString(R.string.unsubscribe)
        binding.btSub.setOnClickListener {
            channelViewModel.unsubscribe(tokenManager.getAccessToken(),channelId, onSuccess = {
                binding.btSub.text = getString(R.string.subscribe)
                showToast(this,R.string.unsubscribed)
                setSubscribeAction()
                val count =  binding.tvSubscribers.text.toString().toLong()
                binding.tvSubscribers.text = (count-1).toString()
            })
        }
    }


    private fun setSubmitAction(){
        binding.btSubmit.text = getString(R.string.submit_request)
        channelViewModel.submitRequest(tokenManager.getAccessToken(), channelId = channelId, onSuccess = {
            binding.btSubmit.text = getString(R.string.delete_request)
            binding.btSubmit.setOnClickListener {
                     setDeleteRequestAction()
            }
    })}
            private fun setDeleteRequestAction(){
                binding.btSubmit.text = getString(R.string.delete_request)
                channelViewModel.deleteRequest(tokenManager.getAccessToken(), channelId = channelId, onSuccess = {
                    binding.btSubmit.text = getString(R.string.submit_request)
                    binding.btSubmit.setOnClickListener {
                       setSubmitAction()
                    }
            })}

    override fun onPostAdded() {
        getData()
    }
}