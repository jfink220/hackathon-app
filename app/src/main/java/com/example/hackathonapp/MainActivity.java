package com.example.hackathonapp;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_ID = 1;
    String APIKey = "9a8be549e6394146a97a727af9f8cba0";
    private static final String CHANNEL_ID = "channel_id";
    OkHttpClient client = new OkHttpClient();
    String getURL = "https://newsapi.org/v2/everything?q=diabetes&language=en&sortBy=relevancy&page="+1+"&apiKey=9a8be549e6394146a97a727af9f8cba0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }
    public void SwitchtoHome(View view){
        setContentView(R.layout.activity_home_screen);
    }
    public void SwitchtoNews(View view){
        Log.i("APIRESPONSE", "TESTING");
        getData();
        setContentView(R.layout.activity_news_screen);
    }

    public void SwitchtoInfo(View view){
        setContentView(R.layout.activity_information_screen);
    }

    public String genRandNum(){
        int rand = (int)(Math.floor(Math.random() * 3) + 1);
        return "https://newsapi.org/v2/everything?q=diabetes&language=en&sortBy=relevancy&page="+rand+"&apiKey=e935ddb73b0147458f29105d83c0c535";
    }
    public void getData(){
        Request req = new Request.Builder().url(genRandNum()).build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String JSONData = response.body().string();
                            ObjectMapper OM = new ObjectMapper();
                            JsonNode JN = OM.readTree(JSONData);
                            int dataRange = (int)(Math.floor((Math.random() * JN.get("articles").size())))- 5;
                            while(dataRange < 0){
                                dataRange = (int)(Math.floor((Math.random() * JN.get("articles").size())))- 5;
                            }
                            Intent intent = new Intent(MainActivity.this, NewsScreen.class);
                            for(int i = dataRange; i < 5 + dataRange; i++){
                                int numArticle = i;
                                while(JN.get("articles").get(numArticle).get("title").toString() == null ||
                                        JN.get("articles").get(numArticle).get("url").toString() == null ||
                                        JN.get("articles").get(numArticle).get("urlToImage").toString() == null ||
                                        (JN.get("articles").get(numArticle).get("title").toString().toLowerCase().indexOf("diabetes") == -1
                                                && JN.get("articles").get(numArticle).get("title").toString().toLowerCase().indexOf("drug") == -1 &&
                                                JN.get("articles").get(numArticle).get("title").toString().toLowerCase().indexOf("insulin") == -1 &&
                                                JN.get("articles").get(numArticle).get("title").toString().toLowerCase().indexOf("weight") == -1)){
                                    numArticle = (int)(Math.floor(((JN.get("articles").size() - 1) * Math.random())));
                                }
                                intent.putExtra("Title"+(i-dataRange), JN.get("articles").get(numArticle).get("title").toString());
                                intent.putExtra("URL"+(i-dataRange), JN.get("articles").get(numArticle).get("url").toString());
                                intent.putExtra("Img"+(i-dataRange), JN.get("articles").get(numArticle).get("urlToImage").toString());
                                startActivity(intent);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }
    public void receiveNotification(View view) {
        //notif code

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Reminder")
                .setContentText("Remember to check the info about diabetes")
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_dialog_alert))
                .build();

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, notification);
        }

    }
    private void showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Notification Title")
                .setContentText("Notification Text")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_dialog_alert));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        CharSequence name = "My Channel";
        String description = "Channel Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}