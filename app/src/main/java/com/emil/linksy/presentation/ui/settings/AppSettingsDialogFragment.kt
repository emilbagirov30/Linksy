package com.emil.linksy.presentation.ui.settings

import android.annotation.SuppressLint
import android.content.Context

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip



class AppSettingsDialogFragment (): DialogFragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.app_settings_dialog, container, false)
        val toolBar = view.findViewById<MaterialToolbar>(R.id.tb)
        val applyButton = view.findViewById<MaterialButton>(R.id.bt_apply)
        toolBar.setNavigationOnClickListener {
            dialog?.dismiss()
        }
        val sharedPref =requireContext().getSharedPreferences("appData", Context.MODE_PRIVATE)
        val language = sharedPref.getString("language", "en").toString()
        val languages = resources.getStringArray(R.array.languages_array)
        val spinner = view.findViewById<Spinner>(R.id.language_spinner)
        var selectedLanguage = language
        val chipLight = view.findViewById<Chip>(R.id.chip_light)
        val chipDark = view.findViewById<Chip>(R.id.chip_dark)
        var currentTheme = sharedPref.getString("theme", "light")
        when (currentTheme) {
            "light" ->  chipLight.isChecked = true
            "dark" ->  chipDark.isChecked = true
        }



        chipLight.setOnClickListener {
          currentTheme = "light"
        }

        chipDark.setOnClickListener {
            currentTheme = "dark"

        }
        applyButton.setOnClickListener {
            sharedPref.edit().putString("theme", currentTheme).apply()
            when (currentTheme){
                "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            if (selectedLanguage != language) {
                with(sharedPref.edit()) {
                    putString("language", selectedLanguage)
                    apply()
                }

            }

        }

        ArrayAdapter.createFromResource(requireContext(), R.array.languages_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        val defaultLanguageIndex = when (language) {
            "ru" -> languages.indexOf("Русский")
            "en" -> languages.indexOf("English")
            else -> 0
        }
        spinner.setSelection(defaultLanguageIndex)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedLanguage = when (position) {
                    1 -> "ru"
                    0 -> "en"
                    else -> "en"
                }
            }override fun onNothingSelected(parent: AdapterView<*>) {}
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

}