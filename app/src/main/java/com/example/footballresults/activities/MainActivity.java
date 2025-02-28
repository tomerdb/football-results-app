package com.example.footballresults.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.footballresults.R;
import com.example.footballresults.database.DatabaseSeeder;

public class MainActivity extends AppCompatActivity {
    private View btnAddMatch, btnViewMatches, btnTeamStats, btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Seed database with initial data
        DatabaseSeeder seeder = new DatabaseSeeder(this);
        seeder.seedDatabase();

        // Initialize UI components
        initializeViews();
    }

    private void initializeViews() {
        btnAddMatch = findViewById(R.id.btn_add_match);
        btnViewMatches = findViewById(R.id.btn_view_matches);
        btnTeamStats = findViewById(R.id.btn_team_stats);
        btnSearch = findViewById(R.id.btn_search);

        // Set up click listeners directly in the initialization method
        btnAddMatch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MatchEntryActivity.class);
            startActivity(intent);
        });

        btnViewMatches.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            intent.putExtra("report_type", "matches");
            startActivity(intent);
        });

        btnTeamStats.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TeamStatsActivity.class);
            startActivity(intent);
        });

        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });
    }
}