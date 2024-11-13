package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.linksy.presentation.viewmodel.UserProfileDataViewModel
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.replaceFragment
import com.emil.presentation.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {
    private lateinit var usernameTextView:TextView
    private lateinit var avatarImageView:ImageView
    private lateinit var uploadAvatarImageView:ImageView
    private lateinit var tabLayout:TabLayout
    private lateinit var sharedPref: SharedPreferences
    private lateinit var shimmerUsername: ShimmerFrameLayout
    private lateinit var shimmerAvatar: ShimmerFrameLayout
    private val userProfileDataViewModel: UserProfileDataViewModel by viewModel<UserProfileDataViewModel>()
    private var containerId:Int =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId", "ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        containerId = R.id.fl_fragment_container_profile
        usernameTextView = view.findViewById(R.id.tv_username)
        shimmerUsername = view.findViewById(R.id.shimmer_username)
        shimmerAvatar = view.findViewById(R.id.shimmer_avatar)
        avatarImageView = view.findViewById(R.id.iv_user_avatar)
        uploadAvatarImageView = view.findViewById(R.id.iv_upload_avatar)
        tabLayout = view.findViewById(R.id.tl_profile_navigation)
        sharedPref = requireContext().getSharedPreferences("TokenData", Context.MODE_PRIVATE)

        shimmerUsername.setShimmer(Linksy.CUSTOM_SHIMMER)
        shimmerAvatar.setShimmer(Linksy.CUSTOM_SHIMMER)
        shimmerUsername.startShimmer()
        shimmerAvatar.startShimmer()
        showPosts()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> { showPosts() }
                    1 -> { showPhotos() }
                }}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                Glide.with(requireContext())
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatarImageView)
                handleSelectedImage(uri)
            }
        }
        uploadAvatarImageView.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
         val token = sharedPref.getString("ACCESS_TOKEN",null)
        userProfileDataViewModel.userData.observe(requireActivity()){
            data ->
                usernameTextView.text = data.username
            if (data.avatarUrl != "null") {
                Glide.with(requireContext())
                    .load(data.avatarUrl)
                    .into(avatarImageView)
            }
        }
        userProfileDataViewModel.getData(token!!, onSuccess = {

        }, onIncorrect = {} , onError = {}, onEnd = {})
        return view
    }
    private fun handleSelectedImage(uri: Uri) {

    }

    private fun showPosts(){
        replaceFragment(containerId,PostFragment())
    }
    private fun showPhotos(){
        replaceFragment(containerId,PhotoFragment())
    }

}