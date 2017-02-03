package com.eventmeapp.eventmeapp;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Erik Fok on 11/1/2016.
 */

public class EventPage extends AppCompatActivity { //


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String s= getIntent().getStringExtra("title");
        String s2 = getIntent().getStringExtra("location");
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar_settings);
        setContentView(R.layout.single_event);
        TextView textViewObj = (TextView) findViewById(R.id.textviewid);
        textViewObj.setText(s);
        TextView snippetText = (TextView) findViewById(R.id.location_text);
        snippetText.setText(s2);
        ImageView backButton = (ImageView) findViewById(R.id.iv_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}
