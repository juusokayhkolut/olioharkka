package com.example.olioharkka;

import static com.example.olioharkka.ApiClient.searchForMunicipalityPopulation;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText searchField;
    private Button searchButton;
    private ListView recentSearchesList;
    private ArrayList<String> recentSearches = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.searchField);
        searchButton = findViewById(R.id.searchButton);
        recentSearchesList = findViewById(R.id.recentSearchesList);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recentSearches);
        recentSearchesList.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchField.getText().toString();
                searchForMunicipality(searchText);
            }
        });

        recentSearchesList.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = recentSearches.get(position);
            searchForMunicipality(selectedItem);
        });
    }

    private void searchForMunicipality(String municipalityName) {

        CompletableFuture<JSONObject> response = searchForMunicipalityPopulation("");
        response.thenAccept(json -> System.out.println(json.optString("class")))
                .exceptionally(e -> {
                    System.out.println("Error: " + e.getMessage());
                    return null;
                });

        if (!recentSearches.contains(municipalityName)) {
            recentSearches.add(municipalityName);
            adapter.notifyDataSetChanged();
        }

        //Intent intent = new Intent(MainActivity.this, MunicipalityDetailsActivity.class);
        //intent.putExtra("MUNICIPALITY_NAME", municipalityName);
        //startActivity(intent);
    }
}
