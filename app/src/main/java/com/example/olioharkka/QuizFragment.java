package com.example.olioharkka;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        // Inflates the layout for this fragment
        View rootView = inflater.inflate(R.layout.quiz_fragment, container, false);

        Button startQuizButton = rootView.findViewById(R.id.bStartQuiz);
        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starts QuizActivity
                Intent intent = new Intent(getActivity(), QuizActivity.class);
                startActivity(intent);
            }
        });

        try {
            DataManager dataManager = DataManager.getInstance();
            String municipality = dataManager.getData("municipality");
            TextView MunicipalityTextQuiz = rootView.findViewById(R.id.tvMunicipalityStartQuiz);
            MunicipalityTextQuiz.setText(municipality);
        } catch (Exception e) {
            // Handles any exceptions that might occur while retrieving municipality data
            e.printStackTrace(); // Prints stack trace for debugging
            // Shows an error message to the user
            Toast.makeText(getContext(), "Error retrieving municipality data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }

}

