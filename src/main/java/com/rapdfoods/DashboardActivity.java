package com.rapdfoods;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import java.net.InetAddress;

public class DashboardActivity extends AppCompatActivity {

    private WebView webview;
    Button btn_retry;
   // Dialog MyDialog;
    SwipeRefreshLayout swipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btn_retry = (Button) findViewById(R.id.btn_retry);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWeb();
            }
        });
        loadWeb();
    }
    

    public boolean isInternetAvailable() {
        try {

            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    private void loadWeb() {

        webview = (WebView) findViewById(R.id.webView);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl("https://www.google.com");
        swipe.setRefreshing(true);
        webview.clearHistory();
        webview.clearCache(true);


        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                try {
                    webView.stopLoading();
                    //if (webView.goBack();)
                    swipe.setRefreshing(false);
                    showCustomDialog();
                } catch (Exception e) {

                }
            }

            public void onPageFinished(WebView view, String url) {
                swipe.setRefreshing(false);
            }
        });
    }

    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.customalertdialog, null);

         //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ThemeOverlay_AppCompat_Light);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(view);


        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        Button btn_retry = view.findViewById(R.id.btn_retry);
        btn_retry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loadWeb();


                alertDialog.hide();
            }
        });

         alertDialog.show();


    }


    @Override
    // This method is used to detect back button
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }
}





