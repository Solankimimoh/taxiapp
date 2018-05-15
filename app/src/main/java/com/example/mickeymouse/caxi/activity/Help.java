package com.example.mickeymouse.caxi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mickeymouse.caxi.R;
import com.webianks.easy_feedback.EasyFeedback;

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        new EasyFeedback.Builder(this)
                .withEmail("caxiride@gmail.com")
                .withSystemInfo()
                .build()
                .start();
    }
}
