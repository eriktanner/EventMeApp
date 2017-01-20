package com.eventmeapp.eventmeapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import static com.eventmeapp.eventmeapp.R.id.emailText;

/**
 * Created by Erik Fok on 10/24/2016.
 */

public class TabProfile extends Fragment {

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_eventstab, container, false);
    }

    }

