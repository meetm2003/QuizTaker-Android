package com.example.quiztaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference MyDbRef;
    private EditText editTextId;
    private EditText editTextPass;
    private Button LoginBtn, SignupBtn;
    private Switch aSwitch;
    DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextId = findViewById(R.id.EditTextEmail);
        editTextPass = findViewById(R.id.EditTextPass);
        LoginBtn = findViewById(R.id.LoginBtn);
        SignupBtn = findViewById(R.id.SignupLBtn);
        aSwitch = findViewById(R.id.switch2);

        final boolean[] admin = new boolean[1];
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    aSwitch.setText("Admin");
                    admin[0] = true;
                    //Toast.makeText(MainActivity.this, "admin "+ admin[0], Toast.LENGTH_SHORT).show();
                } else {
                    aSwitch.setText("User");
                    admin[0] = false;
                    //Toast.makeText(MainActivity.this, "admin "+ admin[0], Toast.LENGTH_SHORT).show();
                }
            }
        });

        SignupBtn.setOnClickListener(view -> {
            Intent i = new Intent(this, SignupPage.class);
            startActivity(i);
        });

        userRef = FirebaseDatabase.getInstance().getReference("Users");

        LoginBtn.setOnClickListener(view -> {

            final String email = editTextId.getText().toString().trim();
            final String password = editTextPass.getText().toString().trim();

            userRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        for (DataSnapshot ss : snapshot.getChildren()) {
                            User user = ss.getValue(User.class);
                            if (user != null && user.getPassword().equals(password)) {
                                if (aSwitch.isChecked() && user.getAdmin()) {
                                    Toast.makeText(MainActivity.this, "Login successful Admin", Toast.LENGTH_SHORT).show();
                                    return;
                                } else if (!aSwitch.isChecked() && !user.getAdmin()) {
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
}