package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.linksy.presentation.viewmodel.AllUserDataViewModel
import com.emil.linksy.presentation.viewmodel.UserProfileDataViewModel
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.showToast
import com.emil.linksy.util.string
import com.emil.presentation.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileSettingsDialogFragment: DialogFragment() {
    private lateinit var uploadAvatarImageView: ImageView
    private lateinit var changePasswordTextView: MaterialTextView
    private lateinit var toolBar: MaterialToolbar
    private lateinit var shimmerAvatar: ShimmerFrameLayout
    private lateinit var shimmerContent: ShimmerFrameLayout
    private lateinit var contentLinearLayout:LinearLayout
    private lateinit var sharedPref: SharedPreferences
    private lateinit var usernameEditText:EditText
    private lateinit var emailEditText:EditText
    private lateinit var linkEditText:EditText
    private lateinit var birthdayEditText:EditText
    private lateinit var avatarFrameLayout: FrameLayout
    private lateinit var avatarImageView:ImageView
    private val allUserDataViewModel:AllUserDataViewModel by viewModel<AllUserDataViewModel>()
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_settings_dialog, container, false)
        uploadAvatarImageView = view.findViewById(R.id.iv_upload_avatar)
        toolBar = view.findViewById(R.id.tb_edit_data)
        changePasswordTextView = view.findViewById(R.id.tv_change_password)
        shimmerAvatar = view.findViewById(R.id.shimmer_avatar)
        shimmerContent = view.findViewById(R.id.shimmer_content)
        contentLinearLayout = view.findViewById(R.id.ll_content)
        usernameEditText = view.findViewById(R.id.et_username)
        emailEditText = view.findViewById(R.id.et_email)
        linkEditText = view.findViewById(R.id.et_link)
        birthdayEditText = view.findViewById(R.id.et_birthday)
        avatarFrameLayout = view.findViewById(R.id.fl_avatar)
        avatarImageView = view.findViewById(R.id.iv_user_avatar)
        sharedPref = requireContext().getSharedPreferences("TokenData", Context.MODE_PRIVATE)
        shimmerAvatar.setShimmer(Linksy.CUSTOM_SHIMMER)
        shimmerContent.setShimmer(Linksy.CUSTOM_SHIMMER)
        shimmerContent.startShimmer()
        shimmerAvatar.startShimmer()
        val token = sharedPref.getString("ACCESS_TOKEN",null)

        allUserDataViewModel.getData(token!!, onSuccess = { },
            onIncorrect = {} , onError = {
                shimmerAvatar.stopShimmer()
                shimmerContent.stopShimmer()
                Snackbar.make(requireView(), getString(R.string.error_loading_data), Snackbar.LENGTH_INDEFINITE).apply {
                setBackgroundTint(Color.WHITE)
                setTextColor(Color.GRAY)
                setAction(getString(R.string.repeat)) {
                }
                setActionTextColor(Color.BLUE)
                show()
            }
            }, onEnd = {})

        allUserDataViewModel.userData.observe(requireActivity()){data ->
            usernameEditText.setText(data.username)
            emailEditText.setText(data.email)
            println(data.link)
            linkEditText.setText(if (data.link == null) ""
            else data.link)
            birthdayEditText.setText(if (data.birthday==null) ""
            else data.birthday )
            if (data.avatarUrl != "null") {
                Glide.with(requireContext())
                    .load(data.avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatarImageView)

            }
            showContent()
        }

        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                Glide.with(requireContext())
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatarImageView)
                handleSelectedImage(uri)
            }
        }
        changePasswordTextView.setOnClickListener {
           PasswordChangeDialogFragment().show(parentFragmentManager, "ChangePasswordDialogFragment")
        }
        uploadAvatarImageView.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
        toolBar.setNavigationOnClickListener { dialog?.dismiss() }
        return view
    }
    private fun handleSelectedImage(uri: Uri) {

    }

    override fun getTheme() = R.style.FullScreenDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)

    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }
    private fun showContent (){
        shimmerAvatar.stopShimmer()
        shimmerContent.stopShimmer()
        shimmerContent.setShimmer(null)
        shimmerAvatar.setShimmer(null)
        shimmerContent.hide()
        shimmerAvatar.hide()
        avatarFrameLayout.show()
        contentLinearLayout.show()
    }
}