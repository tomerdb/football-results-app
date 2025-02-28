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

public class ReportActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvTitle, tvNoData;
    private MatchDao matchDao;
    private TeamStatsDao teamStatsDao;
    private String reportType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Set up toolbar with back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        recyclerView = findViewById(R.id.recycler_report);
        tvTitle = findViewById(R.id.tv_report_title);
        tvNoData = findViewById(R.id.tv_no_data);

        // Initialize DAOs
        matchDao = new MatchDao(this);
        teamStatsDao = new TeamStatsDao(this);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        // Load report data
        loadReportData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data when returning to this activity
        loadReportData();
    }

    private void loadReportData() {
        if (reportType.equals("matches")) {
            loadMatchesReport();
        } else {
            loadTeamStatsReport();
        }
    }

    private void loadMatchesReport() {
        matchDao.open();
        List<Match> matches = matchDao.getAllMatches();
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

    private void showData() {
        recyclerView.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.GONE);
    }

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