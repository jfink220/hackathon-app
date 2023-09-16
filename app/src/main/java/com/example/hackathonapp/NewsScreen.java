package com.example.hackathonapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;

import okhttp3.OkHttpClient;

public class NewsScreen extends MainActivity {
    String[] urls = {"","","","",""};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_news_screen);

            ImageButton[] buttons = {findViewById(R.id.NewsImg1), findViewById(R.id.NewsImg2), findViewById(R.id.NewsImg3), findViewById(R.id.NewsImg4), findViewById(R.id.NewsImg5)};
            TextView[] textViews = {findViewById(R.id.NewsText1), findViewById(R.id.NewsText2), findViewById(R.id.NewsText3), findViewById(R.id.NewsText4), findViewById(R.id.NewsText5),};
            Intent intent = getIntent();
            for (int i = 0; i < 5; i++) {
                if(intent.getStringExtra("Img" + i) != null) {
                    String imgURL = intent.getStringExtra("Img" + i);
                    Picasso.get().load(imgURL.substring(1, imgURL.length() - 1)).into((ImageView) (buttons[i]));
                    textViews[i].setText(intent.getStringExtra("Title" + i));
                    urls[i] = intent.getStringExtra("URL"+i);
                }
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }


    }
    public void openURL(View v){
        String index = (""+(v.getId()));
        index = index.substring(index.length()-1);
        int intIndex = Integer.parseInt(index);
        Log.i("INDEX", ""+urls[intIndex]);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urls[intIndex].substring(1, urls[intIndex].length()-1)));
        startActivity(intent);

    }
}