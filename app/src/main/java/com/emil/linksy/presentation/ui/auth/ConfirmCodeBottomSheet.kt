package com.emil.linksy.presentation.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.emil.linksy.presentation.viewmodel.ConfirmCodeViewModel
import com.emil.linksy.util.BackgroundState
import com.emil.linksy.util.changeEditTextBackgroundColor
import com.emil.linksy.util.hide
import com.emil.linksy.util.replaceFragment
import com.emil.linksy.util.show
import com.emil.linksy.util.showToast
import com.emil.presentation.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmCodeBottomSheet: BottomSheetDialogFragment() {
    private lateinit var email: String

    companion object {
        private const val ARG_EMAIL = "email"

        fun newInstance(email: String): ConfirmCodeBottomSheet {
            val fragment = ConfirmCodeBottomSheet()
            val args = Bundle()
            args.putString(ARG_EMAIL, email)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = arguments?.getString(ARG_EMAIL) ?: ""
    }

    private lateinit var num1editText:EditText
    private lateinit var num2editText:EditText
    private lateinit var num3editText:EditText
    private lateinit var num4editText:EditText
    private lateinit var num5editText:EditText
    private lateinit var emailTextView:MaterialTextView
    private lateinit var invalidCodeTextView:MaterialTextView
    private lateinit var timerTextView:MaterialTextView
    private lateinit var resendButton:MaterialButton
    private lateinit var cancelButton:MaterialButton
    private lateinit var timer: CountDownTimer
    private var isTimerRunning = false
    private val startTimeInMillis = 60000L
    private lateinit var code:String
    private lateinit var editTexts:List<EditText>
    private lateinit var numList:Array<EditText>
    private val confirmCodeViewModel: ConfirmCodeViewModel by viewModel<ConfirmCodeViewModel> ()
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
         cancelButton = view.findViewById(R.id.bt_cancel)
         resendButton = view.findViewById(R.id.bt_send_new_code)
         timerTextView = view.findViewById(R.id.tv_timer)
         numList = arrayOf(num1editText,num2editText,num3editText,num4editText,num5editText)
         emailTextView.text = email
         editTexts = numList.toList()
        cancelButton.setOnClickListener {
            dialog?.dismiss()
        }
        resendButton.setOnClickListener {
             confirmCodeViewModel.resend(email, onError = { showToast(requireContext(),R.string.failed_connection) })
              startTimer()
        }
        setupEditTexts()
        startTimer()
         return view
    }


    private fun setupEditTexts() {
        editTexts.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) { checkAllFields() }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.isNullOrEmpty() && index > 0) { return }
                    if (!s.isNullOrEmpty() && index < editTexts.size - 1) {
                        editTexts[index + 1].requestFocus()
                    }
                }
            })
            editText.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    if (editText.text.isEmpty() && index > 0) {
                        editTexts[index - 1].requestFocus()
                        editTexts[index - 1].setSelection(editTexts[index - 1].text.length)
                        return@setOnKeyListener true
                    }
                }
                false
            }
        }
    }

    private fun checkAllFields() {
        val allFilled = editTexts.all { it.text.isNotEmpty() }
        if (allFilled) {
            hideAllError()
            code = editTexts.joinToString("") { it.text.toString() }
           confirmCodeViewModel.confirm(email,code,
                onSuccess = {replaceFragment(R.id.fl_fragment_container_auth, LoginFragment())
                             showToast(requireContext(), R.string.registration_successful)
                    dialog?.dismiss()
                            },
                onIncorrect = {invalidCodeTextView.show()
                    changeEditTextBackgroundColor(requireContext(),BackgroundState.ERROR,*numList)
                }, onError = { showToast(requireContext(), R.string.failed_connection) }
            )
        }
    }

    private fun startTimer() {
        resendButton.isEnabled = false
        resendButton.alpha = 0.1f
        timerTextView.show()
        timer = object : CountDownTimer(startTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimerText(millisUntilFinished)
            }
            override fun onFinish() {
                isTimerRunning = false
                timerFinishedAction()
            }
        }.start()
        isTimerRunning = true
    }
    @SuppressLint("DefaultLocale")
    private fun updateTimerText(millisUntilFinished: Long) {
        val seconds = (millisUntilFinished / 1000) % 60
        val minutes = (millisUntilFinished / 1000) / 60
        timerTextView.text = String.format("%02d:%02d", minutes, seconds)
    }
    private fun timerFinishedAction() {
        resendButton.isEnabled = true
        resendButton.alpha = 1f
        timer.cancel()
        isTimerRunning = false
        timerTextView.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isTimerRunning) {
            timer.cancel()
            isTimerRunning = false
        }
    }
    private fun hideAllError() {
        invalidCodeTextView.hide()
        changeEditTextBackgroundColor(requireContext(),BackgroundState.DEFAULT,*numList)
    }
}