package edu.monash.fit2081.countryinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebWiki extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_wiki);

        getSupportActionBar().setTitle(R.string.title_activity_web_wiki);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String selectedCountry = getIntent().getStringExtra("country");

        WebView webView = findViewById(R.id.wikiWebView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://en.wikipedia.org/wiki/" + selectedCountry);
    }


//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }

}
