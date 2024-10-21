package com.emil.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import java.util.Locale

class LanguageSelectionActivity : AppCompatActivity() {
    private lateinit var next: MaterialButton
    private lateinit var languageSelector: Spinner
    private lateinit var sharedPref: SharedPreferences
    private lateinit var selectedLanguage:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE)
        if (sharedPref.contains("language")) switchToAuthActivity()
        setContentView(R.layout.activity_language_selection)
        next = findViewById(R.id.bt_next)
        languageSelector = findViewById(R.id.language_spinner)
        ArrayAdapter.createFromResource(this, R.array.languages_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            languageSelector.adapter = adapter
        }

        val systemLanguage = Locale.getDefault().language
        selectedLanguage = systemLanguage
        val defaultLanguage = when (systemLanguage){
            "ru" -> 1
            else -> 0
        }
        languageSelector.setSelection(defaultLanguage)
        next.setOnClickListener{
            with(sharedPref.edit()) {
                putString("language", selectedLanguage)
                apply()
            }
            switchToAuthActivity ()
        }



        languageSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selectedLanguage = when (position) {
                    0 -> "en"
                    else -> "ru"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
private fun switchToAuthActivity (){
    startActivity(Intent(this, AuthActivity::class.java))
    finish()
}



}