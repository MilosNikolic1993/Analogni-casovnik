package com.link.analognicasovnik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AnalogClockView analogClock = findViewById(R.id.analogClock);
        analogClock.setTime(12, 45, 30); // Postavljanje vremena programabilno

    }
}