package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.emil.linksy.presentation.viewmodel.ConfirmViewModel
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmCodeBottomSheet (private var email:String): BottomSheetDialogFragment() {
    private lateinit var num1editText:EditText
    private lateinit var num2editText:EditText
    private lateinit var num3editText:EditText
    private lateinit var num4editText:EditText
    private lateinit var num5editText:EditText
    private lateinit var emailTextView:MaterialTextView
    private lateinit var invalidCodeTextView:MaterialTextView
    private lateinit var sendButton:MaterialButton
    private lateinit var code:String
    private lateinit var editTexts:List<EditText>
    private val registrationViewModel: ConfirmViewModel by viewModel<ConfirmViewModel> ()
    private val TAG  = this.javaClass.simpleName

    override fun getTheme() = R.style.BottomSheetDialogTheme

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.bottom_sheet_confirm_code,  container, false)
        dialog?.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val layoutParams = it.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                it.layoutParams = layoutParams
            }
        }

         num1editText = view.findViewById(R.id.et_num1)
         num2editText = view.findViewById(R.id.et_num2)
         num3editText = view.findViewById(R.id.et_num3)
         num4editText = view.findViewById(R.id.et_num4)
         num5editText = view.findViewById(R.id.et_num5)
         emailTextView =view.findViewById(R.id.tv_email)
       invalidCodeTextView = view.findViewById(R.id.tv_invalid_code)
         sendButton = view.findViewById(R.id.bt_send)
         emailTextView.text = email
         editTexts = listOf(num1editText, num2editText, num3editText, num4editText, num5editText)
         setupEditTexts()
         sendButton.setOnClickListener {
registrationViewModel.confirm(email,code,
    onSuccess = {Log.i(TAG,"Sucess")},
    onIncorrect = {invalidCodeTextView.show()} ,
    onError = { Log.e(TAG,"Error") }
    )
        }
        return view
    }


    private fun setupEditTexts() {
        editTexts.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    checkAllFields()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.isNullOrEmpty() && index > 0) {
                        editTexts[index - 1].requestFocus()
                    }
                    if (!s.isNullOrEmpty() && index < editTexts.size - 1) {
                        editTexts[index + 1].requestFocus()
                    }
                }
            })
        }
    }


    private fun checkAllFields() {
        val allFilled = editTexts.all { it.text.isNotEmpty() }
        if (allFilled) {
            code = editTexts.joinToString("") { it.text.toString() }
            sendButton.isEnabled = true
        } else {
            sendButton.isEnabled = false
        }
    }
}