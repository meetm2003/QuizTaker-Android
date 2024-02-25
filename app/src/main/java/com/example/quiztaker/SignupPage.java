package com.example.quiztaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupPage extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passEditText, cpassEditText;
    private Button signbtn;
    private Switch aSwitch;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        nameEditText = findViewById(R.id.NameEditText);
        emailEditText = findViewById(R.id.EmailEditText);
        passEditText = findViewById(R.id.PassEditText);
        cpassEditText = findViewById(R.id.CpassEditText);
        aSwitch = findViewById(R.id.switch1);
        signbtn = findViewById(R.id.SignupBtn);
        final boolean[] admin = new boolean[1];
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    aSwitch.setText("Admin");
                    admin[0] = true;
                    //Toast.makeText(SignupPage.this, "admin "+ admin[0], Toast.LENGTH_SHORT).show();

                } else {
                    aSwitch.setText("User");
                    admin[0] = false;
                    //Toast.makeText(SignupPage.this, "admin "+ admin[0], Toast.LENGTH_SHORT).show();
                }
            }
        });

        userRef = FirebaseDatabase.getInstance().getReference("Users");
        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = nameEditText.getText().toString().trim();
                final String email = emailEditText.getText().toString().trim();
                final String pass = passEditText.getText().toString().trim();
                final String cpass = cpassEditText.getText().toString().trim();

                String userId = userRef.push().getKey();

                        if(pass.equals(cpass)){
                            User u = new User(name, email, cpass, admin[0]);
                            assert userId != null;
                            userRef.child(userId).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if(admin[0]) {
                                            Toast.makeText(SignupPage.this, "Admin created successfully", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(SignupPage.this, MainActivity.class);
                                            startActivity(i);
                                        }
                                        else {
                                            Toast.makeText(SignupPage.this, "User created successfully", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(SignupPage.this, MainActivity.class);
                                            startActivity(i);
                                        }
                                    } else {
                                        Toast.makeText(SignupPage.this, "Failed to create user", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(SignupPage.this, "Password and Confirm password are not same...", Toast.LENGTH_SHORT).show();
                        }
                    }
        });

    }
}