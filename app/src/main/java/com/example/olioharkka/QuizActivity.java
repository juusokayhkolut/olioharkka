package com.example.olioharkka;

import static com.example.olioharkka.ApiClient.getMunicipalityPopulation;
import static com.example.olioharkka.ApiClient.getWeather;

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
        //tähän muuttujia mihin tallennetaan oikeat vastaukset ja vastausvaihtoehdot
        // ATM TULEE null HUMIDITYSSÄ?

        // Initializes quiz questions
        // Modifies anwers options & correct answers based on chosen municipality
        DataManager dataManager = DataManager.getInstance();
        String municipality = dataManager.getData("municipality");
        Integer municipalityPopulation = getMunicipalityPopulation(municipality);
        municipalityPopulation = Math.round(municipalityPopulation / 10000) * 10000;

        Random random = new Random();
        int variation = random.nextInt(100000) - 50000;
        int option1 = (municipalityPopulation + variation);
        variation = random.nextInt(100000) - 50000;
        int option2 = municipalityPopulation - variation;
        option1 = Math.abs(Math.round(option1 / 10000) * 10000);
        option2 = Math.abs(Math.round(option2 / 10000) * 10000);

        quizItems.add(new QuizQuestion("Municipality population?", municipalityPopulation.toString(), new String[]{municipalityPopulation.toString(), String.valueOf(option1), String.valueOf(option2)}));

        Map<String, Object> weatherMap = getWeather(municipality);
        BigDecimal temperature = BigDecimal.valueOf((Double) weatherMap.get("temp") - 273.1).round(new MathContext(2, RoundingMode.HALF_UP));
        String sTemperature = temperature + " C";
        double dOption1 = Math.round(random.nextInt(15)-5.2);
        double dOption2 = Math.round(random.nextInt(30)-5.5);
        String sOption1 = dOption1 + " C";
        String sOption2 = dOption2 + " C";

        quizItems.add(new QuizQuestion("What is the current temperature in the municipality?", sTemperature, new String[]{sTemperature, sOption1, sOption2}));

        String wind = weatherMap.get("wind_speed") + " m/s";
        dOption1 = Math.abs(Math.round(random.nextInt(15)-5.2));
        dOption2 = Math.abs(Math.round(random.nextInt(30)-5.5));
        sOption1 = dOption1 + " m/s";
        sOption2 = dOption2 + " m/s";

        quizItems.add(new QuizQuestion("What is the wind situation in the moment?", wind, new String[]{wind, sOption1, sOption2}));

        String humidity = weatherMap.get("HUMIDITY") + " %";
        dOption1 = Math.abs(Math.round(random.nextInt(100)-50));
        dOption2 = Math.abs(Math.round(random.nextInt(100)-50));
        sOption1 = dOption1 + " %";
        sOption2 = dOption2 + " %";

        quizItems.add(new QuizQuestion("What is the humidity at the moment?", humidity, new String[]{humidity, sOption1, sOption2}));
        quizItems.add(new QuizQuestion("What is the support for the Perussuomalaiset party in the municipality?", "Correct answer", new String[]{"Correct answer", "Option 1", "Option 2"}));
        quizItems.add(new QuizQuestion("What is the support for the Vihreät party in the municipality?", "Correct answer", new String[]{"Correct answer", "Option 1", "Option 2"}));
        quizItems.add(new QuizQuestion("What is the average net income in the municipality?", "Correct answer", new String[]{"Correct answer", "Option 1", "Option 2"}));
        quizItems.add(new QuizQuestion("What is the support for the SDP party in the municipality?", "Correct answer", new String[]{"Correct answer", "Option 1", "Option 2"}));

        String correctAnswer = "Finns Party (Perussuomalaiset)";
        sOption1 = chooseRandomParty(correctAnswer);
        System.out.println("Chosen Party: " + sOption1);
        sOption2 = chooseRandomParty(correctAnswer);

        quizItems.add(new QuizQuestion("What is the most popular party in the municipality?", correctAnswer, new String[]{correctAnswer, sOption1, sOption2}));

        correctAnswer = "Finns Party (Perussuomalaiset)\"";
        sOption1 = chooseRandomParty(correctAnswer);
        System.out.println("Chosen Party: " + sOption1);
        sOption2 = chooseRandomParty(correctAnswer);

        quizItems.add(new QuizQuestion("What is the least popular party in the municipality?", correctAnswer, new String[]{correctAnswer, sOption1, sOption2}));

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
        // Sitten pitää katsoa että kysymykset & vastaukset mätsää, otetaan suoraan juuson koodista

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

    private static String chooseRandomParty(String correctParty) {
        // Stores the main political parties and returns a party that differs from the input
        List<String> politicalParties = Arrays.asList(
                "National Coalition Party (Kansallinen Kokoomus)",
                "Social Democratic Party of Finland (Suomen Sosialidemokraattinen Puolue)",
                "Finns Party (Perussuomalaiset)",
                "Centre Party (Keskusta)",
                "Green League (Vihreä liitto)",
                "Left Alliance (Vasemmistoliitto)",
                "Swedish People's Party of Finland (Svenska folkpartiet i Finland)",
                "Christian Democrats (Kristillisdemokraatit)",
                "Movement Now (Liike Nyt)",
                "Pirate Party of Finland (Piraattipuolue)"
        );

        Random random = new Random();
        String chosenParty;
        do {
            chosenParty = politicalParties.get(random.nextInt(politicalParties.size()));
        } while (chosenParty.equals(correctParty));
        return chosenParty;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}