package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.content.Intent
import com.emil.linksy.util.replaceFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.emil.linksy.presentation.custom_view.CustomProgressBar
import com.emil.linksy.util.BackgroundState
import com.emil.linksy.util.changeEditTextBackgroundColor
import com.emil.linksy.util.string
import com.emil.linksy.util.togglePasswordVisibility
import com.emil.linksy.presentation.viewmodel.RegistrationViewModel
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.hide
import com.emil.linksy.util.hideKeyboard
import com.emil.linksy.util.isValidEmail
import com.emil.linksy.util.show
import com.emil.linksy.util.showToast
import com.emil.presentation.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegistrationFragment : Fragment() {

    private lateinit var loginButton: MaterialTextView
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var passwordConfirmEditText: EditText
    private lateinit var signUpButton: MaterialButton
    private lateinit var changeInputTypePasswordButton: ImageView
    private lateinit var changeInputTypePasswordConfirmButton: ImageView
    private lateinit var emailExistTextView: MaterialTextView
    private lateinit var emailInvalidFormatTextView: MaterialTextView
    private lateinit var passwordMismatchTextView: MaterialTextView
    private lateinit var passwordShortTextView: MaterialTextView
    private lateinit var loading: LoadingDialog
    private val registrationViewModel: RegistrationViewModel by viewModel<RegistrationViewModel>()
    private val TAG = this.javaClass.simpleName
    private lateinit var bsDialog: ConfirmCodeBottomSheet
    companion object private var errorCount = 0
    private lateinit var username:String
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var confirmPassword:String

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
        emailExistTextView = view.findViewById(R.id.tv_error_email_exist)
        emailInvalidFormatTextView = view.findViewById(R.id.tv_error_isNotMail)
        passwordMismatchTextView = view.findViewById(R.id.tv_error_password_mismatch)
        passwordShortTextView = view.findViewById(R.id.tv_error_password_short)
        registrationViewModel.username.observe(viewLifecycleOwner) { text ->
            if (usernameEditText.text.toString() != text)
                usernameEditText.setText(text)

        }
        registrationViewModel.email.observe(viewLifecycleOwner) { text ->
            if (emailEditText.text.toString() != text)
                emailEditText.setText(text)

        }
        registrationViewModel.password.observe(viewLifecycleOwner) { text ->
            if (passwordEditText.text.toString() != text)
                passwordEditText.setText(text)

        }
        registrationViewModel.passwordConfirm.observe(viewLifecycleOwner) { text ->
            if (passwordConfirmEditText.text.toString() != text)
                passwordConfirmEditText.setText(text)

        }
        val switchingToUserActivity = Intent(activity, UserActivity::class.java)
        startActivity(switchingToUserActivity)
        registrationViewModel.errorStates.observe(viewLifecycleOwner) { errorStates ->
            if (errorStates["emailExist"] == true) emailExistTextView.show() else emailExistTextView.hide()
            if (errorStates["emailInvalidFormat"] == true) emailInvalidFormatTextView.show() else emailInvalidFormatTextView.hide()
            if (errorStates["passwordMismatch"] == true) passwordMismatchTextView.show() else passwordMismatchTextView.hide()
            if (errorStates["passwordShort"] == true) passwordShortTextView.show() else passwordShortTextView.hide()
        }

        registrationViewModel.backgroundStates.observe(viewLifecycleOwner) { states ->
            changeEditTextBackgroundColor(
                requireContext(), states["username"] ?: BackgroundState.DEFAULT, usernameEditText
            )
            changeEditTextBackgroundColor(
                requireContext(), states["email"] ?: BackgroundState.DEFAULT, emailEditText
            )
            changeEditTextBackgroundColor(
                requireContext(), states["password"] ?: BackgroundState.DEFAULT, passwordEditText
            )
            changeEditTextBackgroundColor(
                requireContext(), states["passwordConfirm"] ?: BackgroundState.DEFAULT, passwordConfirmEditText
            )
        }
        fun addListener(et: EditText, saveLiveData: () -> Unit) {
            val textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    checkFieldsForEmptyValues()
                    saveLiveData()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }
            et.addTextChangedListener(textWatcher)
        }

        addListener(usernameEditText, saveLiveData = {
            registrationViewModel.setUsername(usernameEditText.string())
        })
        addListener(emailEditText, saveLiveData = {
            registrationViewModel.setEmail(emailEditText.string())
        })
        addListener(passwordEditText, saveLiveData = {
            registrationViewModel.setPassword(passwordEditText.string())
        })
        addListener(passwordConfirmEditText, saveLiveData = {
            registrationViewModel.setPasswordConfirm(passwordConfirmEditText.string())
        })

        loginButton.setOnClickListener { replaceFragment(R.id.fl_fragment_container_auth,LoginFragment()) }
        changeInputTypePasswordButton.setOnClickListener {
            togglePasswordVisibility(passwordEditText, changeInputTypePasswordButton)
        }
        changeInputTypePasswordConfirmButton.setOnClickListener {
            togglePasswordVisibility(passwordConfirmEditText, changeInputTypePasswordConfirmButton)
        }
        signUpButton.setOnClickListener {
            hideAllError()
            hideKeyboard(requireContext(), view)
            username = usernameEditText.string()
            email = emailEditText.string()
            password = passwordEditText.string()
            confirmPassword = passwordConfirmEditText.string()

            isPasswordValid(password, confirmPassword)
            isPasswordLengthValid(password)
            isEmailValid(email)

            if (errorCount == 0) {
                loading = LoadingDialog(requireContext())
                loading.show()
                registrationViewModel.register(username, email, password,
                    onAccepted = {
                        bsDialog = ConfirmCodeBottomSheet.newInstance(email)
                        bsDialog.show(parentFragmentManager, bsDialog.tag)
                        bsDialog.isCancelable = false
                    },
                    onConflict = {
                        registrationViewModel.setErrorState("emailExist", true)
                        registrationViewModel.setBackgroundState("email", BackgroundState.ERROR)
                    },
                    onError = { showToast(requireContext(), R.string.failed_connection) },
                    onEnd = { loading.dismiss() }

                )

            }
        }
        return view
    }


    private fun checkFieldsForEmptyValues() {
        username = usernameEditText.string()
        email = emailEditText.string()
        password = passwordEditText.string()
        confirmPassword = passwordConfirmEditText.string()
        signUpButton.isEnabled =
            username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
    }

    private fun isEmailValid(email: String) {
        if (!email.isValidEmail()) {
            registrationViewModel.setErrorState("emailInvalidFormat", true)
            registrationViewModel.setBackgroundState("email", BackgroundState.ERROR)
            errorCount++
        } else registrationViewModel.setErrorState("emailInvalidFormat", false)

    }

    private fun isPasswordValid(password: String, passwordConfirm: String) {
        if (password != passwordConfirm) {
            registrationViewModel.setErrorState("passwordMismatch", true)
            registrationViewModel.setBackgroundState("password", BackgroundState.ERROR)
            registrationViewModel.setBackgroundState("passwordConfirm", BackgroundState.ERROR)
            errorCount++
        } else registrationViewModel.setErrorState("passwordMismatch", false)

    }

    private fun isPasswordLengthValid(password: String) {
        if (password.length < Linksy.PASSWORD_MIN_LENGTH) {
            registrationViewModel.setErrorState("passwordShort", true)
            registrationViewModel.setBackgroundState("password", BackgroundState.ERROR)
            registrationViewModel.setBackgroundState("passwordConfirm", BackgroundState.ERROR)
            errorCount++
        } else registrationViewModel.setErrorState("passwordShort", false)

    }

    private fun hideAllError() {
        errorCount = 0
        registrationViewModel.setErrorState("emailInvalidFormat", false)
        registrationViewModel.setErrorState("emailExist", false)
        registrationViewModel.setErrorState("passwordMismatch", false)
        registrationViewModel.setErrorState("passwordShort", false)
        registrationViewModel.setBackgroundState("username", BackgroundState.DEFAULT)
        registrationViewModel.setBackgroundState("email", BackgroundState.DEFAULT)
        registrationViewModel.setBackgroundState("password", BackgroundState.DEFAULT)
        registrationViewModel.setBackgroundState("passwordConfirm", BackgroundState.DEFAULT)
    }
}
