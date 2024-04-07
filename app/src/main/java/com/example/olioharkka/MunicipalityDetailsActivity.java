package com.example.olioharkka;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MunicipalityDetailsActivity extends AppCompatActivity {

    private TextView municipalityNameView;
    private TextView populationView;
    private TextView wikipediaLinkView;
    private TextView politicalDivisionView;
    private TextView weatherView;
    private TextView incomeLevelView;
    private Button mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipality_details);

        municipalityNameView = findViewById(R.id.municipalityName);
        populationView = findViewById(R.id.population);
        wikipediaLinkView = findViewById(R.id.wikipediaLink);
        politicalDivisionView = findViewById(R.id.politicalDivision);
        weatherView = findViewById(R.id.weather);
        incomeLevelView = findViewById(R.id.incomeLevel);
        mapButton = findViewById(R.id.mapButton);

        Intent intent = getIntent();
        String municipalityName = intent.getStringExtra("MUNICIPALITY_NAME");
        municipalityNameView.setText(municipalityName);
        populationView.setText("Asukasmäärä: Placeholder");
        wikipediaLinkView.setText("Wikipedia-linkki: Placeholder");
        politicalDivisionView.setText("Poliittinen jakauma: Placeholder");
        weatherView.setText("Sää: Placeholder");
        incomeLevelView.setText("Tulotaso: Placeholder");

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(MunicipalityDetailsActivity.this, MapActivity.class);
                mapIntent.putExtra("MUNICIPALITY_NAME", municipalityName);
                startActivity(mapIntent);
            }
        });
    }
}
