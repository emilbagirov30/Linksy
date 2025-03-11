package com.emil.linksy.presentation.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emil.domain.usecase.room.ClearAllMessagesUseCase
import com.emil.domain.usecase.room.ClearChatsUseCase
import com.emil.linksy.adapters.SettingsAdapter
import com.emil.linksy.adapters.model.SettingItem
import com.emil.linksy.presentation.ui.auth.AuthActivity
import com.emil.linksy.presentation.ui.auth.PrivacyPolicyActivity
import com.emil.linksy.presentation.ui.navigation.profile.BlackListDialogFragment
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.TokenManager
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class CommonSettingsDialogFragment: DialogFragment() {
private lateinit var toolBar: MaterialToolbar
private lateinit var settingsRecyclerView: RecyclerView
    private var listener: UpdateDataListener? = null
    private val tokenManager: TokenManager by inject()
    private val clearChatsUseCase: ClearChatsUseCase by inject<ClearChatsUseCase> ()
    private val clearAllMessagesUseCase: ClearAllMessagesUseCase by inject<ClearAllMessagesUseCase>()
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.common_settings_dialog, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolBar = view.findViewById(R.id.tb_edit_data)
        settingsRecyclerView = view.findViewById(R.id.rv_settings)
        val privacyTextView = view.findViewById<MaterialTextView>(R.id.tv_privacy_link)
        val exitButton = view.findViewById<MaterialButton>(R.id.bt_exit)
        val settingsList = listOf(
            SettingItem(getString(R.string.app_settings)),
            SettingItem(getString(R.string.profile_settings)),
            SettingItem(getString(R.string.confidentiality)),
            SettingItem(getString(R.string.blacklist)),
        )

        settingsRecyclerView.layoutManager = LinearLayoutManager(context)
        settingsRecyclerView.adapter = SettingsAdapter(settingsList) { settingItem ->
            navigateToSettingDetail(settingItem)
        }
        toolBar.setNavigationOnClickListener {
            dialog?.dismiss()
        }

        exitButton.setOnClickListener {
            logoutUser()
        }

        privacyTextView.setOnClickListener {
            startActivity(Intent(requireActivity(), PrivacyPolicyActivity::class.java))
        }
    }

    override fun getTheme() = R.style.FullScreenDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }

    private fun navigateToSettingDetail(settingItem: SettingItem) {
        when (settingItem.title) {
            getString(R.string.profile_settings) -> {  ProfileSettingsDialogFragment().show(parentFragmentManager, "ProfileSettingsDialog")  }
            getString(R.string.confidentiality)  -> {ConfidentialityDialogFragment().show(parentFragmentManager, "ConfidentialityDialogFragment") }
            getString(R.string.blacklist)-> { BlackListDialogFragment().show(parentFragmentManager,"BlackListDialogFragment") }
            getString(R.string.app_settings)-> { AppSettingsDialogFragment().show(parentFragmentManager, "AppSettingsDialogFragment") }
        }
    }
    private fun logoutUser() {
        val sharedPref: SharedPreferences =
            requireContext().getSharedPreferences(Linksy.SHAREDPREF_MAIN_KEY, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(Linksy.SHAREDPREF_REMEMBER_KEY, false)
        editor.apply()
        tokenManager.clearTokens()
        CoroutineScope(Dispatchers.IO).launch {
            clearChatsUseCase.execute()
            clearAllMessagesUseCase.execute()
        }
        val authIntent = Intent(requireContext(), AuthActivity::class.java)
        authIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(authIntent)
    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.update()
    }
    fun setUpdateListener(listener: UpdateDataListener) {
        this.listener = listener
    }

    interface UpdateDataListener {
        fun update()
    }
}
