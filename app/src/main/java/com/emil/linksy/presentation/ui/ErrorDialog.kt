package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.widget.ImageButton
import com.emil.presentation.R
import com.google.android.material.textview.MaterialTextView

@SuppressLint("MissingInflatedId")
    class ErrorDialog (private val context: Context, private val messageResId:Int): Dialog(context, R.style.RoundedDialog) {
    private var closeImageButton:ImageButton
    init {
        setContentView(R.layout.error_dialog)
        closeImageButton = findViewById(R.id.ib_close)
        val messageTextView = findViewById<MaterialTextView>(R.id.tv_message)
        messageTextView.text = context.getString(messageResId)

        setCancelable(false)
        show()
    }
    fun close(action:()->Unit={}){
        closeImageButton.setOnClickListener {
            dismiss()
            action()
        }
    }

}