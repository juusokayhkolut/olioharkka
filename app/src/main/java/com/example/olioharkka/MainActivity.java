package com.example.olioharkka;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText searchField;
    private Button searchButton;
    private ListView recentSearchesList;
    private ArrayList<String> recentSearches = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    String cityCode = CityCodeLookup.getCityCode(searchText.toLowerCase());

                    if (cityCode == "City not found") {
                        showCityErrorPopup();
                    } else {
                        recentSearches.add(searchText);
                        adapter.notifyDataSetChanged();

                        Intent intent = new Intent(MainActivity.this, TabActivity.class);
                        intent.putExtra("MUNICIPALITY_NAME", searchText);

                        startActivity(intent);
                    }
                }
            }
        });

        recentSearchesList.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = recentSearches.get(position);
            searchField.setText(selectedItem);
            Intent intent = new Intent(MainActivity.this, TabActivity.class);
            intent.putExtra("MUNICIPALITY_NAME", selectedItem);
            startActivity(intent);
        });
    }

    private void showCityErrorPopup() {
        // Show error popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("City is not valid, please try again!")
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
