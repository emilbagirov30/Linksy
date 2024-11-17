package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.linksy.presentation.viewmodel.UserProfileDataViewModel
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.hide
import com.emil.linksy.util.replaceFragment
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {
    private lateinit var usernameTextView:TextView
    private lateinit var avatarImageView:ImageView
    private lateinit var editUserDataImageView:ImageView
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
        editUserDataImageView = view.findViewById(R.id.iv_edit_user_data)
        tabLayout = view.findViewById(R.id.tl_profile_navigation)
        sharedPref = requireContext().getSharedPreferences("TokenData", Context.MODE_PRIVATE)
       shimmerUsername.setShimmer(Linksy.CUSTOM_SHIMMER)
      shimmerAvatar.setShimmer(Linksy.CUSTOM_SHIMMER)
        showPosts()
        shimmerUsername.startShimmer()
        shimmerAvatar.startShimmer()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> { showPosts() }
                    1 -> { showPhotos() }
                }}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        editUserDataImageView.setOnClickListener {
            CommonSettingsDialogFragment().show(parentFragmentManager, "EditProfileDialog")
        }
         val token = sharedPref.getString("ACCESS_TOKEN",null)
     userProfileDataViewModel.getData(token!!, onSuccess = {

      }, onIncorrect = {} , onError = {

         Snackbar.make(view, getString(R.string.error_loading_data), Snackbar.LENGTH_INDEFINITE).apply {
             setBackgroundTint(Color.WHITE)
             setTextColor(Color.GRAY)
             setAction(getString(R.string.repeat)) {
             }
             setActionTextColor(Color.BLUE)
             show()
         }
     }, onEnd = {})

        userProfileDataViewModel.userData.observe(requireActivity()){ data ->
            usernameTextView.text = data.username
            if (data.avatarUrl != "null") {
                Glide.with(requireContext())
                    .load(data.avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatarImageView)
            }
          showContent()
        }
      return view
  }



    private fun showPosts(){
        replaceFragment(containerId,PostFragment())
    }
    private fun showPhotos(){
        replaceFragment(containerId,PhotoFragment())
    }

    private fun showContent (){
        shimmerAvatar.stopShimmer()
        shimmerUsername.stopShimmer()
        shimmerUsername.setShimmer(null)
        shimmerAvatar.setShimmer(null)
        shimmerUsername.hide()
        shimmerAvatar.hide()
        avatarImageView.show()
        usernameTextView.show()
    }



}