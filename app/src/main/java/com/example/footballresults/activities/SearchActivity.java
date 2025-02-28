package com.example.footballresults.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballresults.R;
import com.example.footballresults.adapters.MatchAdapter;
import com.example.footballresults.database.MatchDao;
import com.example.footballresults.database.TeamStatsDao;
import com.example.footballresults.models.Match;
import com.example.footballresults.models.TeamStats;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private Spinner spinnerTeam;
    private RecyclerView recyclerViewMatches;
    private TextView tvNoMatches;
    private MatchAdapter matchAdapter;
    private MatchDao matchDao;
    private TeamStatsDao teamStatsDao;
    private List<String> teamNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Set up toolbar with back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.search_matches));
        }

        // Initialize views
        spinnerTeam = findViewById(R.id.spinner_team);
        recyclerViewMatches = findViewById(R.id.recycler_matches);
        tvNoMatches = findViewById(R.id.tv_no_matches);

        // Initialize RecyclerView
        recyclerViewMatches.setLayoutManager(new LinearLayoutManager(this));

        // Initialize DAOs
        matchDao = new MatchDao(this);
        teamStatsDao = new TeamStatsDao(this);

        // Load team names for spinner
        loadTeamNames();

        // Set spinner listener
        spinnerTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { // Skip the "Select a team" option
                    String selectedTeam = teamNames.get(position);
                    searchMatchesByTeam(selectedTeam);
                } else {
                    // Clear the list if "Select a team" is selected
                    showNoMatches();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showNoMatches();
            }
        });
    }

    private void loadTeamNames() {
        teamStatsDao.open();
        List<TeamStats> allTeams = teamStatsDao.getAllTeamStats();
        teamStatsDao.close();

        teamNames = new ArrayList<>();
        teamNames.add(getString(R.string.select_team)); // First item is a prompt

        for (TeamStats team : allTeams) {
            teamNames.add(team.getTeamName());
        }

        // Set up the spinner with team names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, teamNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeam.setAdapter(adapter);
    }

    private void searchMatchesByTeam(String teamName) {
        matchDao.open();
        List<Match> matches = matchDao.getMatchesByTeam(teamName);
        matchDao.close();

        if (matches.isEmpty()) {
            showNoMatches();
        } else {
            showMatches(matches);
        }
    }

    private void showMatches(List<Match> matches) {
        tvNoMatches.setVisibility(View.GONE);
        recyclerViewMatches.setVisibility(View.VISIBLE);

        matchAdapter = new MatchAdapter(this, matches);
        recyclerViewMatches.setAdapter(matchAdapter);
    }

    private void showNoMatches() {
        tvNoMatches.setVisibility(View.VISIBLE);
        recyclerViewMatches.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}