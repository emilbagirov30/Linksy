package com.emil.linksy.presentation.ui

import com.emil.linksy.util.replaceFragment
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.emil.linksy.presentation.custom_view.CustomProgressBar
import com.emil.linksy.presentation.viewmodel.RecoveryPasswordViewModel
import com.emil.linksy.util.BackgroundState
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.changeEditTextBackgroundColor
import com.emil.linksy.util.hide
import com.emil.linksy.util.hideKeyboard
import com.emil.linksy.util.isValidEmail
import com.emil.linksy.util.show
import com.emil.linksy.util.showToast
import com.emil.linksy.util.string
import com.emil.linksy.util.togglePasswordVisibility
import com.emil.presentation.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.koin.androidx.viewmodel.ext.android.viewModel


class PasswordRecoveryFragment : Fragment() {
    private lateinit var  backButton:MaterialButton
    private lateinit var  emailEditText: EditText
    private lateinit var  continueButton: MaterialButton
    private lateinit var  emailInvalidFormatTextView: MaterialTextView
    private lateinit var  userNotFoundTextView: MaterialTextView
    private lateinit var loading: CustomProgressBar
    private lateinit var email: String
    private lateinit var  requestPasswordChangeLinerLayout: LinearLayout
    private lateinit var  confirmPasswordChangeLinerLayout: LinearLayout
    private lateinit var  emailTextView:MaterialTextView
    private lateinit var  codeEditText: EditText
    private lateinit var  newPasswordEditText: EditText
    private lateinit var  passwordConfirmEditText: EditText
    private lateinit var  changeInputTypeNewPasswordButton: ImageView
    private lateinit var  changeInputTypePasswordConfirmButton: ImageView
    private lateinit var  changePasswordButton: MaterialButton
    private lateinit var  passwordMismatchTextView: MaterialTextView
    private lateinit var  passwordShortTextView: MaterialTextView
    private lateinit var  invalidCodeTextView:MaterialTextView
    private lateinit var code:String
    private lateinit var password:String
    private lateinit var confirmPassword:String
    companion object private var errorCount = 0
    var isValidEmail:Boolean = false
    private val recoveryPasswordViewModel: RecoveryPasswordViewModel by viewModel<RecoveryPasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_password_recovery, container, false)
        backButton = view.findViewById(R.id.bt_back)
        emailEditText = view.findViewById(R.id.et_email)
        continueButton = view.findViewById(R.id.bt_continue)
        emailInvalidFormatTextView = view.findViewById(R.id.tv_error_isNotMail)
        userNotFoundTextView = view.findViewById(R.id.tv_user_not_found)
        loading = view.findViewById(R.id.cpb_loading)
        requestPasswordChangeLinerLayout = view.findViewById (R.id.ll_request_password_change)
        confirmPasswordChangeLinerLayout = view.findViewById (R.id.ll_confirm_password_change)
        emailTextView = view.findViewById(R.id.tv_email)
        codeEditText =  view.findViewById(R.id.et_code)
        newPasswordEditText = view.findViewById(R.id.et_new_password)
        passwordConfirmEditText = view.findViewById(R.id.et_confirm_password)
        changeInputTypeNewPasswordButton = view.findViewById(R.id.iv_password_change_input_type)
        changeInputTypePasswordConfirmButton = view.findViewById(R.id.iv_password_confirm_change_input_type)
        changePasswordButton = view.findViewById(R.id.bt_change)
        passwordMismatchTextView = view.findViewById(R.id.tv_error_password_mismatch)
        passwordShortTextView = view.findViewById(R.id.tv_error_password_short)
        invalidCodeTextView = view.findViewById(R.id.tv_invalid_code)




        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                userNotFoundTextView.hide()
                isValidEmail = emailEditText.string().isValidEmail()
                if (isValidEmail){
                    continueButton.isEnabled = true
                    emailInvalidFormatTextView.hide()
                }else{
                    emailInvalidFormatTextView.show()
                    continueButton.isEnabled = false

                }

            }

        })

    continueButton.setOnClickListener {
        hideKeyboard(requireContext(),view)
    loading.visible()
    email = emailEditText.string()
    recoveryPasswordViewModel.requestPasswordChange(email,
        onSuccess = {
            requestPasswordChangeLinerLayout.hide()
            confirmPasswordChangeLinerLayout.show()
            emailTextView.text = email

                    },
        onIncorrect = { userNotFoundTextView.show()},
        onError = {showToast(requireContext(), R.string.failed_connection)},
        onEnd = {loading.gone()}
        )
}
        backButton.setOnClickListener { replaceFragment(LoginFragment()) }



        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { checkFieldsForEmptyValues() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        codeEditText.addTextChangedListener(textWatcher)
        newPasswordEditText.addTextChangedListener(textWatcher)
        passwordConfirmEditText.addTextChangedListener(textWatcher)


        changePasswordButton.setOnClickListener {
            hideAllError()
            hideKeyboard(requireContext(), view)
            code = codeEditText.string()
            password = newPasswordEditText.string()
            confirmPassword = passwordConfirmEditText.string()

            isPasswordValid(password, confirmPassword)
            isPasswordLengthValid(password)
            isValidCodeLength(code)

            if (errorCount==0) {
                loading.visible()
                recoveryPasswordViewModel.confirmPasswordChange(code,
                    email,
                    password,
                    onSuccess = {requestPasswordChangeLinerLayout.show()
                                 confirmPasswordChangeLinerLayout.hide()
                                 showToast(requireContext(), R.string.password_update)
                                 emailEditText.setText("")
                                },
                    onIncorrect = {
                        invalidCodeTextView.text = getString(R.string.invalid_confirmation_code)
                        changeEditTextBackgroundColor(requireContext(), BackgroundState.ERROR, codeEditText)
                        invalidCodeTextView.show()

                    },
                    onError = { showToast(requireContext(), R.string.failed_connection) },
                    onEnd = { loading.gone() }


                )
            }

        }
        changeInputTypeNewPasswordButton.setOnClickListener {
            togglePasswordVisibility(newPasswordEditText, changeInputTypeNewPasswordButton)
        }
        changeInputTypePasswordConfirmButton.setOnClickListener {
            togglePasswordVisibility(passwordConfirmEditText, changeInputTypePasswordConfirmButton)
        }

        return view
    }
    private fun checkFieldsForEmptyValues() {
        code = codeEditText.string()
        password = newPasswordEditText.string()
        confirmPassword = passwordConfirmEditText.string()
        changePasswordButton.isEnabled =
           password.isNotEmpty() && confirmPassword.isNotEmpty() && code.isNotEmpty()
    }
    private fun isPasswordValid(password: String, passwordConfirm: String) {
        if (password != passwordConfirm) {
            changeEditTextBackgroundColor(requireContext(),BackgroundState.ERROR,newPasswordEditText,passwordConfirmEditText)
            passwordMismatchTextView.show()
            errorCount++
        }
    }

    private fun isValidCodeLength (code:String){
        if (code.length != Linksy.CODE_MIN_LENGTH){
            errorCount++
            invalidCodeTextView.text =  getString(R.string.invalid_code_length)
            invalidCodeTextView.show()
            changeEditTextBackgroundColor(requireContext(),BackgroundState.ERROR,codeEditText)
        }
    }
    private fun isPasswordLengthValid(password: String) {
        if (password.length < Linksy.PASSWORD_MIN_LENGTH) {
            changeEditTextBackgroundColor(requireContext(),BackgroundState.ERROR,newPasswordEditText,passwordConfirmEditText)
            passwordShortTextView.show()
            errorCount++
        }
    }
    private fun hideAllError (){
        errorCount = 0
        changeEditTextBackgroundColor(requireContext(),BackgroundState.DEFAULT,newPasswordEditText,passwordConfirmEditText,codeEditText)
        passwordMismatchTextView.hide()
        passwordShortTextView.hide()
        invalidCodeTextView.hide()
    }
}