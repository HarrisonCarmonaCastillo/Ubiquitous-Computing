package com.example.android.sunshine.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.wearable.DataMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherActivity extends WearableActivity {

    private TextView tvTime, tvDate, tvMaxTemp, tvMinTemp;
    private ImageView ivWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                tvTime = (TextView) stub.findViewById(R.id.tvTime);
                tvDate = (TextView) stub.findViewById(R.id.tvDate);
                tvMaxTemp = (TextView) stub.findViewById(R.id.tvMaxTemp);
                tvMinTemp = (TextView) stub.findViewById(R.id.tvMinTemp);
                ivWeather = (ImageView) stub.findViewById(R.id.ivWeather);

                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                Date date = new Date();
                String currentTimeString = df.format(date);
                tvTime.setText(currentTimeString);
                String dateText = Utility.getFullFriendlyDayString(getApplicationContext(), date.getTime());
                tvDate.setText(dateText);
            }
        });

        setAmbientEnabled();

        // Register the local broadcast receiver
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Double high = intent.getDoubleExtra("high", 0);
            Double low = intent.getDoubleExtra("low", 0);
            Bitmap bitmap = (Bitmap) intent.getParcelableExtra("image");

            String highString = Utility.formatTemperature(getApplicationContext(), high);
            String lowString = Utility.formatTemperature(getApplicationContext(), low);
            tvMaxTemp.setText(highString);
            tvMinTemp.setText(lowString);
            ivWeather.setImageBitmap(bitmap);
        }
    }
}
