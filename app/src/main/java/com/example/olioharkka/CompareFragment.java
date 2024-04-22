package com.example.olioharkka;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.olioharkka.ApiClient.getAmountOfSummerCottages;
import static com.example.olioharkka.ApiClient.getEmploymentRate;
import static com.example.olioharkka.ApiClient.getPopulationChange;
import static com.example.olioharkka.ApiClient.getWeather;
import static com.example.olioharkka.ApiClient.getWorkSelfSufficiency;
import static com.example.olioharkka.ApiClient.searchForMunicipalityPopulation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.AppCompatActivity;

public class CompareFragment extends Fragment {

    private TextView originalMunicipalityNameView;
    private TextView originalPopulationView;
    private TextView originalWikipediaLinkView;
    private TextView originalWeatherView;
    private TextView originalWorkSelfSufficiencyView;
    private TextView originalEmploymentRateView;
    private TextView originalSummerCottagesView;
    private TextView comparedMunicipalityNameView;
    private TextView comparedPopulationView;
    private TextView comparedWikipediaLinkView;
    private TextView comparedWeatherView;
    private TextView comparedWorkSelfSufficiencyView;
    private TextView comparedEmploymentRateView;
    private TextView comparedSummerCottagesView;
    private EditText searchField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compare_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DataManager dataManager = DataManager.getInstance();

        // Get data from storage
        String municipality = dataManager.getData("municipality");
        Double population = Double.parseDouble(dataManager.getData("population"));
        Double populationChange = Double.parseDouble(dataManager.getData("populationChange"));
        String wikipediaLink = dataManager.getData("wikipediaLink");
        Double weatherTemperature = Double.parseDouble(dataManager.getData("weatherTemperature"));
        Double weatherWindSpeed = Double.parseDouble(dataManager.getData("weatherWindSpeed"));
        Double workSelfSufficiency = Double.parseDouble(dataManager.getData("workSelfSufficiency"));
        Double employmentRate = Double.parseDouble(dataManager.getData("employmentRate"));
        Double summerCottages = Double.parseDouble(dataManager.getData("summerCottages"));

        originalMunicipalityNameView = view.findViewById(R.id.originalMunicipalityName);
        originalMunicipalityNameView.setText(municipality);

        originalPopulationView = view.findViewById(R.id.originalPopulation);
        originalPopulationView.setText("Population: " + population + "(" +(populationChange>0?" +":" ") + (int) Math.round(populationChange) + " this year) (2022)");

