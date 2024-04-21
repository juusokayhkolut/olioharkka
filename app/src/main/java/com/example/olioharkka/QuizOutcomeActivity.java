package com.example.olioharkka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class QuizOutcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz_outcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DataManager dataManager = DataManager.getInstance();
        String municipality = dataManager.getData("municipality");
        TextView MunicipalityTextResult = findViewById(R.id.tvMunicipalityOutcome);
        MunicipalityTextResult.setText(municipality);

        int score = getIntent().getIntExtra("SCORE", 0);

        // Set the score to the TextView
        TextView scoreTextView = findViewById(R.id.tvScoreOutcome);
        scoreTextView.setText("Score:" + "\n" + String.valueOf(score) + "/10");

        if (score <= 6) {
            TextView scoreText = findViewById(R.id.tvScoreText);
            scoreText.setText("Good try!");
        } else if (score <=8) {
            TextView scoreText = findViewById(R.id.tvScoreText);
            scoreText.setText("Nicely done!");
        } else if (score <=10) {
            TextView scoreText = findViewById(R.id.tvScoreText);
            scoreText.setText("Excellent!");
        }
    }

    public void switchToStart(View view) {
        Intent intent = new Intent(this, QuizActivity.class);
        startActivity(intent);

        finish();
    }

    public void switchToQuizFragment(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new QuizFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        finish();
    }
}