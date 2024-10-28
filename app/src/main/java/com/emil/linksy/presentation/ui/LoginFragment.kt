package com.emil.linksy.presentation.ui
import com.emil.linksy.util.replaceFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emil.presentation.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView


class LoginFragment : Fragment() {
    private lateinit var forgotLink:MaterialTextView
    private lateinit var createAccountButton:MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        forgotLink = view.findViewById(R.id.tv_forgot_passsword)
        createAccountButton =  view.findViewById<MaterialButton>(R.id.bt_create_account)
        createAccountButton.setOnClickListener { replaceFragment(RegistrationFragment()) }
        forgotLink.setOnClickListener { replaceFragment(PasswordRecoveryFragment()) }
        return  view
    }


}