        originalWikipediaLinkView = view.findViewById(R.id.originalWikipediaLink);
        SpannableString spannableString = new SpannableString("Wikipedia: https://fi.wikipedia.org/wiki/" + municipality);
        spannableString.setSpan(new UnderlineSpan(), 11, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 11, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        originalWikipediaLinkView.setText(spannableString);
        originalWikipediaLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://fi.wikipedia.org/wiki/" + municipality;

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        originalWikipediaLinkView.setText("Wikipedia: " + wikipediaLink);

        originalWeatherView = view.findViewById(R.id.originalWeather);
        originalWeatherView.setText("Current weather:\n" +
                                    "Temperature (C): "+ BigDecimal.valueOf(weatherTemperature)+"\n" +
                                    "Wind Speed (m/s): " + weatherWindSpeed);

        originalWorkSelfSufficiencyView = view.findViewById(R.id.originalWorkSelfSufficiency);
        originalWorkSelfSufficiencyView.setText("Work Self Sufficiency: " + workSelfSufficiency);

        originalEmploymentRateView = view.findViewById(R.id.originalEmploymentRate);
        originalEmploymentRateView.setText("Employment Rate: " + employmentRate);

        originalSummerCottagesView = view.findViewById(R.id.originalAmountOfSummerCottages);
        originalSummerCottagesView.setText("Summer Cottages: " + summerCottages);

        // Compared municipality
        comparedMunicipalityNameView = view.findViewById(R.id.comparedMunicipalityName);
        comparedMunicipalityNameView.setText("Municipality #2");

        comparedPopulationView = view.findViewById(R.id.comparedPopulation);
        comparedPopulationView.setText(" ");

        comparedWikipediaLinkView = view.findViewById(R.id.comparedWikipediaLink);
        comparedWikipediaLinkView.setText(" ");

        comparedWeatherView = view.findViewById(R.id.comparedWeather);
        comparedWeatherView.setText(" ");

        comparedWorkSelfSufficiencyView = view.findViewById(R.id.comparedWorkSelfSufficiency);
        comparedWorkSelfSufficiencyView.setText(" ");

        comparedEmploymentRateView = view.findViewById(R.id.comparedEmploymentRate);
        comparedEmploymentRateView.setText(" ");

        comparedSummerCottagesView = view.findViewById(R.id.comparedAmountOfSummerCottages);
        comparedSummerCottagesView.setText(" ");

        searchField = view.findViewById(R.id.searchField);
        ProgressBar loadingIndicator = view.findViewById(R.id.loadingIndicator);

        // Search button
        Button compareButton = view.findViewById(R.id.compareMunicipalitiesButton);
        compareButton.setOnClickListener(v -> {
            if (searchField.getText().toString() != "") {
                loadingIndicator.setVisibility(View.VISIBLE);

                dismissKeyboard();

                String comparedMunicipality = searchField.getText().toString();

                // get data
                Integer comparedMunicipalityPopulation = searchForMunicipalityPopulation(comparedMunicipality);
                Double comparedPopulationChange = getPopulationChange(comparedMunicipality);
                Double comparedWorkSelfSufficiency = getWorkSelfSufficiency(comparedMunicipality);
                Double comparedEmploymentRate = getEmploymentRate(comparedMunicipality);
                Double comparedSummerCottages = getAmountOfSummerCottages(comparedMunicipality);
                Map<String, Object> comparedWeather = getWeather(comparedMunicipality);

                comparedMunicipalityNameView.setText(comparedMunicipality);
                comparedPopulationView.setText("Population: " + comparedMunicipalityPopulation + " (" +(comparedPopulationChange>0?" +":" ") + (int) Math.round(comparedPopulationChange) + " this year) (2022)");

                SpannableString comparedSpannableString = new SpannableString("Wikipedia: https://fi.wikipedia.org/wiki/" + comparedMunicipality);
                comparedSpannableString.setSpan(new UnderlineSpan(), 11, comparedSpannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comparedSpannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 11, comparedSpannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                comparedWikipediaLinkView.setText(comparedSpannableString);
                comparedWikipediaLinkView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://fi.wikipedia.org/wiki/" + comparedMunicipality;

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                });
                comparedWikipediaLinkView.setText(comparedSpannableString);

                comparedWeatherView.setText("Current weather:\n" +
                        "Temperature (C): "+ BigDecimal.valueOf((Double) comparedWeather.get("temp") - 273.1).round(new MathContext(2, RoundingMode.HALF_UP))+"\n" +
                        "Wind Speed (m/s): " + comparedWeather.get("wind_speed"));
                dataManager.setData("weatherTemperature", (BigDecimal.valueOf((Double) comparedWeather.get("temp") - 273.1).round(new MathContext(2, RoundingMode.HALF_UP)).toString()));
                dataManager.setData("weatherWindSpeed", comparedWeather.get("wind_speed").toString());

                comparedWorkSelfSufficiencyView.setText("Work Self Sufficiency: " + comparedWorkSelfSufficiency + "% (2022)");
                comparedEmploymentRateView.setText("Employment Rate: " + comparedEmploymentRate + "% (2022)");
                comparedSummerCottagesView.setText("Summer Cottages: " + comparedSummerCottages + " (2022)");

                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }

    private void dismissKeyboard() {
        View view = getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}