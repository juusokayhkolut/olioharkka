package com.example.olioharkka;

import static com.example.olioharkka.ApiClient.searchForMunicipalityPopulation;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

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

                if (!recentSearches.contains(searchText)) {
                    recentSearches.add(searchText);
                    adapter.notifyDataSetChanged();
                }

                Intent intent = new Intent(MainActivity.this, TabActivity.class);
                intent.putExtra("MUNICIPALITY_NAME", searchText);
                startActivity(intent);
            }
        });

        recentSearchesList.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = recentSearches.get(position);
            searchField.setText(selectedItem);
        });
    }
}
