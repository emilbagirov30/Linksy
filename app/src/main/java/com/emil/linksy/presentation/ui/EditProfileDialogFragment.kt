package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.app.Dialog
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

class EditProfileDialogFragment : DialogFragment() {
private lateinit var toolBar: MaterialToolbar
private lateinit var uploadAvatarImageView: ImageView
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile_dialog, container, false)
        toolBar = view.findViewById(R.id.tb_edit_data)
        uploadAvatarImageView = view.findViewById(R.id.iv_upload_avatar)
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

    override fun getTheme() = R.style.FullScreenDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
        setCancelable(false)

    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }
    private fun handleSelectedImage(uri: Uri) {

    }
}
