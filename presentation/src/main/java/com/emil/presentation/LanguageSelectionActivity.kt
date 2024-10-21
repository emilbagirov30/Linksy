package com.emil.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class LanguageSelectionActivity : AppCompatActivity() {
    private lateinit var next: MaterialButton
    private lateinit var languageSelector: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_selection)
        next = findViewById(R.id.bt_next)
        languageSelector = findViewById(R.id.language_spinner)

    }
}