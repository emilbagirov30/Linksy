package com.emil.linksy.presentation.ui.navigation.chat

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import com.emil.linksy.presentation.viewmodel.MessageViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.string
import com.emil.presentation.R
import com.google.android.material.button.MaterialButton

@SuppressLint("WrongViewCast")
class EditMessageDialog private constructor(private val context: Context,private val messageId: Long, private val messageText: String,
                                            private val tokenManager: TokenManager,private val messageViewModel: MessageViewModel) : Dialog(context, R.style.RoundedDialog) {

                                                private val sendButton:ImageButton
                                                private val messageEditText:EditText
    companion object {
        fun newInstance(context: Context, messageId: Long,message: String,tokenManager: TokenManager,messageViewModel: MessageViewModel): EditMessageDialog {
            return EditMessageDialog(context,messageId, message,tokenManager, messageViewModel)
        }
    }

    init {
        setContentView(R.layout.edit_message_dialog)
        show()
       messageEditText = findViewById(R.id.et_message)
       sendButton = findViewById(R.id.ib_send)
        messageEditText?.setText(messageText)
        messageEditText.addTextChangedListener{updateButtonState()}
        sendButton?.setOnClickListener {
            it.anim()
            val updatedMessage = messageEditText.string()
            messageViewModel.editMessage(tokenManager.getAccessToken(),messageId,updatedMessage, onSuccess = { dismiss()})
        }
    }

   private fun updateButtonState (){
        val updatedMessage = messageEditText.string()
         if (updatedMessage!=messageText)
             sendButton.show()
       else sendButton.hide()
    }
}
