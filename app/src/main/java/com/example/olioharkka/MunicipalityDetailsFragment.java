package com.example.olioharkka;

import static com.example.olioharkka.ApiClient.searchForMunicipalityPopulation;

import android.os.Bundle;
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

import java.util.concurrent.CompletableFuture;

public class MunicipalityDetailsFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private GoogleMap mMap;

    private TextView municipalityNameView;
    private TextView populationView;
    private TextView wikipediaLinkView;
    private TextView politicalDivisionView;
    private TextView weatherView;
    private TextView incomeLevelView;
    private Button mapButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.municipality_details_fragment, container, false);

        mMapView = rootView.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        System.out.println("________GOOGLE MAPS INIT______________");
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get data
        Bundle args = getArguments();
        String municipality = args.getString("municipality");

        municipalityNameView = view.findViewById(R.id.municipalityName);
        populationView = view.findViewById(R.id.population);
        wikipediaLinkView = view.findViewById(R.id.wikipediaLink);
        politicalDivisionView = view.findViewById(R.id.politicalDivision);
        weatherView = view.findViewById(R.id.weather);
        incomeLevelView = view.findViewById(R.id.incomeLevel);

        municipalityNameView.setText(municipality);
        populationView.setText("Asukasmäärä: Placeholder");
        wikipediaLinkView.setText("Wikipedia-linkki: https://fi.wikipedia.org/wiki/Helsinki");
        politicalDivisionView.setText("Poliittinen jakauma: Placeholder");
        weatherView.setText("Sää: Placeholder");
        incomeLevelView.setText("Tulotaso: Placeholder");

        // population
        Integer municipalityPopulation = getMunicipalityPopulation(municipality);
        populationView.setText("Asukasmäärä: " + municipalityPopulation.toString());

        // wikipedia
        wikipediaLinkView.setText("Wikipedia-linkki: https://fi.wikipedia.org/wiki/" + municipality);

        searchAndShowCity(municipality);
    }

    private Integer getMunicipalityPopulation(String municipalityName) {
        CompletableFuture<Integer> response = searchForMunicipalityPopulation(municipalityName);
        CompletableFuture<Integer> populationForMunicipality = response.thenApply(population -> {
            System.out.println("Population: " + population);
            return population;
        }).exceptionally(e -> {
            System.out.println("Error: " + e.getMessage());
            return null;
        });

        return populationForMunicipality.join();
    }

    private void searchAndShowCity(String cityName) {
        GeoApiContext context = new GeoApiContext.Builder().apiKey(getString(R.string.google_maps_key)).build();
        GeocodingResult[] results;
        try {
            results = GeocodingApi.geocode(context, cityName).await();
            System.out.println("________MAPS DETILAS______"_);
            System.out.println(results);
            if (results != null && results.length > 0) {
                GeocodingResult result = results[0];
                com.google.maps.model.LatLng location = result.geometry.location;
                LatLngBounds bounds = new LatLngBounds(
                        new LatLng(result.geometry.bounds.southwest.lat, result.geometry.bounds.southwest.lng),
                        new LatLng(result.geometry.bounds.northeast.lat, result.geometry.bounds.northeast.lng)
                );
                LatLng latLng = new LatLng(location.lat, location.lng);

                if (mMap != null) {
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng).title(result.formattedAddress));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                } else {
                    // Log a message or handle the case where mMap is null
                    System.out.println("_______ Error loading city on map ___________");
                }
            }
        } catch (IOException | InterruptedException | com.google.maps.errors.ApiException e) {
            e.printStackTrace();
        }
    }

}
