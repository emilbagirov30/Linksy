package com.emil.linksy.presentation.ui

import android.app.Dialog
import android.content.Context
import com.emil.presentation.R

class LoadingDialog (private val context: Context): Dialog(context,R.style.LoadingDialog) {

    init {
        setContentView(R.layout.loading_dialog)
        setCancelable(false)
    }

}