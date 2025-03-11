package com.emil.linksy.presentation.ui.auth
import com.emil.linksy.util.LocaleManager

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.emil.linksy.presentation.ui.navigation.MainNavigationActivity
import com.emil.linksy.util.Linksy
import com.emil.presentation.R
import com.google.android.material.button.MaterialButton
import java.util.Locale

class LanguageSelectionActivity : AppCompatActivity() {
    private lateinit var next: MaterialButton
    private lateinit var languageSelector: Spinner
    private lateinit var sharedPref: SharedPreferences
    private lateinit var selectedLanguage: String
    private var  isRemember:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = getSharedPreferences(Linksy.SHAREDPREF_MAIN_KEY, Context.MODE_PRIVATE)
        isRemember =  sharedPref.getBoolean(Linksy.SHAREDPREF_REMEMBER_KEY, false)
        sharedPref.getString(Linksy.SHAREDPREF_LANGUAGE_KEY, null)?.let { language ->
            applyLocaleAndSwitch(language)
            return
        }
        setContentView(R.layout.activity_language_selection)
        next = findViewById(R.id.bt_next)
        languageSelector = findViewById(R.id.language_spinner)
        next.setOnClickListener {
            saveLanguage(selectedLanguage)
            applyLocaleAndSwitch(selectedLanguage)
        }
        setupLanguageSpinner()
    }

    private fun setupLanguageSpinner() {
        ArrayAdapter.createFromResource(this, R.array.languages_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            languageSelector.adapter = adapter
        }
        val systemLanguage = Locale.getDefault().language
        selectedLanguage = systemLanguage
        languageSelector.setSelection(if (systemLanguage == Linksy.SHAREDPREF_LANGUAGE_VALUE_RU) 1 else 0)
        languageSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedLanguage = if (position == 0) Linksy.SHAREDPREF_LANGUAGE_VALUE_EN else Linksy.SHAREDPREF_LANGUAGE_VALUE_RU
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun saveLanguage(language: String) {
        sharedPref.edit().putString(Linksy.SHAREDPREF_LANGUAGE_KEY, language).apply()
    }

    private fun applyLocaleAndSwitch(language: String) {
        LocaleManager.setLocale(this, language)
        if (isRemember) startActivity(Intent(this, MainNavigationActivity::class.java))
        else startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}
