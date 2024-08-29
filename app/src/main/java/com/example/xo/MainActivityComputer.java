package com.example.xo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivityComputer extends AppCompatActivity {

    private TextView playerOneName, playerTwoName;
    private static boolean playerOneTurn = true; // True if it's Player 1's turn, false for Computer
    private static int[] gameState = {0, 0, 0, 0, 0, 0, 0, 0, 0}; // 0 - Empty, 1 - Player One (X), 2 - Computer (O)
    private final int[][] winPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
            {0, 4, 8}, {2, 4, 6}             // Diagonals
    };
    private static List<ImageView> imageViewList = new ArrayList<>();
    private static boolean gameActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        if (sharedPreferences == null) {
            throw new NullPointerException("SharedPreferences is null");
        }

        String language = sharedPreferences.getString("language", "en");
        LocaleHelper.setLocale(this, language);
        setContentView(R.layout.activity_main_computer);

        playerOneName = findViewById(R.id.playerOneName);
        playerTwoName = findViewById(R.id.playerTwoName);

        // Get player names from Intent
        String playerOne = getIntent().getStringExtra("playerOne");
        playerOneName.setText(playerOne);
        playerTwoName.setText("Computer");

        // Add ImageViews to the list for easier access
        imageViewList.add(findViewById(R.id.image1));
        imageViewList.add(findViewById(R.id.image2));
        imageViewList.add(findViewById(R.id.image3));
        imageViewList.add(findViewById(R.id.image4));
        imageViewList.add(findViewById(R.id.image5));
        imageViewList.add(findViewById(R.id.image6));
        imageViewList.add(findViewById(R.id.image7));
        imageViewList.add(findViewById(R.id.image8));
        imageViewList.add(findViewById(R.id.image9));

        // Set click listeners for all cells
        for (int i = 0; i < imageViewList.size(); i++) {
            final int index = i;
            imageViewList.get(i).setOnClickListener(view -> onCellClick(view, index));
        }
    }

    private void onCellClick(View view, int index) {
        if (!gameActive || gameState[index] != 0) {
            return;
        }

        if (playerOneTurn) {
            gameState[index] = 1;
            ((ImageView) view).setImageResource(R.drawable.ximage);
            if (checkWin()) {
                ResultDialog resultDialog = new ResultDialog(MainActivityComputer.this, playerOneName.getText().toString(), MainActivityComputer.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
                return;
            }
            playerOneTurn = false;
            computerTurn();
        }
    }

    private void computerTurn() {
        if (!gameActive) return;

        // First, try to win or block
        if (makeOptimalMove()) {
            return;
        }

        // If no optimal move, pick a random available spot
        List<Integer> availablePositions = new ArrayList<>();
        for (int i = 0; i < gameState.length; i++) {
            if (gameState[i] == 0) {
                availablePositions.add(i);
            }
        }

        if (availablePositions.isEmpty()) {
            draw_dialog resultDialog = new draw_dialog(MainActivityComputer.this, "Match Draw", MainActivityComputer.this);
            resultDialog.setCancelable(false);
            resultDialog.show();
            return;
        }

        // Randomly choose a position from available spots
        int randomIndex = new Random().nextInt(availablePositions.size());
        int chosenPosition = availablePositions.get(randomIndex);

        gameState[chosenPosition] = 2;
        imageViewList.get(chosenPosition).setImageResource(R.drawable.oimage);

        if (checkWin()) {
            ResultDialog resultDialog = new ResultDialog(MainActivityComputer.this, playerTwoName.getText().toString(), MainActivityComputer.this);
            resultDialog.setCancelable(false);
            resultDialog.show();
        } else {
            playerOneTurn = true; // Back to player one’s turn
        }
    }

    private boolean makeOptimalMove() {
        // Try to win or block
        for (int[] winPosition : winPositions) {
            // Check if the computer can win
            if (canWin(2, winPosition)) {
                makeMove(findWinningMove(2, winPosition));
                return true;
            }
            // Check if the player is about to win and block it
            if (canWin(1, winPosition)) {
                makeMove(findWinningMove(1, winPosition));
                return true;
            }
        }
        return false;
    }

    private boolean canWin(int player, int[] winPosition) {
        int countPlayer = 0;
        int emptySpot = -1;

        for (int pos : winPosition) {
            if (gameState[pos] == player) {
                countPlayer++;
            } else if (gameState[pos] == 0) {
                emptySpot = pos;
            }
        }
        return countPlayer == 2 && emptySpot != -1;
    }

    private int findWinningMove(int player, int[] winPosition) {
        for (int pos : winPosition) {
            if (gameState[pos] == 0) {
                return pos;
            }
        }
        return -1; // This should never happen if canWin() returned true
    }

    private void makeMove(int position) {
        if (position >= 0 && position < gameState.length) {
            gameState[position] = 2;
            imageViewList.get(position).setImageResource(R.drawable.oimage);

            if (checkWin()) {
                ResultDialog resultDialog = new ResultDialog(MainActivityComputer.this, playerTwoName.getText().toString(), MainActivityComputer.this);
                resultDialog.setCancelable(false);
                resultDialog.show();
            } else {
                playerOneTurn = true; // Back to player one’s turn
            }
        }
    }

    private boolean checkWin() {
        for (int[] winPosition : winPositions) {
            if (gameState[winPosition[0]] != 0 &&
                    gameState[winPosition[0]] == gameState[winPosition[1]] &&
                    gameState[winPosition[1]] == gameState[winPosition[2]]) {
                gameActive = false;
                return true;
            }
        }

        // Check if all cells are filled
        boolean draw = true;
        for (int state : gameState) {
            if (state == 0) {
                draw = false;
                break;
            }
        }

        if (draw) {
            draw_dialog resultDialog = new draw_dialog(MainActivityComputer.this, "Match Draw", MainActivityComputer.this);
            resultDialog.setCancelable(false);
            resultDialog.show();
        }

        return false;
    }

    private void endGame(String message) {
        gameActive = false;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    static void restartMatch() {
        // Reset the game state
        gameState = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
        playerOneTurn = true;
        gameActive = true;

        // Reset ImageViews
        for (ImageView imageView : imageViewList) {
            imageView.setImageResource(R.drawable.white_box);
        }
    }
}
