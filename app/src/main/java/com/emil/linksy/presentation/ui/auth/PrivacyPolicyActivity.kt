package com.emil.linksy.presentation.ui.auth

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar

class PrivacyPolicyActivity : AppCompatActivity() {
    private var webView: WebView? = null
    companion object {
        private const val URL = "https://linksy-mes.ru/privacy"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.privacy_policy_activity)
        webView = findViewById(R.id.webView)
        val toolbar = findViewById<MaterialToolbar>(R.id.tb)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        webView?.webViewClient = WebViewClient()
        webView?.loadUrl(URL)
    }
    override fun onDestroy() {
        super.onDestroy()
        webView?.destroy()
    }
}