package com.example.olioharkka;

import static com.example.olioharkka.ApiClient.getAmountOfSummerCottages;
import static com.example.olioharkka.ApiClient.getEmploymentRate;
import static com.example.olioharkka.ApiClient.getMunicipalityPopulation;
import static com.example.olioharkka.ApiClient.getPopulationChange;
import static com.example.olioharkka.ApiClient.getWeather;
import static com.example.olioharkka.ApiClient.getWorkSelfSufficiency;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;

public class MunicipalityDetailsFragment extends Fragment {

    private TextView municipalityNameView;
    private TextView populationView;
    private TextView wikipediaLinkView;
    private TextView weatherView;
    private TextView workSelfSufficiencyView;
    private TextView employmentRateView;
    private TextView summerCottagesView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.municipality_details_fragment, container, false);
        return rootView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get municipality data
        DataManager dataManager = DataManager.getInstance();
        String municipalityName = dataManager.getData("municipality");

        municipalityNameView = view.findViewById(R.id.municipalityName);
        municipalityNameView.setText(municipalityName);

        populationView = view.findViewById(R.id.population);
        populationView.setText("Population: Placeholder");

        wikipediaLinkView = view.findViewById(R.id.wikipediaLink);
        wikipediaLinkView.setText("Wikipedia: Placeholder");

        weatherView = view.findViewById(R.id.weather);
        weatherView.setText("Weather: Placeholder");

        workSelfSufficiencyView = view.findViewById(R.id.workSelfSufficiency);
        workSelfSufficiencyView.setText("Work Self Sufficiency: Placeholder");

        employmentRateView = view.findViewById(R.id.employmentRate);
        employmentRateView.setText("Employment Rate: Placeholder");

        summerCottagesView = view.findViewById(R.id.amountOfSummerCottages);
        summerCottagesView.setText("Summer Cottages: Placeholder");

        Map<String, Object> municipalityWeatherMap = getWeather(municipalityName);
        Weather municipalityWeather = new Weather(Double.parseDouble(municipalityWeatherMap.get("temp").toString()), Double.parseDouble(municipalityWeatherMap.get("wind_speed").toString()));

        Municipality municipality = new Municipality(municipalityName, getMunicipalityPopulation(municipalityName), getPopulationChange(municipalityName), "Wikipedia: https://fi.wikipedia.org/wiki/" + municipalityName, getWorkSelfSufficiency(municipalityName), getEmploymentRate(municipalityName), getAmountOfSummerCottages(municipalityName), municipalityWeather);

        if (municipality.getPopulation() != null) {
            populationView.setText("Population: " + municipality.getPopulation().toString() + " (" +(municipality.getPopulationChange()>0?"+":"") + (int) Math.round(municipality.getPopulationChange()) + ") (2022)");
            dataManager.setData("population", municipality.getPopulation().toString());
            dataManager.setData("populationChange", municipality.getPopulationChange().toString());
        }

        SpannableString spannableString = new SpannableString("Wikipedia: https://fi.wikipedia.org/wiki/" + municipality);
        spannableString.setSpan(new UnderlineSpan(), 11, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 11, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wikipediaLinkView.setText(spannableString);
        wikipediaLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://fi.wikipedia.org/wiki/" + municipality;

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        dataManager.setData("wikipediaLink", "https://fi.wikipedia.org/wiki/" + municipality);

        if (municipality.getWorkSelfSufficiency() != null) {
            workSelfSufficiencyView.setText("Work Self Sufficiency: " + municipality.getWorkSelfSufficiency().toString() + "% (2022)");
            dataManager.setData("workSelfSufficiency", municipality.getWorkSelfSufficiency().toString());
        }

        if (municipality.getEmploymentRate() != null) {
            employmentRateView.setText("Employment Rate: " + municipality.getEmploymentRate().toString() + "% (2022)");
            dataManager.setData("employmentRate", municipality.getEmploymentRate().toString());
        }

        if (municipality.getSummerCottages() != null) {
            summerCottagesView.setText("Summer Cottages: " + (int) Math.round(municipality.getSummerCottages()) + " (2022)");
            dataManager.setData("summerCottages", municipality.getSummerCottages().toString());
        }

        if (municipality.getWeather() != null) {
            weatherView.setText("Current weather:\n" +
                                "Temperature (C): "+ BigDecimal.valueOf((Double) municipality.getWeather().getTemperature() - 273.1).round(new MathContext(2, RoundingMode.HALF_UP))+"\n" +
                                "Wind Speed (m/s): " + municipality.getWeather().getWindSpeed());
            dataManager.setData("weatherTemperature", (BigDecimal.valueOf((Double) municipality.getWeather().getTemperature() - 273.1).round(new MathContext(2, RoundingMode.HALF_UP)).toString()));
            dataManager.setData("weatherWindSpeed", municipality.getWeather().getWindSpeed().toString());
        }
    }

    public static <T> T getNestedValue(Map map, String... keys) {
        Object value = map;

        for (String key : keys) {
            value = ((Map) value).get(key);
        }

        return (T) value;
    }



}
