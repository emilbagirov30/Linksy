package com.emil.linksy.presentation.ui

import android.app.Dialog
import android.content.Context
import com.emil.presentation.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class ActionDialog (private val context: Context):Dialog(context, R.style.RoundedDialog) {
    private val titleTextView:MaterialTextView
    private val cancelButton:MaterialButton
    private val confirmButton:MaterialButton
    init {
        setContentView(R.layout.dialog_action)
        setCancelable(false)
        show()
        titleTextView =findViewById(R.id.tv_title)
        cancelButton = findViewById(R.id.bt_cancel)
        confirmButton =findViewById(R.id.bt_confirm)
        cancelButton.setOnClickListener {
            dismiss()
        }
    }

    fun setTitle (title:String){
        titleTextView.text = title
    }

    fun setAction(action:()->Unit){
        confirmButton.setOnClickListener {
            action()
        }
    }

}