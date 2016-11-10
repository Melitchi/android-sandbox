package com.android.melitchi.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private Button btn;
    private ImageView imageView;
    private static final int PHOTO_CODE_REQUEST = 0x12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=(ImageView)findViewById(R.id.imageView);
        webView=(WebView)findViewById(R.id.webView);
        btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture,PHOTO_CODE_REQUEST);
               // webView.loadUrl("javascript:hello('Chocobo')");
            }
        });
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT){
            webView.setWebContentsDebuggingEnabled(true);
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new MyWebClient());
        webView.loadUrl("file:///android_asset/www/page.html");

        webView.addJavascriptInterface(new JSInterface(this),"Android");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_CODE_REQUEST){
            if (resultCode == RESULT_OK){
                Bundle extras =data.getExtras();
                Bitmap image =(Bitmap) extras.get("data");
                imageView.setImageBitmap(image);
            }
        }
    }

    private class JSInterface {
        private final Context context;
        public JSInterface(Context context){
            this.context=context;
        }
        @JavascriptInterface
        public void helloJava(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "AndroKwei", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private class MyWebClient extends WebViewClient{
        public boolean shouldOverrideUrlLoading(WebView webView, String url){
            if (url.startsWith("mailto:")){

            }else if(url.startsWith("tel:")){

            }
            return false;
        }
    }
}
