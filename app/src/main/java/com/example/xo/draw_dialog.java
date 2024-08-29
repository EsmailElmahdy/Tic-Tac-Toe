package com.example.xo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class draw_dialog extends Dialog {

    private MainActivity mainActivity;
    private MainActivityComputer mainActivityComputer;

    public draw_dialog(@NonNull Context context, String message, MainActivity mainActivity) {
        super(context);
        this.mainActivity = mainActivity;
    }

    public draw_dialog(@NonNull Context context, String message, MainActivityComputer mainActivity) {
        super(context);
        this.mainActivityComputer = mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_draw_dialog);
        Button startAgainButton = findViewById(R.id.startAgainButton);

        startAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mainActivity != null) {
                    mainActivity.restartMatch();

                }
                else
                {
                    mainActivityComputer.restartMatch();
                }
                dismiss();
            }
        });
    }
}