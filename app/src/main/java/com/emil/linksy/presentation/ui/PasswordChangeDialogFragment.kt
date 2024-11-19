package com.emil.linksy.presentation.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.emil.linksy.presentation.viewmodel.PasswordChangeViewModel
import com.emil.linksy.presentation.viewmodel.ProfileManagementViewModel
import com.emil.linksy.util.BackgroundState
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.showToast
import com.emil.linksy.util.string
import com.emil.linksy.util.togglePasswordVisibility
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.koin.androidx.viewmodel.ext.android.viewModel

class PasswordChangeDialogFragment: DialogFragment() {
    private lateinit var toolBar: MaterialToolbar
    private lateinit var oldPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmNewPasswordEditText: EditText
    private lateinit var changeInputTypeOldPasswordButton: ImageView
    private lateinit var changeInputTypeNewPasswordButton: ImageView
    private lateinit var changeInputTypeConfirmNewPasswordButton: ImageView
    private lateinit var changeButton: MaterialButton
    private lateinit var oldPassword: String
    private lateinit var newPassword: String
    private lateinit var confirmNewPassword: String
    private lateinit var wrongPasswordTextView: MaterialTextView
    private lateinit var passwordMismatchTextView: MaterialTextView
    private lateinit var passwordShortTextView: MaterialTextView
    private lateinit var loading: LoadingDialog
    private lateinit var sharedPref: SharedPreferences
    private val passwordChangeViewModel: PasswordChangeViewModel by viewModel<PasswordChangeViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.password_change_dialog, container, false)
        oldPasswordEditText = view.findViewById(R.id.et_old_password)
        newPasswordEditText = view.findViewById(R.id.et_new_password)
        confirmNewPasswordEditText = view.findViewById(R.id.et_confirm_new_password)
        changeInputTypeOldPasswordButton = view.findViewById(R.id.iv_old_password_change_input_type)
        changeInputTypeNewPasswordButton = view.findViewById(R.id.iv_new_password_change_input_type)
        changeInputTypeConfirmNewPasswordButton =
            view.findViewById(R.id.iv_confirm_new_password_change_input_type)
        wrongPasswordTextView = view.findViewById(R.id.tv_error_password_wrong)
        passwordMismatchTextView = view.findViewById(R.id.tv_error_password_mismatch)
        passwordShortTextView = view.findViewById(R.id.tv_error_password_short)
        changeButton = view.findViewById(R.id.bt_change)
        toolBar = view.findViewById(R.id.tb_edit_data)
        toolBar.setNavigationOnClickListener { dialog?.dismiss() }
        sharedPref = requireContext().getSharedPreferences("TokenData", Context.MODE_PRIVATE)
        changeInputTypeOldPasswordButton.setOnClickListener {
            togglePasswordVisibility(oldPasswordEditText, changeInputTypeOldPasswordButton)
        }
        changeInputTypeNewPasswordButton.setOnClickListener {
            togglePasswordVisibility(newPasswordEditText, changeInputTypeNewPasswordButton)
        }
        changeInputTypeConfirmNewPasswordButton.setOnClickListener {
            togglePasswordVisibility(
                confirmNewPasswordEditText,
                changeInputTypeConfirmNewPasswordButton
            )
        }

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkFieldsForEmptyValues()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        oldPasswordEditText.addTextChangedListener(textWatcher)
        newPasswordEditText.addTextChangedListener(textWatcher)
        confirmNewPasswordEditText.addTextChangedListener(textWatcher)
        changeButton.setOnClickListener {
            hideAllError()
            oldPassword = oldPasswordEditText.string()
            newPassword = newPasswordEditText.string()
            confirmNewPassword = confirmNewPasswordEditText.string()
            val validPassword= isPasswordValid(newPassword,confirmNewPassword)
            val validLength = isPasswordLengthValid(newPassword)
            if(validPassword&&validLength){
                loading = LoadingDialog(requireContext())
                loading.show()
                val token = sharedPref.getString("ACCESS_TOKEN",null).toString()
               passwordChangeViewModel.changePassword(token = token,oldPassword =oldPassword,newPassword = newPassword, onSuccess = {
                   showToast(requireContext(), R.string.password_update)
                   oldPasswordEditText.setText("")
                   newPasswordEditText.setText("")
                   confirmNewPasswordEditText.setText("")
               }, onIncorrect = {wrongPasswordTextView.show()}, onError = {showToast(requireContext(), R.string.failed_connection)}, onEnd = {loading.dismiss()})
            }
        }
        return view
    }

    override fun getTheme() = R.style.FullScreenDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }

    private fun checkFieldsForEmptyValues() {
        oldPassword = oldPasswordEditText.string()
        newPassword = newPasswordEditText.string()
        confirmNewPassword = confirmNewPasswordEditText.string()
        changeButton.isEnabled =
            oldPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmNewPassword.isNotEmpty()
    }

    private fun isPasswordValid(password: String, passwordConfirm: String): Boolean {
        return if (password != passwordConfirm) {
            passwordMismatchTextView.show()
            false
        } else true

    }

    private fun isPasswordLengthValid(password: String): Boolean {
        return if (password.length < Linksy.PASSWORD_MIN_LENGTH) {
            passwordShortTextView.show()
            false
        } else true
    }
    private fun hideAllError(){
        wrongPasswordTextView.hide()
        passwordShortTextView.hide()
        passwordMismatchTextView.hide()
    }
}