package com.example.footballresults.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.footballresults.R;
import com.example.footballresults.database.MatchDao;
import com.example.footballresults.models.Match;
import com.example.footballresults.utils.StatisticsCalculator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MatchEntryActivity extends AppCompatActivity {
    private EditText etDate, etCity, etTeamA, etTeamB, etTeamAGoals, etTeamBGoals;
    private Button btnSave, btnCancel, btnDelete;
    private MatchDao matchDao;
    private StatisticsCalculator statsCalculator;
    private Match existingMatch;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_entry);

        // Initialize components
        initializeViews();
        initializeData();
        setupListeners();
    }

    private void initializeViews() {
        etDate = findViewById(R.id.et_date);
        etCity = findViewById(R.id.et_city);
        etTeamA = findViewById(R.id.et_team_a);
        etTeamB = findViewById(R.id.et_team_b);
        etTeamAGoals = findViewById(R.id.et_team_a_goals);
        etTeamBGoals = findViewById(R.id.et_team_b_goals);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        btnDelete = findViewById(R.id.btn_delete);
    }

    private void initializeData() {
        matchDao = new MatchDao(this);
        statsCalculator = new StatisticsCalculator(this);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Check if we're editing an existing match
        if (getIntent().hasExtra("match_id")) {
            long matchId = getIntent().getLongExtra("match_id", -1);
            if (matchId != -1) {
                matchDao.open();
                existingMatch = matchDao.getMatchById(matchId);
                matchDao.close();

                if (existingMatch != null) {
                    populateFields(existingMatch);
                    btnDelete.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setupListeners() {
        // Date picker dialog
        etDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MatchEntryActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        etDate.setText(dateFormat.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Save button
        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveMatch();
            }
        });

        // Cancel button
        btnCancel.setOnClickListener(v -> finish());

        // Delete button
        btnDelete.setOnClickListener(v -> {
            if (existingMatch != null) {
                deleteMatch(existingMatch.getId());
            }
        });
    }

    private void populateFields(Match match) {
        etDate.setText(match.getDate());
        etCity.setText(match.getCity());
        etTeamA.setText(match.getTeamA());
        etTeamB.setText(match.getTeamB());
        etTeamAGoals.setText(String.valueOf(match.getTeamAGoals()));
        etTeamBGoals.setText(String.valueOf(match.getTeamBGoals()));
    }

    private boolean validateInputs() {
        if (etDate.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.error_date_required));
            return false;
        }
        if (etCity.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.error_city_required));
            return false;
        }
        if (etTeamA.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.error_team_a_required));
            return false;
        }
        if (etTeamB.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.error_team_b_required));
            return false;
        }
        if (etTeamAGoals.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.error_team_a_goals_required));
            return false;
        }
        if (etTeamBGoals.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.error_team_b_goals_required));
            return false;
        }

        String teamA = etTeamA.getText().toString().trim();
        String teamB = etTeamB.getText().toString().trim();
        if (teamA.equals(teamB)) {
            showToast(getString(R.string.error_same_teams));
            return false;
        }

        return true;
    }

    private void saveMatch() {
        String date = etDate.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String teamA = etTeamA.getText().toString().trim();
        String teamB = etTeamB.getText().toString().trim();
        int teamAGoals = Integer.parseInt(etTeamAGoals.getText().toString().trim());
        int teamBGoals = Integer.parseInt(etTeamBGoals.getText().toString().trim());

        Match match = new Match(date, city, teamA, teamB, teamAGoals, teamBGoals);

        matchDao.open();

        if (existingMatch != null) {
            // We need to update an existing match
            match.setId(existingMatch.getId());

            // First remove the old match's impact on statistics
            statsCalculator.removeMatchStats(existingMatch);

            // Update the match
            boolean success = matchDao.updateMatch(match);

            if (success) {
                // Add the new match's impact on statistics
                statsCalculator.calculateStatsForMatch(match);
                showToast(getString(R.string.match_updated));
                finish();
            } else {
                showToast(getString(R.string.error_update_match));
            }
        } else {
            // Adding a new match
            long matchId = matchDao.addMatch(match);

            if (matchId != -1) {
                match.setId(matchId);
                statsCalculator.calculateStatsForMatch(match);
                showToast(getString(R.string.match_added));
                finish();
            } else {
                showToast(getString(R.string.error_add_match));
            }
        }

        matchDao.close();
    }

    private void deleteMatch(long matchId) {
        matchDao.open();
        Match match = matchDao.getMatchById(matchId);

        if (match != null) {
            // Remove this match's impact on team statistics
            statsCalculator.removeMatchStats(match);

            boolean success = matchDao.deleteMatch(matchId);
            if (success) {
                showToast(getString(R.string.match_deleted));
                finish();
            } else {
                showToast(getString(R.string.error_delete_match));
            }
        }

        matchDao.close();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}