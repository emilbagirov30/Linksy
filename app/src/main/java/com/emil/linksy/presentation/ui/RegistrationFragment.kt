package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import com.emil.linksy.util.replaceFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.emil.linksy.util.BackgroundState
import com.emil.linksy.util.changeEditTextBackgroundColor
import com.emil.linksy.util.string
import com.emil.linksy.util.togglePasswordVisibility
import com.emil.linksy.presentation.viewmodel.RegistrationViewModel
import com.emil.linksy.util.hide
import com.emil.linksy.util.isValidEmail
import com.emil.linksy.util.show
import com.emil.presentation.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : Fragment() {

    private lateinit var loginButton: MaterialTextView
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var  passwordConfirmEditText: EditText
    private lateinit var signUpButton: MaterialButton
    private lateinit var changeInputTypePasswordButton: ImageView
    private lateinit var changeInputTypePasswordConfirmButton: ImageView
    private lateinit var emailExistTextView: MaterialTextView
    private lateinit var emailInvalidFormatTextView: MaterialTextView
    private lateinit var passwordMismatchTextView: MaterialTextView
    private lateinit var passwordShortTextView: MaterialTextView
    private val registrationViewModel: RegistrationViewModel by viewModel<RegistrationViewModel> ()
    private val TAG  = this.javaClass.simpleName
    private lateinit var bsDialog:ConfirmCodeBottomSheet
    companion object private var errorCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    @SuppressLint("MissingInflatedId", "ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)
        loginButton = view.findViewById(R.id.bt_login)
        usernameEditText = view.findViewById(R.id.et_username)
        emailEditText = view.findViewById(R.id.et_email)
        passwordEditText = view.findViewById(R.id.et_password)
        passwordConfirmEditText = view.findViewById(R.id.et_confirm_password)
        signUpButton = view.findViewById(R.id.bt_sign_up)
        changeInputTypePasswordButton = view.findViewById(R.id.iv_password_change_input_type)
        changeInputTypePasswordConfirmButton = view.findViewById(R.id.iv_password_confirm_change_input_type)
        emailExistTextView = view.findViewById (R.id.tv_error_email_exist)
        emailInvalidFormatTextView = view.findViewById (R.id.tv_error_isNotMail)
        passwordMismatchTextView = view.findViewById (R.id.tv_error_password_mismatch)
        passwordShortTextView = view.findViewById (R.id.tv_error_password_short)


        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { checkFieldsForEmptyValues() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        usernameEditText.addTextChangedListener(textWatcher)
        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
        passwordConfirmEditText.addTextChangedListener(textWatcher)
        loginButton.setOnClickListener { replaceFragment(LoginFragment()) }
        changeInputTypePasswordButton.setOnClickListener {
            togglePasswordVisibility(passwordEditText, changeInputTypePasswordButton)
        }
        changeInputTypePasswordConfirmButton.setOnClickListener {
            togglePasswordVisibility( passwordConfirmEditText, changeInputTypePasswordConfirmButton)
        }
        signUpButton.setOnClickListener {
            hideAllError()
            val username = usernameEditText.string()
            val email = emailEditText.string()
            val password = passwordEditText.string()
            val passwordConfirm = passwordConfirmEditText.string()

            isPasswordValid(password,passwordConfirm)
            isPasswordLengthValid(password)
            isEmailValid(email)

            if (errorCount==0) {
                registrationViewModel.register(username, email, password,
                    onAccepted = {
                                 bsDialog = ConfirmCodeBottomSheet(email)
                                 bsDialog.show(parentFragmentManager,bsDialog.tag)
                                 bsDialog.isCancelable = false
                                 },
                    onConflict = { emailExistTextView.show()
                        changeEditTextBackgroundColor(requireContext(), BackgroundState.ERROR, passwordEditText)
                                 },
                    onError = { Log.e(TAG,"Error") }
                )
            }
        }
        return view
    }


    private fun checkFieldsForEmptyValues() {
        val username = usernameEditText.string()
        val email = emailEditText.string()
        val password = passwordEditText.string()
        val confirmPassword =  passwordConfirmEditText.string()
        signUpButton.isEnabled = username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
    }

    private fun isPasswordValid(password: String, passwordConfirm: String) {
       if (password != passwordConfirm) {
            changeEditTextBackgroundColor(requireContext(), BackgroundState.ERROR, passwordEditText, passwordConfirmEditText)
            passwordMismatchTextView.show()
            errorCount++
        }
    }
    private fun isPasswordLengthValid(password: String) {
         if (password.length < 6) {
            changeEditTextBackgroundColor(requireContext(), BackgroundState.ERROR, passwordEditText)
            passwordShortTextView.show()
            errorCount++
        }
    }
    private fun isEmailValid(email: String) {
         if (!email.isValidEmail()) {
             changeEditTextBackgroundColor(requireContext(), BackgroundState.ERROR, emailEditText)
            emailInvalidFormatTextView.show()
             errorCount++
        }
    }
    private fun hideAllError (){
        errorCount=0
        passwordMismatchTextView.hide()
        passwordShortTextView.hide()
        emailInvalidFormatTextView.hide()
        emailExistTextView.hide()
        changeEditTextBackgroundColor(requireContext(),BackgroundState.DEFAULT,emailEditText,passwordEditText,passwordConfirmEditText)
    }
}
