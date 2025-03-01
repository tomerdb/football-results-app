package com.example.footballresults.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.footballresults.R;
import com.example.footballresults.database.DatabaseSeeder;

/**
 * Main entry point of the application.
 * This activity serves as the home screen and provides navigation to all major features
 * including adding matches, viewing match history, checking team statistics, and searching matches.
 */
public class MainActivity extends AppCompatActivity {
    /** UI Components for navigation buttons */
    private View btnAddMatch, btnViewMatches, btnTeamStats, btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Seed database with initial data if needed
        DatabaseSeeder seeder = new DatabaseSeeder(this);
        seeder.seedDatabase();

        // Initialize UI components
        initializeViews();
    }

    /**
     * Initializes all UI components and sets up their click listeners.
     * Each button launches a different activity for specific functionality:
     * - Add Match: Opens form to add new matches
     * - View Matches: Shows list of all matches
     * - Team Stats: Displays team statistics
     * - Search: Allows searching for specific matches
     */
    private void initializeViews() {
        // Find views
        btnAddMatch = findViewById(R.id.btn_add_match);
        btnViewMatches = findViewById(R.id.btn_view_matches);
        btnTeamStats = findViewById(R.id.btn_team_stats);
        btnSearch = findViewById(R.id.btn_search);

        // Set up click listeners for navigation
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