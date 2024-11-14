package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar

class ProfileSettingsDialogFragment: DialogFragment() {
    private lateinit var uploadAvatarImageView: ImageView
    private lateinit var toolBar: MaterialToolbar
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_settings_dialog, container, false)
        uploadAvatarImageView = view.findViewById(R.id.iv_upload_avatar)
        toolBar = view.findViewById(R.id.tb_edit_data)
        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                Glide.with(requireContext())
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(uploadAvatarImageView)
                handleSelectedImage(uri)
            }
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
}