package com.example.xo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Settings extends Dialog {

    private AddPlayer addPlayer;
    private SharedPreferences sharedPreferences;
    private ImageButton toggleMusicButton;
    private Button logoutButton;

    public Settings(Context context) {
        super(context);
        // Check if the context is an instance of AddPlayer
        if (context instanceof AddPlayer) {
            addPlayer = (AddPlayer) context;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toggleMusicButton = findViewById(R.id.btnToggleMusic);
        logoutButton = findViewById(R.id.logoutBtn);

        // Initialize SharedPreferences
        sharedPreferences = getContext().getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);

        // Check the current music state and update the button image
        boolean isMusicPlaying = sharedPreferences.getBoolean("musicPlaying", true);
        updateButtonImage(isMusicPlaying);

        toggleMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addPlayer != null) {
                    addPlayer.toggleMusic();
                    // After toggling, update the image based on the new state
                    boolean newMusicState = sharedPreferences.getBoolean("musicPlaying", true);
                    updateButtonImage(newMusicState);
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear only login-related data from SharedPreferences
                SharedPreferences loginPreferences = getContext().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor loginEditor = loginPreferences.edit();
                loginEditor.clear(); // Clear only login data
                loginEditor.apply();

                // Navigate back to the login activity
                Intent intent = new Intent(getContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear stack
                getContext().startActivity(intent);
                dismiss(); // Close the settings dialog
            }
        });


    }

    private void updateButtonImage(boolean isMusicPlaying) {
        if (isMusicPlaying) {
            toggleMusicButton.setImageResource(R.drawable.music); // Show the "music" image
        } else {
            toggleMusicButton.setImageResource(R.drawable.mute); // Show the "mute" image
        }
    }
}
