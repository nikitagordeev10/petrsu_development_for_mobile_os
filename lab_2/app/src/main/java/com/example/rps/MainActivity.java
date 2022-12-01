package com.example.rps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    TextView scoreTextView,
            wonLostTextView,
            userSelectionTextView,
            compSelectionTextView;

    // Стартовые значения
    int userScore = 0;
    int compScore = 0;

    // Выбор компьютера
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreTextView = findViewById(R.id.scoreTextView);
        wonLostTextView = findViewById(R.id.wonLostTextView);
        compSelectionTextView = findViewById(R.id.compSelectionTextView);
        userSelectionTextView = findViewById(R.id.userSelectionTextView);

        wonLostTextView.setText("");
        compSelectionTextView.setText("");
        userSelectionTextView.setText("");
    }

    // Вариант пользователя
    public void rpsButtonSelected(View view) {
        Log.i(TAG, "Была выбрана кнопка rps: " + view.getTag());
        int userSelection = Integer.parseInt(view.getTag().toString());
        match(userSelection);
    }

    private void match(int userSelection) {

        // Вариант компьютера
        int low = 1;
        int high = 3;
        int compSelection = random.nextInt(high) + low;

        // Подведение итогов по баллам
        switch (userSelection){
            case 1:
                switch (compSelection){
                    case 1:
                        wonLostTextView.setText("Это ничья!");
                        break;
                    case 2:
                        wonLostTextView.setText("Ура, ты победил!");
                        userScore++;
                        break;
                    case 3:
                        wonLostTextView.setText("Упс, ты проиграл!");
                        compScore++;
                        break;
                }
                break;
            case 2:
                switch (compSelection){
                    case 1:
                        wonLostTextView.setText("Упс, ты проиграл!");
                        compScore++;
                        break;
                    case 2:
                        wonLostTextView.setText("Это ничья!");
                        break;
                    case 3:
                        wonLostTextView.setText("Ура, ты победил!");
                        userScore++;
                        break;
                }
                break;
            case 3:
                switch (compSelection){
                    case 1:
                        wonLostTextView.setText("Ура, ты победил!");
                        userScore++;
                        break;
                    case 2:
                        wonLostTextView.setText("Упс, ты проиграл!");
                        compScore++;
                        break;
                    case 3:
                        wonLostTextView.setText("Это ничья!");
                        break;
                }
                break;
        }
        
        // Установка баллов
        setScoreTextView(userScore, compScore);

        // Установка предмета компьютера
        switch (compSelection) {
            case 1:
                compSelectionTextView.setText("Камень");
                break;
            case 2:
                compSelectionTextView.setText("Ножницы");
                break;
            case 3:
                compSelectionTextView.setText("Бумага");
                break;
            default:
                compSelectionTextView.setText("");
        }

        // Установка предмета игрока
        switch (userSelection) {
            case 1:
                userSelectionTextView.setText("Камень");
                break;
            case 2:
                userSelectionTextView.setText("Ножницы");
                break;
            case 3:
                userSelectionTextView.setText("Бумага");
                break;
            default:
                userSelectionTextView.setText("");
        }
        setScoreTextView(userScore, compScore);
    }

    // Сброс до значений по умолчанию
    public void resetGame(View view) {
        userScore = 0;
        compScore = 0;
        wonLostTextView.setText("");
        compSelectionTextView.setText("");
        setScoreTextView(userScore, compScore);
        userSelectionTextView.setText("");
        compSelectionTextView.setText("");

    }

    // Установка предмета игрока
    private void setScoreTextView(int userScore, int compScore) {
        String scoreString = String.valueOf(userScore) + " : " + String.valueOf(compScore);
        scoreTextView.setText(scoreString);
    }

}
