package com.example.olioharkka;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.olioharkka.databinding.ActivityMapBinding;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Hae SupportMapFragment ja ilmoita, kun kartta on valmis käytettäväksi.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Oletetaan, että olet hakenut kunnan sijainnin (leveys- ja pituusasteet).
        // Korvaa seuraavat arvot oikeilla koordinaateilla.
        LatLng municipalityLocation = new LatLng(-34, 151); // Esimerkki: Sydney, Australia

        mMap.addMarker(new MarkerOptions().position(municipalityLocation).title("Kunta"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(municipalityLocation, 10));
    }
}

