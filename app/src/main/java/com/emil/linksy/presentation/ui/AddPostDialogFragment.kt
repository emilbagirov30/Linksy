package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.emil.linksy.presentation.viewmodel.PostViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.string
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddPostDialogFragment: DialogFragment() {

    private lateinit var toolBar: MaterialToolbar
    private val postViewModel: PostViewModel by viewModel<PostViewModel>()
    private lateinit var postEditText: EditText
    private lateinit var publishButton: ImageButton
    private val tokenManager: TokenManager by inject()
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_post_dialog, container, false)
        postEditText = view.findViewById(R.id.et_post)
        publishButton = view.findViewById(R.id.ib_publish)
        toolBar = view.findViewById(R.id.tb_edit_data)
        toolBar.setNavigationOnClickListener { dialog?.dismiss() }
        publishButton.setOnClickListener {
            val text = postEditText.string()
            val token = tokenManager.getAccessToken()
            postViewModel.publishPost(token, onSuccess = {dialog?.dismiss()},text)
        }


        return  view
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