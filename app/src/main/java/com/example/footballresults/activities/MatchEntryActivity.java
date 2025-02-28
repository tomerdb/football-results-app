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