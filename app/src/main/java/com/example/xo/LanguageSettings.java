package com.example.xo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xo.LocaleHelper;

public class LanguageSettings extends AppCompatActivity {

    private RadioGroup languageRadioGroup;
    private Button applyButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_language_settings);

        languageRadioGroup = findViewById(R.id.languageRadioGroup);
        applyButton = findViewById(R.id.applyButton);

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = languageRadioGroup.getCheckedRadioButtonId();
                String selectedLanguage = "en"; // Default to English

                if (selectedId == R.id.radioArabic) {
                    selectedLanguage = "ar";
                } else if (selectedId == R.id.radioFrench) {
                    selectedLanguage = "fr";
                }
                else if (selectedId == R.id.radioEnglish) {
                    selectedLanguage = "en";
                }else if (selectedId == R.id.radioGerman) {
                    selectedLanguage = "de";
                } else if (selectedId == R.id.radioSpanish) {
                    selectedLanguage = "es";
                }

                // Save the selected language
                editor.putString("language", selectedLanguage);
                editor.apply();

                // Apply the new locale and restart the app
                LocaleHelper.setLocale(LanguageSettings.this, selectedLanguage);
                Intent intent = new Intent(LanguageSettings.this, AddPlayer.class); // Replace with your main activity
                startActivity(intent);
                finish();
            }
        });
    }
}
