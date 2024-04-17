package com.example.olioharkka;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_view_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        Intent intent = getIntent();
        String[] dataToPass = {intent.getStringExtra("MUNICIPALITY_NAME")};

        TabPagerAdapter pagerAdapter = new TabPagerAdapter(this, dataToPass);
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.setSwipeEnabled(false);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Perustiedot");
                            break;
                        case 1:
                            tab.setText("Vertaa");
                            break;
                        case 2:
                            tab.setText("Quiz");
                            break;
                        default:
                            tab.setText("Tab " + (position + 1));
                            break;
                    }
                }
        ).attach();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
