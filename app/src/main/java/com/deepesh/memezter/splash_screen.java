package com.deepesh.memezter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Pauses splash screen for 2 sec & then opens MainActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(splash_screen.this,MainActivity.class);
            startActivity(intent);
        },2000);
    }

}