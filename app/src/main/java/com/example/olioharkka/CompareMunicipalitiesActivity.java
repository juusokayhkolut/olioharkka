package com.example.olioharkka;


import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CompareMunicipalitiesActivity extends AppCompatActivity {

    private TextView municipality1Name;
    private TextView municipality2Name;
    private TextView populationComparison;
    private TextView politicalDivisionComparison;
    private TextView incomeLevelComparison;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_municipalities);

        municipality1Name = findViewById(R.id.municipality1Name);
        municipality2Name = findViewById(R.id.municipality2Name);
        populationComparison = findViewById(R.id.populationComparison);
        politicalDivisionComparison = findViewById(R.id.politicalDivisionComparison);
        incomeLevelComparison = findViewById(R.id.incomeLevelComparison);

        // Oletetaan, että olemme jo hankkineet tiedot kahdesta kunnasta
        // ja että ne ovat saatavilla. Tässä esimerkissä käytämme oletusarvoja.
        municipality1Name.setText("Kunta 1: Placeholder");
        municipality2Name.setText("Kunta 2: Placeholder");

        // Vertaile kunnan tietoja ja päivitä UI
        compareMunicipalities();
    }

    private void compareMunicipalities() {
        // Tässä metodissa vertaillaan kahden kunnan tietoja.
        // Oletetaan, että olemme jo hankkineet tarvittavat tiedot.
        // Päivitä näkymä vastaamaan vertailun tuloksia.
        // Esimerkki: "Asukasmäärä: Kunta 1 > Kunta 2"
        populationComparison.setText("Asukasmäärä: Placeholder");
        politicalDivisionComparison.setText("Poliittinen jakauma: Placeholder");
        incomeLevelComparison.setText("Tulotasotiedot: Placeholder");
    }
}
