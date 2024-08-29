package com.example.xo;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultDialog extends Dialog {

    private final String message;
    private MainActivity mainActivity;
    private MainActivityComputer mainActivityComputer;

    public ResultDialog(@NonNull Context context, String message, MainActivity mainActivity) {
        super(context);
        this.message = message;
        this.mainActivity = mainActivity;
    }

    public ResultDialog(@NonNull Context context, String message, MainActivityComputer mainActivity) {
        super(context);
        this.message = message;
        this.mainActivityComputer = mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_dialog);

        TextView messageText = findViewById(R.id.messageText);
        Button startAgainButton = findViewById(R.id.startAgainButton);

        messageText.setText(message);

        startAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mainActivity != null) {
                    mainActivity.restartMatch();

                }
                else
                {
                    MainActivityComputer.restartMatch();
                }
                dismiss();
            }
        });
    }
}