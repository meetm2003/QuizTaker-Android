package com.example.quiztaker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText editTextId;
    private EditText editTextPass;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch aSwitch;
    DatabaseReference userRef;
    private SharedPreferences sharedPreferences;
    public String UA_Id = "";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        if (isUserLoggedIn()) {
            startMainActivity();
            return;
        }

        editTextId = findViewById(R.id.EditTextEmail);
        editTextPass = findViewById(R.id.EditTextPass);
        Button loginBtn = findViewById(R.id.LoginBtn);
        Button signupBtn = findViewById(R.id.SignupLBtn);
        aSwitch = findViewById(R.id.switch2);

        final boolean[] admin = new boolean[1];
        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                aSwitch.setText("Admin");
                admin[0] = true;
                //Toast.makeText(MainActivity.this, "admin "+ admin[0], Toast.LENGTH_SHORT).show();
            } else {
                aSwitch.setText("User");
                admin[0] = false;
                //Toast.makeText(MainActivity.this, "admin "+ admin[0], Toast.LENGTH_SHORT).show();
            }
        });

        signupBtn.setOnClickListener(view -> {
            Intent i = new Intent(this, SignupPage.class);
            startActivity(i);
        });

        userRef = FirebaseDatabase.getInstance().getReference("Users");

        loginBtn.setOnClickListener(view -> {

            final String email = editTextId.getText().toString().trim();
            final String password = editTextPass.getText().toString().trim();

            userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        for (DataSnapshot ss : snapshot.getChildren()) {
                            User user = ss.getValue(User.class);
                            if (user != null && user.getPassword().equals(password)) {
                                UA_Id = ss.getKey();
                                if (aSwitch.isChecked() && user.getAdmin()) {
                                    Toast.makeText(MainActivity.this, "Login successful Admin", Toast.LENGTH_SHORT).show();



                                    return;
                                } else if (!aSwitch.isChecked() && !user.getAdmin()) {
                                    try {
                                        Intent i = new Intent(MainActivity.this, quiz_start_page.class);
                                        i.putExtra("UserId", UA_Id);
                                        startActivity(i);
                                        saveUserData(UA_Id, email,password);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Log.e("MainActivity", "Exception: " + e.getMessage());
                                    }
                                    Toast.makeText(MainActivity.this, "Login successful User", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }
                        Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Error: ", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private boolean isUserLoggedIn() {
        // Check if user is logged in by checking SharedPreferences
        return sharedPreferences.getBoolean("is_logged_in", false);
    }
    private void saveUserData(String UA_Id, String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserId", UA_Id);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putBoolean("is_admin", true);
        editor.putBoolean("is_logged_in", true);
        editor.apply();
    }
    private void startMainActivity() {
        Intent intent = new Intent(MainActivity.this, quiz_start_page.class);
        intent.putExtra("UserId", UA_Id);
        startActivity(intent);
        finish();
    }

}
