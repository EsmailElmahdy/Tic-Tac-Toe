package com.example.xo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Intent;

public class AddPlayer extends AppCompatActivity {

    EditText playerOne, playerTwo;
    Button startGameButton, playWithComputerButton;
    ImageButton settingsButton;
    MediaPlayer mediaPlayer;
    SharedPreferences sharedPreferences;
    boolean isMusicPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_player);

        playerOne = findViewById(R.id.playerOne);
        playerTwo = findViewById(R.id.playerTwo);
        startGameButton = findViewById(R.id.startGameButton);
        playWithComputerButton = findViewById(R.id.playWithComputer);
        settingsButton = findViewById(R.id.settingsBtn);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);

        // Load the current music state
        isMusicPlaying = sharedPreferences.getBoolean("musicPlaying", true);

        // Initialize the MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.music);

        // Ensure the music loops continuously
        mediaPlayer.setLooping(true); // This makes sure the music repeats automatically

        // Check if music should be played
        if (isMusicPlaying) {
            mediaPlayer.start();
        }

        // Handle settings button click
        settingsButton.setOnClickListener(view -> openSettingsDialog());

        startGameButton.setOnClickListener(view -> {
            String getPlayerOneName = playerOne.getText().toString();
            String getPlayerTwoName = playerTwo.getText().toString();

            if (getPlayerOneName.isEmpty() || getPlayerTwoName.isEmpty()) {
                Toast.makeText(AddPlayer.this, "Please enter player names", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(AddPlayer.this, MainActivity.class);
                intent.putExtra("playerOne", getPlayerOneName);
                intent.putExtra("playerTwo", getPlayerTwoName);
                startActivity(intent);
            }
        });

        playWithComputerButton.setOnClickListener(view -> {
            Intent intent = new Intent(AddPlayer.this, addOnePlayer.class);
            startActivity(intent);
        });
    }

    private void openSettingsDialog() {
        Settings settingsDialog = new Settings(this);
        settingsDialog.setCancelable(true);
        settingsDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void toggleMusic() {
        if (isMusicPlaying) {
            mediaPlayer.pause();
            isMusicPlaying = false;
        } else {
            mediaPlayer.start();
            isMusicPlaying = true;
        }

        // Save the updated music state
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("musicPlaying", isMusicPlaying);
        editor.apply();
    }

}
