package com.example.quiztaker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class quiz_start_page extends AppCompatActivity {

    private TextView name;
    private TextView email;
    private TextView show_score;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_start_page);

        Button start_btn = findViewById(R.id.start_quiz_btn);
        name = findViewById(R.id.username_tv);
        email = findViewById(R.id.email_tv);
        show_score = findViewById(R.id.score_show_tv);


    }
}