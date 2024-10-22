package com.emil.presentation.ui
import com.emil.presentation.R
import com.emil.presentation.utils.replaceFragment
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton


class PasswordRecoveryFragment : Fragment() {
private lateinit var  backButton:MaterialButton

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
        backButton.setOnClickListener { replaceFragment(LoginFragment()) }
        return view
    }


}