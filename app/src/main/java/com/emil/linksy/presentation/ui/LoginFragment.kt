package com.emil.linksy.presentation.ui
import com.emil.linksy.util.replaceFragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import com.emil.linksy.presentation.custom_view.CustomProgressBar
import com.emil.linksy.presentation.viewmodel.LoginViewModel
import com.emil.linksy.util.Linksy
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


class LoginFragment : Fragment() {
    private lateinit var forgotLink:MaterialTextView
    private lateinit var createAccountButton:MaterialButton
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton:MaterialButton
    private lateinit var rememberCheckBox:CheckBox
    private lateinit var changeInputTypePasswordButton: ImageView
    private lateinit var emailInvalidFormatTextView: MaterialTextView
    private lateinit var passwordShortTextView: MaterialTextView
    private lateinit var loading: CustomProgressBar
    private val loginViewModel: LoginViewModel by viewModel<LoginViewModel>()
    private val TAG = this.javaClass.simpleName
    private lateinit var email:String
    private lateinit var password:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        forgotLink = view.findViewById(R.id.tv_forgot_passsword)
        createAccountButton =  view.findViewById(R.id.bt_create_account)
        emailEditText = view.findViewById(R.id.et_email)
        passwordEditText = view.findViewById(R.id.et_password)
        changeInputTypePasswordButton = view.findViewById(R.id.iv_password_change_input_type)
        loginButton =  view.findViewById(R.id.bt_login)
        rememberCheckBox = view.findViewById(R.id.cb_remember)
        emailInvalidFormatTextView = view.findViewById(R.id.tv_error_isNotMail)
        passwordShortTextView = view.findViewById(R.id.tv_error_password_short)
        loading = view.findViewById(R.id.cpb_loading)
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { checkFieldsForValidValues() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)

        changeInputTypePasswordButton.setOnClickListener {
            togglePasswordVisibility(passwordEditText, changeInputTypePasswordButton)
        }


        loginButton.setOnClickListener {
            loading.visible()
            hideAllError()
            hideKeyboard(requireContext(), view)
            val email = emailEditText.string()
            val password = passwordEditText.string()
            loginViewModel.login(
                email = email, password = password, onSuccess = {Log.i(TAG,"Sucess")},
                onIncorrect = {showToast(requireContext(), R.string.user_not_found)},
                onError =  { showToast(requireContext(), R.string.failed_connection) },
                onEnd = { loading.gone() })
        }
        createAccountButton.setOnClickListener { replaceFragment(RegistrationFragment()) }
        forgotLink.setOnClickListener { replaceFragment(PasswordRecoveryFragment()) }
        return view
    }
    private fun checkFieldsForValidValues() {
        email = emailEditText.string()
        password = passwordEditText.string()

        fun checkPassword(password: String) {
            when {
                password.length >= Linksy.PASSWORD_MIN_LENGTH -> passwordShortTextView.hide()
                password.isNotEmpty() -> passwordShortTextView.show()
            }
        }
        fun checkEmail(email: String) {
            when {
                email.isValidEmail() -> emailInvalidFormatTextView.hide()
                email.isNotEmpty() -> emailInvalidFormatTextView.show()
            }
        }
        checkPassword(password)
        checkEmail(email)
        loginButton.isEnabled = email.isValidEmail() && password.length >= Linksy.PASSWORD_MIN_LENGTH
    }
    private fun hideAllError() {
        passwordShortTextView.hide()
        emailInvalidFormatTextView.hide()
    }
}