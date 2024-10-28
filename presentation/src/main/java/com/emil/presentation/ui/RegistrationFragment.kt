package com.emil.presentation.ui

import android.annotation.SuppressLint
import com.emil.presentation.R
import com.emil.presentation.util.replaceFragment
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
import androidx.lifecycle.ViewModelProvider
import com.emil.presentation.util.BackgroundState
import com.emil.presentation.util.changeEditTextBackgroundColor
import com.emil.presentation.util.string
import com.emil.presentation.util.togglePasswordVisibility
import com.emil.presentation.viewmodel.RegistrationViewModel
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
    private val TAG  = this.javaClass.simpleName
    private val registrationViewModel: RegistrationViewModel by viewModel<RegistrationViewModel> ()


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
            if (passwordEditText.string() != passwordConfirmEditText.string()) {
                changeEditTextBackgroundColor(
                    requireContext(),
                    BackgroundState.ERROR,
                    passwordEditText,
                    passwordConfirmEditText
                )
            } else {
                val username = usernameEditText.string()
                val email = emailEditText.string()
                val password = passwordEditText.string()
                registrationViewModel.register(username, email, password,
                    onSuccess = { Log.i(TAG,"Sucess") },
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
}
