package com.example.olioharkka;

import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CompareFragment extends Fragment {
    private TextView municipalityNameView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compare_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DataManager dataManager = DataManager.getInstance();
        String munipality1 = dataManager.getData("municipality");

        TextView municipality1NameView = view.findViewById(R.id.municipality1Name);
        municipality1NameView.setText(munipality1);
    }
}