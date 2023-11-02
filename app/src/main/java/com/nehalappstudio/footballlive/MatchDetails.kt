package com.nehalappstudio.footballlive

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MatchDetails : AppCompatActivity() {

    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_details)

        // Find the WebView by its ID
        webView = findViewById(R.id.webView)

        // Enable JavaScript (if needed)
        webView.settings.javaScriptEnabled = true

        // Prevent links from opening in an external browser
        webView.webViewClient = WebViewClient()

        val id = intent.getStringExtra("id")

        if(id != null && !id.isEmpty()){

            Toast.makeText(baseContext, "Loaded", Toast.LENGTH_LONG).show()
            webView.loadUrl("https://football-live-nextjs-bare.vercel.app/match/"+id)
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    // Page has finished loading
                    // You can perform actions here

                }
            }
        }

        // Load a web page

    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}