package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import com.emil.domain.model.ReportRequest
import com.emil.linksy.presentation.viewmodel.PeopleViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.string
import com.emil.presentation.R

@SuppressLint("WrongViewCast")
class ReportDialog private constructor(private val context: Context,
                                       private val userId: Long?= null,
                                       private val channelId: Long? = null,
                                       private val tokenManager: TokenManager,
                                       private val peopleViewModel: PeopleViewModel
) : Dialog(context, R.style.RoundedDialog) {

    private val sendButton: ImageButton
    private val reasonEditText: EditText
    companion object {
        fun newInstance(context: Context, userId: Long?, channelId: Long?, tokenManager: TokenManager,peopleViewModel: PeopleViewModel): ReportDialog {
            return ReportDialog(context =context, userId =userId,channelId=channelId, tokenManager =tokenManager, peopleViewModel = peopleViewModel)
        }
    }

    init {
        setContentView(R.layout.report_dialog)
        show()
        reasonEditText = findViewById(R.id.et_reason)
        sendButton = findViewById(R.id.ib_send)
        reasonEditText.addTextChangedListener{updateButtonState()}
        sendButton?.setOnClickListener {
            it.anim()
            peopleViewModel.sendReport(tokenManager.getAccessToken(), ReportRequest(userId, channelId,reasonEditText.string()), onSuccess = {dismiss()})
        }
    }

    private fun updateButtonState (){
        val reason =reasonEditText.string()
        if (reason.length>5) sendButton.show()
        else sendButton.hide()
    }
}