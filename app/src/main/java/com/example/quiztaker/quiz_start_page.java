package com.example.quiztaker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

public class quiz_start_page extends AppCompatActivity {

    private TextView name;
    private TextView email;
    private Button signout;
    private Button start;
    private TextView show_score;
    private SharedPreferences sharedPreferences;
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_start_page);

        // Access SharedPreferences
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        // Retrieve data
        String userId = sharedPreferences.getString("UserId", null);


        signout = findViewById(R.id.signOut_btn);
        start = findViewById(R.id.start_quiz_btn);
        name = findViewById(R.id.username_tv);
        email = findViewById(R.id.email_tv);
        show_score = findViewById(R.id.score_show_tv);

        Intent intent = getIntent();
        int totalScore = intent.getIntExtra("TOTAL_SCORE", 0);

        show_score.setText("Score : " + totalScore);

        if (userId != null) {
            // Proceed with fetching user data from Firebase
            try {
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Retrieve user object
                            User user = snapshot.getValue(User.class);
                            if (user != null) {
                                // Display user details
                                name.setText("Name: " + user.getName());
                                email.setText("Email: " + user.getEmail());
                                // You can add more TextViews or modify as needed to display other user details
                            } else {
                                Toast.makeText(quiz_start_page.this, "User data not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(quiz_start_page.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(quiz_start_page.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("quiz_start_page", "Exception: " + e.getMessage());
            }
        } else {
            // Handle the case where userId is null
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            // Redirect the user to login/signup screen or perform other appropriate action
        }

        signout.setOnClickListener(view -> {
            signOutmethod();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        });

        start.setOnClickListener(view -> {
            Intent i = new Intent(this, Quiz_page.class);
            startActivity(i);
        });

    }
    private void signOutmethod() {
        // Clear user data from SharedPreferences to simulate logout
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}