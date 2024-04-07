package com.example.olioharkka;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private TextView questionTextView;
    private RadioGroup radioGroup;
    private RadioButton option1, option2, option3, option4;
    private Button submitButton;

    private int currentQuestionIndex = 0;
    private int score = 0;

    // Oletetaan, että meillä on staattinen lista kysymyksistä ja vastauksista
    private String[][] questions = {
            {"Mikä kunta on väkiluvultaan Suomen suurin?", "Helsinki", "Espoo", "Tampere", "Vantaa", "Helsinki"},
            {"Mikä näistä kunnista sijaitsee Lapissa?", "Kuusamo", "Sodankylä", "Joensuu", "Porvoo", "Sodankylä"},
            // Lisää kysymyksiä tarpeen mukaan
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.question);
        radioGroup = findViewById(R.id.radioGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                if (currentQuestionIndex < questions.length - 1) {
                    currentQuestionIndex++;
                    setQuestionView();
                } else {
                    // Quiz päättyy
                    // Näytä tulokset
                    finishQuiz();
                }
            }
        });

        setQuestionView();
    }

    private void setQuestionView() {
        String[] currentQuestion = questions[currentQuestionIndex];
        questionTextView.setText(currentQuestion[0]);
        option1.setText(currentQuestion[1]);
        option2.setText(currentQuestion[2]);
        option3.setText(currentQuestion[3]);
        option4.setText(currentQuestion[4]);
        radioGroup.clearCheck();
    }

    private void checkAnswer() {
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(radioButtonID);
        int idx = radioGroup.indexOfChild(radioButton);

        if (idx != -1) {
            String selectedAnswer = ((RadioButton) radioButton).getText().toString();
            String correctAnswer = questions[currentQuestionIndex][5];

            if (selectedAnswer.equals(correctAnswer)) {
                score++;
            }
        }
    }

    private void finishQuiz() {
        // Tässä voitaisiin siirtyä uuteen aktiviteettiin, joka näyttää käyttäjän pisteet ja tarjoaa mahdollisuuden yrittää uudelleen.
        // Esimerkiksi:
        // Intent intent = new Intent(QuizActivity.this, QuizResultsActivity.class);
        // intent.putExtra("SCORE", score);
        // startActivity(intent);
        // Finish current activity
        finish();
    }
}
