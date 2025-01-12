package com.emil.linksy.presentation.ui.page

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.linksy.presentation.ui.ErrorDialog
import com.emil.linksy.presentation.ui.LoadingDialog
import com.emil.linksy.presentation.ui.QrBottomSheet
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.presentation.viewmodel.PeopleViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.emil.presentation.databinding.ActivityChannelPageBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChannelPageActivity : AppCompatActivity() {
    private lateinit var binding :ActivityChannelPageBinding
    private val channelViewModel: ChannelViewModel by viewModel<ChannelViewModel>()
    private val tokenManager: TokenManager by inject()
    @SuppressLint("SetTextI18n")
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
            binding.tvName.text = pageData.name
            val avatarUrl = pageData.avatarUrl
            if (avatarUrl!="null"){
                Glide.with(this)
                    .load(avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.ivAvatar)
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
            binding.tvSubscriber.text = pageData.subscribersCount.toString()
            binding.tvDescription.text = pageData.description
            val isSubscriber = pageData.isSubscriber
            if(isSubscriber) binding.btSub.text =getString( R.string.unsubscribe) else binding.btSub.text =getString( R.string.subscribe)

            if (userId == pageData.ownerId){
                binding.etNewPost.show()
                binding.ivEditChannel.show()
                binding.llRequest.show()
            }

        }
        channelViewModel.getChannelPageData(tokenManager.getAccessToken(),groupId,
onSuccess = {loading.dismiss()
    view.show()},

            onConflict = {
                val errorDialog =  ErrorDialog(this,R.string.channel_not_found)
                errorDialog.close(action = {finish()})
        })

        binding.ibQr.setOnClickListener {
            it.anim()
            val bsDialog = QrBottomSheet.newInstance(groupId)
            bsDialog.show(supportFragmentManager,bsDialog.tag)
        }

        binding.tb.setNavigationOnClickListener {
            finish()
        }
    }
}