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

/**
 * Activity for searching matches by team.
 * This activity allows users to select a team from a dropdown spinner
 * and view all matches where that team participated, either as team A or team B.
 */
public class SearchActivity extends AppCompatActivity {
    /** UI Components */
    private Spinner spinnerTeam;
    private RecyclerView recyclerViewMatches;
    private TextView tvNoMatches;
    
    /** Adapter for displaying matches */
    private MatchAdapter matchAdapter;
    
    /** Data Access Objects */
    private MatchDao matchDao;
    private TeamStatsDao teamStatsDao;
    
    /** List of team names for the spinner */
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

        // Initialize views and data
        initializeViews();
        initializeData();
        setupSpinner();
    }

    /**
     * Initializes all UI components by finding their views.
     */
    private void initializeViews() {
        spinnerTeam = findViewById(R.id.spinner_team);
        recyclerViewMatches = findViewById(R.id.recycler_matches);
        tvNoMatches = findViewById(R.id.tv_no_matches);

        // Set up RecyclerView
        recyclerViewMatches.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Initializes data access objects.
     */
    private void initializeData() {
        matchDao = new MatchDao(this);
        teamStatsDao = new TeamStatsDao(this);
    }

    /**
     * Sets up the team selection spinner with data and listener.
     * Loads all team names and configures the spinner's behavior
     * when a team is selected.
     */
    private void setupSpinner() {
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

    /**
     * Loads all team names from the database and populates the spinner.
     * Adds a default "Select a team" option at the beginning of the list.
     */
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

    /**
     * Searches for and displays all matches involving the selected team.
     * @param teamName The name of the team to search for
     */
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

    /**
     * Shows the "no matches found" message and hides the RecyclerView.
     */
    private void showNoMatches() {
        recyclerViewMatches.setVisibility(View.GONE);
        tvNoMatches.setVisibility(View.VISIBLE);
    }

    /**
     * Shows the list of matches and hides the "no matches" message.
     * @param matches List of matches to display
     */
    private void showMatches(List<Match> matches) {
        recyclerViewMatches.setVisibility(View.VISIBLE);
        tvNoMatches.setVisibility(View.GONE);
        matchAdapter = new MatchAdapter(this, matches);
        recyclerViewMatches.setAdapter(matchAdapter);
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