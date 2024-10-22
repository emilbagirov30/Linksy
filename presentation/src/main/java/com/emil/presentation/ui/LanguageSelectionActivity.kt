package com.emil.presentation.ui
import com.emil.presentation.utils.LocaleManager
import com.emil.presentation.R
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.util.Locale

class LanguageSelectionActivity : AppCompatActivity() {
    private lateinit var next: MaterialButton
    private lateinit var languageSelector: Spinner
    private lateinit var sharedPref: SharedPreferences
    private lateinit var selectedLanguage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE)
        sharedPref.getString("language", null)?.let { language ->
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
        languageSelector.setSelection(if (systemLanguage == "ru") 1 else 0)
        languageSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedLanguage = if (position == 0) "en" else "ru"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun saveLanguage(language: String) {
        sharedPref.edit().putString("language", language).apply()
    }

    private fun applyLocaleAndSwitch(language: String) {
        LocaleManager.setLocale(this, language)
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}
