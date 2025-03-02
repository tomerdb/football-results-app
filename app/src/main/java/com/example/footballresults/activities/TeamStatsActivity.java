package com.example.footballresults.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballresults.R;
import com.example.footballresults.adapters.TeamStatsAdapter;
import com.example.footballresults.database.TeamStatsDao;
import com.example.footballresults.models.TeamStats;
import com.example.footballresults.utils.StatisticsCalculator;

import java.util.List;

/**
 * Activity for displaying team statistics.
 * Shows a sortable table of team statistics including matches played,
 * wins, draws, losses, goals scored, and total points.
 * Provides functionality to recalculate statistics and sort by points.
 */
public class TeamStatsActivity extends AppCompatActivity {
    /** UI Components */
    private RecyclerView recyclerView;
    private ImageView sortIndicator;
    
    /** Data handling components */
    private TeamStatsAdapter adapter;
    private TeamStatsDao teamStatsDao;
    private StatisticsCalculator statsCalculator;
    
    /** State tracking */
    private boolean isAscendingSort = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_stats);

        // Set up toolbar with back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.team_statistics));
        }

        // Initialize components
        initializeViews();
        initializeData();
        loadTeamStats();
    }

    /**
     * Initializes all UI components by finding their views.
     */
    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_team_stats);
        sortIndicator = findViewById(R.id.sort_indicator);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Synchronize horizontal scrolling between header and data
        HorizontalScrollView headerScroll = findViewById(R.id.header_scroll);
        HorizontalScrollView dataScroll = findViewById(R.id.data_scroll);

        headerScroll.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> 
            dataScroll.scrollTo(scrollX, 0));

        dataScroll.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> 
            headerScroll.scrollTo(scrollX, 0));
    }

    /**
     * Initializes data access objects and utilities.
     */
    private void initializeData() {
        teamStatsDao = new TeamStatsDao(this);
        statsCalculator = new StatisticsCalculator(this);
    }

    /**
     * Updates the sort indicator icon based on current sort direction.
     */
    private void updateSortIndicator() {
        if (isAscendingSort) {
            sortIndicator.setImageResource(android.R.drawable.arrow_up_float);
        } else {
            sortIndicator.setImageResource(android.R.drawable.arrow_down_float);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data when returning to this activity
        loadTeamStats();
    }

    /**
     * Loads team statistics from the database and displays them.
     * Statistics are sorted according to the current sort direction.
     */
    private void loadTeamStats() {
        teamStatsDao.open();
        List<TeamStats> teamStatsList = teamStatsDao.getAllTeamStatsSorted(isAscendingSort);
        teamStatsDao.close();

        adapter = new TeamStatsAdapter(this, teamStatsList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_team_stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Handle back button in toolbar
            finish();
            return true;
        } else if (id == R.id.action_recalculate_stats) {
            // Recalculate all statistics
            recalculateAllStats();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Recalculates all team statistics from match data.
     * This is useful if statistics become out of sync or after bulk operations.
     */
    private void recalculateAllStats() {
        statsCalculator.recalculateAllStats();
        loadTeamStats();
        Toast.makeText(this, getString(R.string.stats_recalculated), Toast.LENGTH_SHORT).show();
    }
}