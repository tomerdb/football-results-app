package com.example.footballresults.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballresults.R;
import com.example.footballresults.adapters.MatchAdapter;
import com.example.footballresults.adapters.TeamStatsAdapter;
import com.example.footballresults.database.MatchDao;
import com.example.footballresults.database.TeamStatsDao;
import com.example.footballresults.models.Match;
import com.example.footballresults.models.TeamStats;

import java.util.List;

/**
 * Activity for displaying various types of reports.
 * This activity can show either a list of matches or team statistics
 * depending on the report type specified in the intent.
 * Supports viewing match details and editing matches from the list.
 */
public class ReportActivity extends AppCompatActivity {
    /** UI Components */
    private RecyclerView recyclerView;
    private TextView tvTitle, tvNoData;
    
    /** Data Access Objects */
    private MatchDao matchDao;
    private TeamStatsDao teamStatsDao;
    
    /** Type of report to display ("matches" or "stats") */
    private String reportType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Set up toolbar with back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize components
        initializeViews();
        initializeData();
        setupReportType();
        loadReportData();
    }

    /**
     * Initializes all UI components by finding their views.
     */
    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_report);
        tvTitle = findViewById(R.id.tv_report_title);
        tvNoData = findViewById(R.id.tv_no_data);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Initializes data access objects.
     */
    private void initializeData() {
        matchDao = new MatchDao(this);
        teamStatsDao = new TeamStatsDao(this);
    }

    /**
     * Sets up the report type based on intent extras and updates UI accordingly.
     * Configures the title and action bar based on the selected report type.
     */
    private void setupReportType() {
        // Get report type from intent
        reportType = getIntent().getStringExtra("report_type");
        if (reportType == null) {
            reportType = "matches"; // Default to matches report
        }

        // Set title based on report type
        if (getSupportActionBar() != null) {
            if (reportType.equals("matches")) {
                getSupportActionBar().setTitle(getString(R.string.matches_report));
                tvTitle.setText(getString(R.string.matches_report));
            } else {
                getSupportActionBar().setTitle(getString(R.string.team_statistics));
                tvTitle.setText(getString(R.string.team_statistics));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data when returning to this activity
        loadReportData();
    }

    /**
     * Loads the appropriate report data based on the report type.
     */
    private void loadReportData() {
        if (reportType.equals("matches")) {
            loadMatchesReport();
        } else {
            loadTeamStatsReport();
        }
    }

    /**
     * Loads and displays the matches report.
     * Shows a list of all matches sorted by date, with the ability to edit matches.
     */
    private void loadMatchesReport() {
        matchDao.open();
        // Use the new method that returns matches sorted by date
        List<Match> matches = matchDao.getAllMatchesSortedByDate();
        matchDao.close();

        if (matches.isEmpty()) {
            showNoData();
        } else {
            showData();
            MatchAdapter adapter = new MatchAdapter(this, matches);
            adapter.setOnItemClickListener(match -> {
                // Open match details for editing
                Intent intent = new Intent(ReportActivity.this, MatchEntryActivity.class);
                intent.putExtra("match_id", match.getId());
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * Loads and displays the team statistics report.
     * Shows a table of team statistics including matches played, wins, draws, etc.
     */
    private void loadTeamStatsReport() {
        teamStatsDao.open();
        List<TeamStats> teamStats = teamStatsDao.getAllTeamStats();
        teamStatsDao.close();

        if (teamStats.isEmpty()) {
            showNoData();
        } else {
            showData();
            TeamStatsAdapter adapter = new TeamStatsAdapter(this, teamStats);
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * Shows the data view and hides the "no data" message.
     */
    private void showData() {
        recyclerView.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.GONE);
    }

    /**
     * Shows the "no data" message and hides the data view.
     * Updates the message based on the current report type.
     */
    private void showNoData() {
        recyclerView.setVisibility(View.GONE);
        tvNoData.setVisibility(View.VISIBLE);

        if (reportType.equals("matches")) {
            tvNoData.setText(getString(R.string.no_matches_found));
        } else {
            tvNoData.setText(getString(R.string.no_team_stats_found));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_print) {
            // Handle print report functionality
            // This would typically involve creating a PDF and printing it
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}