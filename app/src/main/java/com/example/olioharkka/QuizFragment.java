package com.example.olioharkka;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class QuizFragment extends Fragment {

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.quiz_fragment, container, false);

        Button startQuizButton = rootView.findViewById(R.id.bStartQuiz);
        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start QuizActivity
                Intent intent = new Intent(getActivity(), QuizActivity.class);
                startActivity(intent);
            }
        });

        DataManager dataManager = DataManager.getInstance();
        String municipality = dataManager.getData("municipality");

        TextView MunicipalityTextQuiz = rootView.findViewById(R.id.tvMunicipalityStartQuiz);
        MunicipalityTextQuiz.setText(municipality);

        /*
        Button tryAgainButton = rootView.findViewById(R.id.bTryAgain);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start QuizActivity
                Intent intent = new Intent(getActivity(), QuizActivity.class);
                startActivity(intent);
            }
        });
         */

        return rootView;
    }

}

/*
    public void updateUIElements(int score) {
        // Set the score to the TextView
        TextView scoreTextView = rootView.findViewById(R.id.tvScoreResult);
        scoreTextView.setText(String.valueOf(score) + "/10");

        // Set the result text based on the score
        TextView scoreText = rootView.findViewById(R.id.tvResultText);
        if (score <= 6) {
            scoreText.setText("Good try!");
        } else if (score <= 8) {
            scoreText.setText("Nicely done!");
        } else if (score <= 10) {
            scoreText.setText("Excellent!");
        }

        // Make hidden elements visible
        TextView tvScore = rootView.findViewById(R.id.tvScore);
        tvScore.setVisibility(View.VISIBLE);

        scoreTextView.setVisibility(View.VISIBLE);
        scoreTextView.setVisibility(View.VISIBLE);

        Button tryAgainButton = rootView.findViewById(R.id.bTryAgain);
        tryAgainButton.setVisibility(View.VISIBLE);

        Button startQuizButton = rootView.findViewById(R.id.bStartQuiz);
        startQuizButton.setVisibility(View.GONE);
    }
}
 */