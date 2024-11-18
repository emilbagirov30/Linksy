package com.emil.linksy.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar

class PasswordChangeDialogFragment: DialogFragment() {
    private lateinit var toolBar: MaterialToolbar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.password_change_dialog, container, false)
        toolBar = view.findViewById(R.id.tb_edit_data)
        toolBar.setNavigationOnClickListener { dialog?.dismiss() }
        return view
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