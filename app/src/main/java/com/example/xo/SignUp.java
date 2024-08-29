package com.example.xo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUp extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button signUpButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize UI components
        usernameEditText = findViewById(R.id.signUpUsername);
        passwordEditText = findViewById(R.id.signUpPassword);
        signUpButton = findViewById(R.id.signUpBtn);
        databaseHelper = new DatabaseHelper(this);

        // Set up sign-up button click listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get username and password inputs
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Perform basic validation
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUp.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Add user to the database
                    boolean isInserted = databaseHelper.addUser(username, password);

                    if (isInserted) {
                        Toast.makeText(SignUp.this, "Sign-up successful!", Toast.LENGTH_SHORT).show();
                        finish(); // Close the sign-up activity
                    } else {
                        Toast.makeText(SignUp.this, "Sign-up failed. Try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
