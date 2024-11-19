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
import com.emil.linksy.util.showToast
import com.emil.presentation.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {
    private lateinit var usernameTextView:TextView
    private lateinit var linkTextView:TextView
    private lateinit var avatarImageView:ImageView
    private lateinit var editUserDataImageView:ImageView
    private lateinit var tabLayout:TabLayout
    private lateinit var sharedPref: SharedPreferences
   private lateinit var shimmerUsername: ShimmerFrameLayout
   private lateinit var shimmerAvatar: ShimmerFrameLayout
    private lateinit var shimmerLink: ShimmerFrameLayout
    private val userProfileDataViewModel: UserProfileDataViewModel by viewModel<UserProfileDataViewModel>()
    private var containerId:Int =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingInflatedId", "ResourceAsColor", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        containerId = R.id.fl_fragment_container_profile
        usernameTextView = view.findViewById(R.id.tv_username)
        linkTextView = view.findViewById(R.id.tv_link)
       shimmerUsername = view.findViewById(R.id.shimmer_username)
        shimmerAvatar = view.findViewById(R.id.shimmer_avatar)
        shimmerLink = view.findViewById(R.id.shimmer_link)
        avatarImageView = view.findViewById(R.id.iv_user_avatar)
        editUserDataImageView = view.findViewById(R.id.iv_edit_user_data)
        tabLayout = view.findViewById(R.id.tl_profile_navigation)
        sharedPref = requireContext().getSharedPreferences("TokenData", Context.MODE_PRIVATE)
        showPosts()
        fetchData()
        editUserDataImageView.setOnClickListener {
            CommonSettingsDialogFragment().show(parentFragmentManager, "CommonSettingsDialog")
        }
        userProfileDataViewModel.userData.observe(requireActivity()){ data ->
            usernameTextView.text = data.username
            if (data.avatarUrl != "null") {
                Glide.with(requireContext())
                    .load(data.avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatarImageView)
            }
            showContent()
            if (!data.link.isNullOrEmpty()) linkTextView.text = "@${data.link}"
            else linkTextView.hide()
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> { showPosts() }
                    1 -> { showPhotos() }
                }}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
      return view
  }


    private fun showPosts(){
        replaceFragment(containerId,PostFragment())
    }
    private fun showPhotos(){
        replaceFragment(containerId,PhotoFragment())
    }

    private fun startShimmer(){
        shimmerUsername.setShimmer(Linksy.CUSTOM_SHIMMER)
        shimmerAvatar.setShimmer(Linksy.CUSTOM_SHIMMER)
        shimmerLink.setShimmer(Linksy.CUSTOM_SHIMMER)
        shimmerUsername.startShimmer()
        shimmerAvatar.startShimmer()
        shimmerLink.startShimmer()
    }
    private fun showContent (){
        shimmerAvatar.stopShimmer()
        shimmerUsername.stopShimmer()
        shimmerLink.stopShimmer()
        shimmerUsername.setShimmer(null)
        shimmerAvatar.setShimmer(null)
        shimmerLink.setShimmer(null)
        shimmerUsername.hide()
        shimmerAvatar.hide()
        shimmerLink.hide()
        avatarImageView.show()
        usernameTextView.show()
        linkTextView.show()
    }
private fun stopShimmer(){
    shimmerAvatar.stopShimmer()
    shimmerUsername.stopShimmer()
    shimmerLink.stopShimmer()
}
    private fun fetchData() {
        startShimmer()
       val token = sharedPref.getString("ACCESS_TOKEN",null).toString()
        userProfileDataViewModel.getData(token,onIncorrect = { showToast(requireContext(),R.string.error_invalid_token) } , onError = {
            stopShimmer()
            if (isAdded && view != null) {
                Snackbar.make(requireView(), getString(R.string.error_loading_data), Snackbar.LENGTH_INDEFINITE).apply {
                    setBackgroundTint(Color.WHITE)
                    setTextColor(Color.GRAY)
                    setAction(getString(R.string.repeat)) {
                         fetchData()
                    }
                    setActionTextColor(Color.BLUE)
                    show()
                }
            }
        })
    }

}