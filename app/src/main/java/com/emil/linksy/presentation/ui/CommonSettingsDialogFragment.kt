package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emil.linksy.adapters.SettingsAdapter
import com.emil.linksy.adapters.model.SettingItem
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar

class CommonSettingsDialogFragment (private val profileFragment:ProfileFragment) : DialogFragment() {
private lateinit var toolBar: MaterialToolbar
private lateinit var settingsRecyclerView: RecyclerView
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.common_settings_dialog, container, false)
        toolBar = view.findViewById(R.id.tb_edit_data)
        settingsRecyclerView = view.findViewById(R.id.rv_settings)
        val settingsList = listOf(
            SettingItem(getString(R.string.app_settings)),
            SettingItem(getString(R.string.profile_settings)),
            SettingItem(getString(R.string.сonfidentiality)),
            SettingItem(getString(R.string.blacklist)),
           )

        settingsRecyclerView.layoutManager = LinearLayoutManager(context)
        settingsRecyclerView.adapter = SettingsAdapter(settingsList) { settingItem ->
            navigateToSettingDetail(settingItem)
        }
        toolBar.setNavigationOnClickListener {
            profileFragment.fetchData()
            dialog?.dismiss()
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

    private fun navigateToSettingDetail(settingItem: SettingItem) {
        when (settingItem.title) {
            getString(R.string.profile_settings) -> {  ProfileSettingsDialogFragment().show(parentFragmentManager, "ProfileSettingsDialog")  }
            getString(R.string.сonfidentiality)  -> { }
            getString(R.string.blacklist)-> {  }
            getString(R.string.app_settings)-> {}

        }
    }
}
