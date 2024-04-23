package com.example.olioharkka;

import static com.example.olioharkka.ApiClient.getAmountOfSummerCottages;
import static com.example.olioharkka.ApiClient.getEmploymentRate;
import static com.example.olioharkka.ApiClient.getMunicipalityPopulation;
import static com.example.olioharkka.ApiClient.getPopulationChange;
import static com.example.olioharkka.ApiClient.getWeather;
import static com.example.olioharkka.ApiClient.getWorkSelfSufficiency;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    private int currentQuestionIndex;
    private List<QuizQuestion> quizItems;
    private int score;

    private String correctAnswer, sOption1, sOption2;
    private double dOption1, dOption2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        quizItems = new ArrayList<>();
        score = 0;
        currentQuestionIndex = 0;

        try {
            // Initializes quiz questions
            initializeQuizQuestions();
        } catch (Exception e) {
            // Handles any exceptions that might occur during initialization
            Toast.makeText(this, "Error initializing quiz: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace(); // Prints stack trace for debugging
        }

        QuizQuestion currentQuizItem = quizItems.get(currentQuestionIndex);

        TextView questionTextView = findViewById(R.id.tvQuestion);
        questionTextView.setText(currentQuizItem.getQuestionText());

        RadioButton rbOption1 = findViewById(R.id.rbOption1);
        RadioButton rbOption2 = findViewById(R.id.rbOption2);
        RadioButton rbOption3 = findViewById(R.id.rbOption3);

        String[] answerOptions = currentQuizItem.getAnswerOptions();
        rbOption1.setText(answerOptions[0]);
        rbOption2.setText(answerOptions[1]);
        rbOption3.setText(answerOptions[2]);

        DataManager dataManager = DataManager.getInstance();

        try {
            String municipality = dataManager.getData("municipality");
            TextView MunicipalityTextResult = findViewById(R.id.tvMunicipalityTextResult);
            MunicipalityTextResult.setText(municipality);
        } catch (Exception e) {
            // Handles any exceptions that might occur while retrieving municipality data
            Toast.makeText(this, "Error retrieving municipality data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace(); // Prints stack trace for debugging
        }
    }

    private void initializeQuizQuestions() {
        // Initializes quiz questions
        // Modifies anwers options & correct answers based on chosen municipality
        DataManager dataManager = DataManager.getInstance();
        String municipality = dataManager.getData("municipality");

        Integer municipalityPopulation = getMunicipalityPopulation(municipality);
        municipalityPopulation = (int) Math.round(municipalityPopulation / 10000) * 10000;

        Random random = new Random();

        int variation = random.nextInt(100000) - 50000;
        int option1 = (municipalityPopulation + variation);
        variation = random.nextInt(100000) - 50000;
        int option2 = municipalityPopulation - variation;
        dOption1 = Math.abs(Math.round(option1 / 10000) * 10000);
        dOption2 = Math.abs(Math.round(option2 / 10000) * 10000);

        correctAnswer = municipalityPopulation.toString();
        sOption1 = String.valueOf(dOption1);
        sOption2 = String.valueOf(dOption2);

        quizItems.add(new QuizQuestion("Municipality population?", correctAnswer, new String[]{correctAnswer, sOption1, sOption2}));

        correctAnswer = String.valueOf(getPopulationChange(municipality));
        sOption1 = String.valueOf(Math.abs(Math.round(1000)-500));
        sOption2 = String.valueOf(Math.abs(Math.round(1000)-250));

        quizItems.add(new QuizQuestion("What is the population change in the municipality", correctAnswer, new String[]{correctAnswer, sOption1, sOption2}));

        Map<String, Object> weatherMap = getWeather(municipality);
        BigDecimal temperature = BigDecimal.valueOf((Double) weatherMap.get("temp") - 273.1).round(new MathContext(2, RoundingMode.HALF_UP));
        correctAnswer = String.valueOf(temperature) + " C";
        sOption1 = String.valueOf(Math.round(random.nextInt(15)-5.2)) + " C";
        sOption2 = String.valueOf(Math.round(random.nextInt(30)-5.5)) + " C";

        quizItems.add(new QuizQuestion("What is the current temperature in the municipality?", correctAnswer, new String[]{correctAnswer, sOption1, sOption2}));

        correctAnswer = String.valueOf(weatherMap.get("wind_speed")) + " m/s";
        sOption1 = String.valueOf(Math.abs(Math.round(random.nextInt(15)-5.2))) + " m/s";
        sOption2 = String.valueOf(Math.abs(Math.round(random.nextInt(9)+5.5))) + " m/s";

        quizItems.add(new QuizQuestion("What is the wind situation in the moment?", correctAnswer, new String[]{correctAnswer, sOption1, sOption2}));

        correctAnswer = String.valueOf(getWorkSelfSufficiency(municipality).toString());
        sOption1 = String.valueOf(Math.abs(Math.round(random.nextInt(100)-50)));
        sOption2 = String.valueOf(Math.abs(Math.round(random.nextInt(100)+50)));

        quizItems.add(new QuizQuestion("What is the Work Self Sufficiency in the municipality?", correctAnswer, new String[]{correctAnswer, sOption1, sOption2}));

        correctAnswer = String.valueOf(getEmploymentRate(municipality)) + " %";
        sOption1 = String.valueOf(Math.abs(Math.round(random.nextInt(100) - 30.5))) + " %";
        sOption2 = String.valueOf(Math.abs(Math.round(random.nextInt(100)-10.2))) + " %";

        quizItems.add(new QuizQuestion("What is the Employment Rate for the municipality?", correctAnswer, new String[]{correctAnswer, sOption1, sOption2}));

        correctAnswer = String.valueOf(getAmountOfSummerCottages(municipality));
        sOption1 = String.valueOf(Math.abs(Math.round(random.nextInt(1000)-500)));
        sOption2 = String.valueOf(Math.abs(Math.round(random.nextInt(1000)+250)));

        quizItems.add(new QuizQuestion("What is the amount of summer cottages in the municipality?", correctAnswer, new String[]{correctAnswer, sOption1, sOption2}));
        if (getPopulationChange(municipality) > 0) {
            correctAnswer = "Increased";
            sOption1 = "Decreased";
            sOption2 = "Stayed the same";
        } else if (getPopulationChange(municipality) < 0) {
            correctAnswer = "Decreased";
            sOption1 = "Increased";
            sOption2 = "Stayed the same";
        } else if (getPopulationChange(municipality) == 0) {
            correctAnswer = "Stayed the same";
            sOption1 = "Increased";
            sOption2 = "Decreased";
        }
        quizItems.add(new QuizQuestion("Has the population increased or decreased from the previous year?", correctAnswer, new String[]{correctAnswer, sOption1, sOption2}));
        if (getEmploymentRate(municipality) > 50) {
            correctAnswer = "Over 50 %";
            sOption1 = "Under 50 %";
            sOption2 = "Exactly 50 %";
        } else if (getEmploymentRate(municipality) < 50) {
            correctAnswer = "Under 50 %";
            sOption1 = "Over 50 %";
            sOption2 = "Exactly 50 %";
        } else if (getEmploymentRate(municipality) == 50) {
            correctAnswer = "Exactly 50 %";
            sOption1 = "Over 50 %";
            sOption2 = "Under 50 %";
        }
        quizItems.add(new QuizQuestion("Is the employment rate over or under 50 % ?", correctAnswer, new String[]{correctAnswer, sOption1, sOption2}));

        if (getEmploymentRate(municipality) > 0) {
            correctAnswer = "Over 0 degrees (C)";
            sOption1 = "Under 0 degrees (C)";
            sOption2 = "Exactly 0 degrees (C)";
        } else if (getEmploymentRate(municipality) < 0) {
            correctAnswer = "Under 0 degrees (C)";
            sOption1 = "Over 0 degrees (C)";
            sOption2 = "Exactly 0 degrees (C)";
        } else if (getEmploymentRate(municipality) == 0) {
            correctAnswer = "Exactly 0 degrees (C)";
            sOption1 = "Over 0 degrees (C)";
            sOption2 = "Under 0 degrees (C)";
        }
        quizItems.add(new QuizQuestion("Is the temperature over or under 0 degrees (C)?", correctAnswer, new String[]{correctAnswer, sOption1, sOption2}));
        // Creates a list that shuffles the answer options order
        List<String[]> shuffledOptions = new ArrayList<>();

        for (QuizQuestion question : quizItems) {
            String[] options = question.getAnswerOptions();
            List<String> optionList = Arrays.asList(options);
            Collections.shuffle(optionList);
            shuffledOptions.add(optionList.toArray(new String[0]));
        }

        for (int i = 0; i < quizItems.size(); i++) {
            QuizQuestion originalQuestion = quizItems.get(i);
            String[] shuffledArray = shuffledOptions.get(i);
            quizItems.set(i, new QuizQuestion(originalQuestion.getQuestionText(), originalQuestion.getCorrectAnswer(), shuffledArray));
        }

        TextView questionIndexTextView = findViewById(R.id.tvQuestionIndex);
        questionIndexTextView.setText(String.valueOf(currentQuestionIndex + 1) + " / 10");
    }

    public void switchToQuizFragment(View view) {
        // Switches from current activity into the QuizFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new QuizFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        finish();
    }

    public void updateQuestions(View view) {
        // Updates the activity layout based on the question index
        DataManager dataManager = DataManager.getInstance();
        String municipality = dataManager.getData("municipality");

        TextView MunicipalityTextResult = findViewById(R.id.tvMunicipalityTextResult);
        MunicipalityTextResult.setText(municipality);

        RadioGroup rgQuiz = findViewById(R.id.rgQuiz);
        int selectedRadioButtonId = rgQuiz.getCheckedRadioButtonId();

        if (selectedRadioButtonId == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }
        rgQuiz.clearCheck();

        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String selectedAnswer = selectedRadioButton.getText().toString();
        QuizQuestion currentQuizQuestion = quizItems.get(currentQuestionIndex);
        String correctAnswer = currentQuizQuestion.getCorrectAnswer();

        if (selectedAnswer.equals(correctAnswer)) {
            score++;
        }

        currentQuestionIndex++;
        QuizQuestion currentQuizItem = quizItems.get(currentQuestionIndex);

        TextView questionTextView = findViewById(R.id.tvQuestion);
        questionTextView.setText(currentQuizItem.getQuestionText());

        RadioButton Option1 = findViewById(R.id.rbOption1);
        RadioButton Option2 = findViewById(R.id.rbOption2);
        RadioButton Option3 = findViewById(R.id.rbOption3);

        String[] answerOptions = currentQuizItem.getAnswerOptions();
        Option1.setText(answerOptions[0]);
        Option2.setText(answerOptions[1]);
        Option3.setText(answerOptions[2]);

        if (currentQuestionIndex == 9) {
            Button nextButton = findViewById(R.id.bNextQuestion);
            nextButton.setVisibility(View.GONE);

            Button submitButton = findViewById(R.id.bSubmit);
            submitButton.setVisibility(View.VISIBLE);
        }

        TextView questionIndexTextView = findViewById(R.id.tvQuestionIndex);
        questionIndexTextView.setText(String.valueOf(currentQuestionIndex + 1) + " / 10"); // Adding 1 to start from 1 instead of 0
    }

    public void submitAnswers(View view) {
        // Submits the final score to the outcome layout and starts that activity
        RadioGroup rgQuiz = findViewById(R.id.rgQuiz);
        int selectedRadioButtonId = rgQuiz.getCheckedRadioButtonId();

        if (selectedRadioButtonId == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String selectedAnswer = selectedRadioButton.getText().toString();
        QuizQuestion currentQuizQuestion = quizItems.get(currentQuestionIndex);
        String correctAnswer = currentQuizQuestion.getCorrectAnswer();

        if (selectedAnswer.equals(correctAnswer)) {
            score++;
        }

        Intent intent = new Intent(this, QuizOutcomeActivity.class);
        intent.putExtra("SCORE", score);
        startActivity(intent);
        finish();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}