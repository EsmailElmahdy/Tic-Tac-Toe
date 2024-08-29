package com.example.xo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addOnePlayer extends AppCompatActivity {

    EditText playerOne;
    Button startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        if (sharedPreferences == null) {
            throw new NullPointerException("SharedPreferences is null");
        }

        String language = sharedPreferences.getString("language", "en");
        LocaleHelper.setLocale(this, language);
        setContentView(R.layout.activity_add_one_player);

        playerOne = findViewById(R.id.playerOne);
        startGameButton = findViewById(R.id.startGameButton);

        startGameButton.setOnClickListener(view -> {
            String getPlayerOneName = playerOne.getText().toString();

            if (getPlayerOneName.isEmpty()) {
                Toast.makeText(addOnePlayer.this, "Please enter player name", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(addOnePlayer.this, MainActivityComputer.class);
                intent.putExtra("playerOne", getPlayerOneName);
                intent.putExtra("playerTwo", "Computer");
                startActivity(intent);
            }
        });
    }
}
