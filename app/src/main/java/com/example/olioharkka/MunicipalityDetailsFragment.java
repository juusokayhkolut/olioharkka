package com.example.olioharkka;

import static com.example.olioharkka.ApiClient.getAmountOfSummerCottages;
import static com.example.olioharkka.ApiClient.getEmploymentRate;
import static com.example.olioharkka.ApiClient.getPopulationChange;
import static com.example.olioharkka.ApiClient.getWeather;
import static com.example.olioharkka.ApiClient.getWorkSelfSufficiency;
import static com.example.olioharkka.ApiClient.searchForMunicipalityPopulation;

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
import android.widget.Button;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import java.io.IOException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
        String municipality = dataManager.getData("municipality");

        municipalityNameView = view.findViewById(R.id.municipalityName);
        municipalityNameView.setText(municipality);

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

        // get data
        Integer municipalityPopulation = searchForMunicipalityPopulation(municipality);
        Double populationChange = getPopulationChange(municipality);
        Double workSelfSufficiency = getWorkSelfSufficiency(municipality);
        Double employmentRate = getEmploymentRate(municipality);
        Double summerCottages = getAmountOfSummerCottages(municipality);
        Map<String, Object> weather = getWeather(municipality);

        if (municipalityPopulation != null) {
            populationView.setText("Population: " + municipalityPopulation.toString() + "(" +(populationChange>0?" +":" ") + (int) Math.round(populationChange) + " this year) (2022)");
            dataManager.setData("population", municipalityPopulation.toString());
            dataManager.setData("populationChange", populationChange.toString());
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

        if (workSelfSufficiency != null) {
            workSelfSufficiencyView.setText("Work Self Sufficiency: " + workSelfSufficiency.toString() + "% (2022)");
            dataManager.setData("workSelfSufficiency", workSelfSufficiency.toString());
        }

        if (employmentRate != null) {
            employmentRateView.setText("Employment Rate: " + employmentRate.toString() + "% (2022)");
            dataManager.setData("employmentRate", employmentRate.toString());
        }

        if (summerCottages != null) {
            summerCottagesView.setText("Summer Cottages: " + (int) Math.round(summerCottages) + " (2022)");
            dataManager.setData("summerCottages", employmentRate.toString());
        }

        if (weather != null) {
            weatherView.setText("Current weather:" +
                                "Temperature (C): "+ BigDecimal.valueOf((Double) weather.get("temp") - 273.1).round(new MathContext(2, RoundingMode.HALF_UP))+"\n" +
                                "Wind Speed (m/s): " + weather.get("wind_speed"));
            dataManager.setData("weatherTemperature", (BigDecimal.valueOf((Double) weather.get("temp") - 273.1).round(new MathContext(2, RoundingMode.HALF_UP)).toString()));
            dataManager.setData("weatherWindSpeed", weather.get("wind_speed").toString());
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
