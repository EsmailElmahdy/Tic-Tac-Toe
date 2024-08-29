package com.example.xo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signUpButton;
    private CheckBox rememberMeCheckbox;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Initialize UI components
        usernameEditText = findViewById(R.id.Username);
        passwordEditText = findViewById(R.id.Password);
        loginButton = findViewById(R.id.loginBtn);
        signUpButton = findViewById(R.id.playWithComputer);
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);
        databaseHelper = new DatabaseHelper(this);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Auto-login if "Remember Me" was checked and credentials are valid
        if (sharedPreferences.getBoolean("rememberMe", false)) {
            String savedUsername = sharedPreferences.getString("username", "");
            String savedPassword = sharedPreferences.getString("password", "");

            if (databaseHelper.checkUser(savedUsername, savedPassword)) {
                // Successful login
                Toast.makeText(Login.this, "Auto-login successful!", Toast.LENGTH_SHORT).show();

                // Navigate to another activity (e.g., home screen)
                Intent intent = new Intent(Login.this, AddPlayer.class);
                startActivity(intent);
                finish(); // Close the login activity
            }
        }

        // Set up login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get username and password inputs
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Perform basic validation
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Check user credentials
                    boolean isValid = databaseHelper.checkUser(username, password);

                    if (isValid) {
                        // Save login info if "Remember Me" is checked
                        if (rememberMeCheckbox.isChecked()) {
                            editor.putBoolean("rememberMe", true);
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.apply();
                        } else {
                            editor.clear();
                            editor.apply();
                        }

                        // Successful login
                        Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();

                        // Navigate to another activity (e.g., home screen)
                        Intent intent = new Intent(Login.this, AddPlayer.class);
                        startActivity(intent);
                        finish(); // Close the login activity
                    } else {
                        // Login failed
                        Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Set up sign-up button click listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the sign-up activity
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
}
