package com.emil.presentation.ui

import com.emil.presentation.R
import com.emil.presentation.utils.replaceFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.emil.presentation.utils.string
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class RegistrationFragment : Fragment() {

    private lateinit var loginButton: MaterialTextView
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmEditText: EditText
    private lateinit var signUpButton: MaterialButton
    private lateinit var showPasswordButton: ImageView
    private lateinit var showPasswordConfirmButton: ImageView
    private lateinit var hidePasswordButton: ImageView
    private lateinit var hidePasswordConfirmButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)
        loginButton = view.findViewById(R.id.bt_login)
        usernameEditText = view.findViewById(R.id.et_username)
        emailEditText = view.findViewById(R.id.et_email)
        passwordEditText = view.findViewById(R.id.et_password)
        confirmEditText = view.findViewById(R.id.et_confirm_password)
        signUpButton = view.findViewById(R.id.bt_sign_up)
        showPasswordButton = view.findViewById(R.id.iv_password_show)
        showPasswordConfirmButton = view.findViewById(R.id.iv_passwordc_show)
        hidePasswordButton = view.findViewById(R.id.iv_password_hide)
        hidePasswordConfirmButton = view.findViewById(R.id.iv_passwordc_hide)


        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { checkFieldsForEmptyValues() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        usernameEditText.addTextChangedListener(textWatcher)
        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
        confirmEditText.addTextChangedListener(textWatcher)
        loginButton.setOnClickListener { replaceFragment(LoginFragment()) }
        return view
    }


    private fun checkFieldsForEmptyValues() {
        val username = usernameEditText.string()
        val email = emailEditText.string()
        val password = passwordEditText.string()
        val confirmPassword = confirmEditText.string()
        signUpButton.isEnabled = username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
    }
}
