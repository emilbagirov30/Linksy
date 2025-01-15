package com.emil.linksy.presentation.ui.page

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat

import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.linksy.adapters.ChannelPostsAdapter
import com.emil.linksy.presentation.ui.BigPictureDialog
import com.emil.linksy.presentation.ui.ErrorDialog
import com.emil.linksy.presentation.ui.LoadingDialog
import com.emil.linksy.presentation.ui.QrBottomSheet
import com.emil.linksy.presentation.ui.navigation.channel.AddChannelPostDialogFragment
import com.emil.linksy.presentation.ui.navigation.people.RelationsDialogFragment
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.util.RelationType
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.showToast
import com.emil.presentation.R
import com.emil.presentation.databinding.ActivityChannelPageBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChannelPageActivity : AppCompatActivity() {
    private lateinit var binding :ActivityChannelPageBinding
    private val channelViewModel: ChannelViewModel by viewModel<ChannelViewModel>()
    private val tokenManager: TokenManager by inject()
    val userId:Long = -1
    var channelId:Long = -1
    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChannelPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val sharedPref: SharedPreferences = getSharedPreferences("AppData", Context.MODE_PRIVATE)
        val userId = sharedPref.getLong("ID",-1)
        val groupId = intent.getLongExtra("GROUP_ID",-1)
        val loading = LoadingDialog(this)
        loading.show()


        channelViewModel.pageData.observe(this){pageData ->
            channelId = pageData.channelId
            binding.tvName.text = pageData.name
            val avatarUrl = pageData.avatarUrl
            if (avatarUrl!="null"){
                Glide.with(this)
                    .load(avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivAvatar)
                binding.ivAvatar.setOnClickListener {
                    BigPictureDialog(this,avatarUrl).show(supportFragmentManager,  "BigPictureDialog")
                }

            }
            pageData.link?.let { binding.tvLink.show()
            binding.tvLink.text = "@${pageData.link}"
            }

            val rating = pageData.rating
            if (rating<0) binding.tvRating.text = "-"
            else binding.tvRating.text = rating.toString()

            if (rating<2.99 && rating>0)
                ViewCompat.setBackgroundTintList(binding.ivRating, ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.red)))
            if(rating >= 3.0 && rating < 4.0)  ViewCompat.setBackgroundTintList(binding.ivRating, ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.yellow)))
            if(rating >=4)  ViewCompat.setBackgroundTintList(binding.ivRating, ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.green)))
            binding.tvSubscribers.text = pageData.subscribersCount.toString()
            binding.tvDescription.text = pageData.description
            val isSubscriber = pageData.isSubscriber
            if(isSubscriber) binding.btSub.text =getString( R.string.unsubscribe) else binding.btSub.text =getString( R.string.subscribe)

            if (userId == pageData.ownerId){
                binding.etNewPost.show()
                binding.btSubmit.hide()
                binding.btSub.hide()
                binding.ivEditChannel.show()
                binding.etNewPost.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                       AddChannelPostDialogFragment(pageData.channelId).show(supportFragmentManager, "AddPostDialogFragment")
                        true
                    } else {
                        false
                    }
                }}

                if (pageData.type == "PRIVATE" && pageData.ownerId == userId) {
                    binding.llRequest.show()
                    binding.tvRequests.text = pageData.requestsCount.toString()
                    binding.llRequest.setOnClickListener {
                        RelationsDialogFragment(RelationType.REQUESTS, channelId = pageData.channelId).show(
                            supportFragmentManager, "RelationsDialogFragment"
                        )
                    }
                }

                   if(pageData.type == "PUBLIC" && pageData.ownerId!=userId){
                       if (pageData.isSubscriber){
                           setUnSubscribeAction()
                       }else setSubscribeAction()
                   }

                           if (pageData.type == "PRIVATE" && !pageData.isSubscriber  && pageData.ownerId!=userId&& !pageData.isSubmitted){
                               binding.btSubmit.show()
                               binding.btSub.hide()
                               binding.tvPosts.hide()
                               binding.rvPosts.hide()
                               binding.btSubmit.setOnClickListener {
                                setSubmitAction()
                                   }
                               }
            if (pageData.type == "PRIVATE" && pageData.isSubscriber && pageData.ownerId!=userId){
                binding.btSubmit.hide()
                binding.btSub.show()
                binding.btSub.setOnClickListener {
                   setUnSubscribeAction()
                }
            }

                 if(pageData.type == "PRIVATE" && pageData.isSubmitted){
                     binding.btSubmit.show()
                     binding.btSub.hide()
                     binding.tvPosts.hide()
                     binding.rvPosts.hide()
                     binding.btSubmit.text = getString(R.string.delete_request)
                     binding.btSubmit.setOnClickListener {
                        setDeleteRequestAction()
                     }

                 }

                if (isSubscriber) {
                    binding.llSubscribers.setOnClickListener { RelationsDialogFragment(RelationType.CHANNEL_MEMBERS, channelId = pageData.channelId).show(
                            supportFragmentManager, "RelationsDialogFragment"
                        )
                    }
                }

            channelViewModel.getChannelPosts(tokenManager.getAccessToken(),pageData.channelId, onConflict = {})


        }
        channelViewModel.getChannelPageData(tokenManager.getAccessToken(),groupId,
         onSuccess = {loading.dismiss()
              view.show()},

            onConflict = {
                val errorDialog =  ErrorDialog(this,R.string.channel_not_found)
                errorDialog.close(action = {finish()})
        })

        channelViewModel.postList.observe(this){postlist ->
            binding.rvPosts.layoutManager = LinearLayoutManager(this)
            binding.rvPosts.adapter = ChannelPostsAdapter(postlist,this,tokenManager,channelViewModel)
        }
        binding.ibQr.setOnClickListener {
            it.anim()
            val bsDialog = QrBottomSheet.newInstance(groupId)
            bsDialog.show(supportFragmentManager,bsDialog.tag)
        }

        binding.tb.setNavigationOnClickListener {
            finish()
        }
    }


    @SuppressLint("SetTextI18n")
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
    @SuppressLint("SetTextI18n")
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
        channelViewModel.submitRequest(tokenManager.getAccessToken(), channelId = channelId, onConflict = {}, onSuccess = {
            binding.btSubmit.text = getString(R.string.delete_request)
            binding.btSubmit.setOnClickListener {
                     setDeleteRequestAction()
            }
    })}
            private fun setDeleteRequestAction(){
                binding.btSubmit.text = getString(R.string.delete_request)
                channelViewModel.deleteRequest(tokenManager.getAccessToken(), channelId = channelId, onConflict = {}, onSuccess = {
                    binding.btSubmit.text = getString(R.string.submit_request)
                    binding.btSubmit.setOnClickListener {
                       setSubmitAction()
                    }
            })}
}