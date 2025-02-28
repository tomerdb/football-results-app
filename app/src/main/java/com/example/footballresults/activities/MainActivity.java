package com.example.footballresults.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.footballresults.R;

public class MainActivity extends AppCompatActivity {
    private Button btnAddMatch, btnViewMatches, btnTeamStats, btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddMatch = findViewById(R.id.btn_add_match);
        btnViewMatches = findViewById(R.id.btn_view_matches);
        btnTeamStats = findViewById(R.id.btn_team_stats);
        btnSearch = findViewById(R.id.btn_search);

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