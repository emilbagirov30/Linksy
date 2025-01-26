package com.emil.linksy.presentation.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar

class PrivacyPolicyActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.privacy_policy_activity)
        val webView: WebView = findViewById(R.id.webView)
        val toolbar = findViewById<MaterialToolbar>(R.id.tb)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://linksy-mes.ru/privacy")
    }
}