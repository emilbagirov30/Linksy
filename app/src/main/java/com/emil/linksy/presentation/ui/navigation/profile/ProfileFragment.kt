package com.emil.linksy.presentation.ui.navigation.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.linksy.adapters.ProfilePagerAdapter
import com.emil.linksy.presentation.ui.QrBottomSheet
import com.emil.linksy.presentation.ui.settings.CommonSettingsDialogFragment
import com.emil.linksy.presentation.viewmodel.UserViewModel
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.showToast
import com.emil.presentation.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates


class ProfileFragment : Fragment() {
    private lateinit var usernameTextView:TextView
    private lateinit var linkTextView:TextView
    private lateinit var avatarImageView:ImageView
    private lateinit var editUserDataImageView:ImageView
    private lateinit var tabLayout:TabLayout
   private lateinit var shimmerUsername: ShimmerFrameLayout
   private lateinit var shimmerAvatar: ShimmerFrameLayout
    private lateinit var shimmerLink: ShimmerFrameLayout
    private val userViewModel: UserViewModel by viewModel<UserViewModel>()
    private val tokenManager: TokenManager by inject()
    private var currentTabPosition: Int = 0
    private var id by Delegates.notNull<Long>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("current_tab", currentTabPosition)
    }
    @SuppressLint("MissingInflatedId", "ResourceAsColor", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        usernameTextView = view.findViewById(R.id.tv_username)
        linkTextView = view.findViewById(R.id.tv_link)
        shimmerUsername = view.findViewById(R.id.shimmer_username)
        shimmerAvatar = view.findViewById(R.id.shimmer_avatar)
        shimmerLink = view.findViewById(R.id.shimmer_link)
        avatarImageView = view.findViewById(R.id.iv_user_avatar)
        editUserDataImageView = view.findViewById(R.id.iv_edit_user_data)
        tabLayout = view.findViewById(R.id.tl_profile_navigation)
        val qrImageButton = view.findViewById<ImageButton>(R.id.ib_qr)
        val viewPager = view.findViewById<ViewPager2>(R.id.vp_profile_pager)
        val pagerAdapter = ProfilePagerAdapter(this)
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences("appData", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()

        viewPager.adapter = pagerAdapter
        fetchData()
        editUserDataImageView.setOnClickListener {
            it.anim()
            CommonSettingsDialogFragment().show(parentFragmentManager, "CommonSettingsDialog")
        }
        userViewModel.userData.observe(requireActivity()){ data ->
            id = data.id
            editor.putLong("ID", id)
            editor.apply()
            qrImageButton.show()
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
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.posts)
                1 -> getString(R.string.moments)
                else -> null
            }
        }.attach()
        savedInstanceState?.let {
            currentTabPosition = it.getInt("current_tab", 0)
            viewPager.setCurrentItem(currentTabPosition, false)
        }

        qrImageButton.setOnClickListener {
             it.anim()
             val bsDialog = QrBottomSheet.newInstance(id)
             bsDialog.show(parentFragmentManager,bsDialog.tag)
        }
      return view
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
     fun fetchData() {
        startShimmer()
        val token = tokenManager.getAccessToken()
        userViewModel.getData(token,onIncorrect = { showToast(requireContext(),R.string.error_invalid_token) } , onError = {
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