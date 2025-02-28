package com.example.footballresults.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class TeamStatsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TeamStatsAdapter adapter;
    private TeamStatsDao teamStatsDao;
    private StatisticsCalculator statsCalculator;
    private boolean isAscendingSort = false; // Default to descending (highest points first)
    private ImageView sortIndicator;

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
        recyclerView = findViewById(R.id.recycler_team_stats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up points header sorting
        LinearLayout pointsHeader = findViewById(R.id.points_header);
        sortIndicator = findViewById(R.id.sort_indicator);

        pointsHeader.setOnClickListener(v -> {
            // Toggle sort direction
            isAscendingSort = !isAscendingSort;
            updateSortIndicator();
            loadTeamStats();
        });

        teamStatsDao = new TeamStatsDao(this);
        statsCalculator = new StatisticsCalculator(this);

        loadTeamStats();
    }

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

    private void recalculateAllStats() {
        statsCalculator.recalculateAllStats();
        loadTeamStats();
        Toast.makeText(this, getString(R.string.stats_recalculated), Toast.LENGTH_SHORT).show();
    }
}