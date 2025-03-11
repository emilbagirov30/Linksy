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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.linksy.adapters.ProfilePagerAdapter
import com.emil.linksy.presentation.ui.BigPictureDialog
import com.emil.linksy.presentation.ui.QrBottomSheet
import com.emil.linksy.presentation.ui.settings.CommonSettingsDialogFragment
import com.emil.linksy.presentation.viewmodel.UserViewModel
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates


class ProfileFragment : Fragment(),CommonSettingsDialogFragment.UpdateDataListener {
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
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("current_tab", currentTabPosition)
    }
    @SuppressLint("MissingInflatedId", "ResourceAsColor", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
  }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usernameTextView = view.findViewById(R.id.tv_username)
        linkTextView = view.findViewById(R.id.tv_link)
        shimmerUsername = view.findViewById(R.id.shimmer_username)
        shimmerAvatar = view.findViewById(R.id.shimmer_avatar)
        shimmerLink = view.findViewById(R.id.shimmer_link)
        avatarImageView = view.findViewById(R.id.iv_user_avatar)
        editUserDataImageView = view.findViewById(R.id.iv_edit_user_data)
        tabLayout = view.findViewById(R.id.tl_profile_navigation)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        val confirmed = view.findViewById<ImageView>(R.id.iv_confirmed)
        val qrImageButton = view.findViewById<ImageButton>(R.id.ib_qr)
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences("appData", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        fetchData()
        swipeRefreshLayout.setOnRefreshListener {
            fetchData()
        }
        editUserDataImageView.setOnClickListener {
            it.anim()
            val dialog = CommonSettingsDialogFragment()
            dialog.setUpdateListener(this)
            dialog.show(parentFragmentManager, "CommonSettingsDialog")
        }
        userViewModel.userData.observe(requireActivity()){ data ->
            swipeRefreshLayout.isRefreshing=false
            id = data.id
            editor.putLong("ID", id)
            editor.apply()
            qrImageButton.show()
            usernameTextView.text = data.username
            if(data.confirmed) confirmed.show() else confirmed.hide()
            if (data.avatarUrl != Linksy.EMPTY_AVATAR) {
                avatarImageView.setOnClickListener {
                    avatarImageView.setOnClickListener { BigPictureDialog.newInstance(data.avatarUrl).show(parentFragmentManager,  "BigPictureDialog") }
                }


                context?.let {
                    Glide.with(it)
                        .load(data.avatarUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(avatarImageView)
                }
            }
            showContent()
            if (!data.link.isNullOrEmpty()){
                linkTextView.text = "@${data.link}"
                linkTextView.show()
            } else linkTextView.hide()
        }

        qrImageButton.setOnClickListener {
            it.anim()
            val bsDialog = QrBottomSheet.newInstance(id)
            bsDialog.show(parentFragmentManager,bsDialog.tag)
        }
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
        val token = tokenManager.getAccessToken()
        userViewModel.getData(token,onIncorrect = { fetchData() } , onError = {
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
        val pageAdapter = ProfilePagerAdapter(this)
        val viewPager = requireView().findViewById<ViewPager2>(R.id.vp_profile_pager)
        viewPager.adapter = pageAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.posts)
                1 -> getString(R.string.moments)
                else -> null
            }
        }.attach()
    }

    override fun update() {
        fetchData()
    }

}