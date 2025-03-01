package com.example.footballresults.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.footballresults.models.Match;
import com.example.footballresults.utils.StatisticsCalculator;

/**
 * Database seeder class responsible for populating the database with initial data.
 * This class provides functionality to seed the database with sample matches and teams,
 * and calculates initial statistics. It ensures the database is only seeded once
 * by checking if data already exists.
 */
public class DatabaseSeeder {
    private static final String TAG = "DatabaseSeeder";
    private final Context context;
    private final DatabaseHelper dbHelper;

    /**
     * Constructs a new DatabaseSeeder.
     * @param context The application context
     */
    public DatabaseSeeder(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Seeds the database with initial data if it's empty.
     * This method checks if the database already contains data before seeding
     * to prevent duplicate entries.
     */
    public void seedDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Check if database already has data
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_MATCHES, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        // Only seed if database is empty
        if (count == 0) {
            Log.d(TAG, "Database is empty. Seeding with initial data...");
            insertSampleData(db);
            Log.d(TAG, "Database seeding completed successfully.");
        } else {
            Log.d(TAG, "Database already contains data. Skipping seeding.");
        }

        db.close();
    }

    /**
     * Inserts sample data into the database within a transaction.
     * This method handles the complete seeding process:
     * 1. Inserts teams
     * 2. Inserts matches
     * 3. Calculates and updates team statistics
     * @param db The SQLiteDatabase instance to use for insertions
     */
    private void insertSampleData(SQLiteDatabase db) {
        // Create transaction for better performance
        db.beginTransaction();
        try {
            // Insert teams
            insertTeams(db);

            // Insert matches
            insertMatches(db);

            // Calculate statistics
            updateStatistics(db);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error seeding database: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Inserts initial team data into the database.
     * Creates records for all teams with initial statistics set to zero.
     * @param db The SQLiteDatabase instance to use for insertions
     */
    private void insertTeams(SQLiteDatabase db) {
        Log.d(TAG, "Inserting initial teams...");
        String[] teams = {
                "מכבי ת\"א",
                "הפועל ת\"א",
                "ביתר ירושלים",
                "מכבי חיפה",
                "מכבי נתניה",
                "מכבי פתח תקווה",
                "הפועל באר שבע",
                "מכבי הרצליה"
        };

        for (String team : teams) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_TEAM_NAME, team);
            values.put(DatabaseHelper.COLUMN_MATCHES_PLAYED, 0);
            values.put(DatabaseHelper.COLUMN_WINS, 0);
            values.put(DatabaseHelper.COLUMN_DRAWS, 0);
            values.put(DatabaseHelper.COLUMN_LOSSES, 0);
            values.put(DatabaseHelper.COLUMN_GOALS_SCORED, 0);
            values.put(DatabaseHelper.COLUMN_GOALS_AGAINST, 0);
            values.put(DatabaseHelper.COLUMN_POINTS, 0);

            db.insert(DatabaseHelper.TABLE_TEAM_STATS, null, values);
        }
    }

    /**
     * Inserts sample match data into the database.
     * Creates records for initial matches with various results.
     * @param db The SQLiteDatabase instance to use for insertions
     */
    private void insertMatches(SQLiteDatabase db) {
        Log.d(TAG, "Inserting initial matches...");

        // Match 1
        ContentValues match1 = new ContentValues();
        match1.put(DatabaseHelper.COLUMN_DATE, "22/11/2022");
        match1.put(DatabaseHelper.COLUMN_CITY, "תל אביב");
        match1.put(DatabaseHelper.COLUMN_TEAM_A, "מכבי ת\"א");
        match1.put(DatabaseHelper.COLUMN_TEAM_B, "הפועל ת\"א");
        match1.put(DatabaseHelper.COLUMN_TEAM_A_GOALS, 2);
        match1.put(DatabaseHelper.COLUMN_TEAM_B_GOALS, 0);
        db.insert(DatabaseHelper.TABLE_MATCHES, null, match1);

        // Match 2
        ContentValues match2 = new ContentValues();
        match2.put(DatabaseHelper.COLUMN_DATE, "22/11/2022");
        match2.put(DatabaseHelper.COLUMN_CITY, "כרמיאל");
        match2.put(DatabaseHelper.COLUMN_TEAM_A, "ביתר ירושלים");
        match2.put(DatabaseHelper.COLUMN_TEAM_B, "מכבי חיפה");
        match2.put(DatabaseHelper.COLUMN_TEAM_A_GOALS, 1);
        match2.put(DatabaseHelper.COLUMN_TEAM_B_GOALS, 1);
        db.insert(DatabaseHelper.TABLE_MATCHES, null, match2);

        // Match 3
        ContentValues match3 = new ContentValues();
        match3.put(DatabaseHelper.COLUMN_DATE, "29/11/2022");
        match3.put(DatabaseHelper.COLUMN_CITY, "חיפה");
        match3.put(DatabaseHelper.COLUMN_TEAM_A, "מכבי חיפה");
        match3.put(DatabaseHelper.COLUMN_TEAM_B, "ביתר ירושלים");
        match3.put(DatabaseHelper.COLUMN_TEAM_A_GOALS, 3);
        match3.put(DatabaseHelper.COLUMN_TEAM_B_GOALS, 1);
        db.insert(DatabaseHelper.TABLE_MATCHES, null, match3);

        // Match 4
        ContentValues match4 = new ContentValues();
        match4.put(DatabaseHelper.COLUMN_DATE, "05/12/2022");
        match4.put(DatabaseHelper.COLUMN_CITY, "נתניה");
        match4.put(DatabaseHelper.COLUMN_TEAM_A, "מכבי נתניה");
        match4.put(DatabaseHelper.COLUMN_TEAM_B, "מכבי ת\"א");
        match4.put(DatabaseHelper.COLUMN_TEAM_A_GOALS, 0);
        match4.put(DatabaseHelper.COLUMN_TEAM_B_GOALS, 2);
        db.insert(DatabaseHelper.TABLE_MATCHES, null, match4);

        // Match 5
        ContentValues match5 = new ContentValues();
        match5.put(DatabaseHelper.COLUMN_DATE, "12/12/2022");
        match5.put(DatabaseHelper.COLUMN_CITY, "באר שבע");
        match5.put(DatabaseHelper.COLUMN_TEAM_A, "הפועל באר שבע");
        match5.put(DatabaseHelper.COLUMN_TEAM_B, "מכבי חיפה");
        match5.put(DatabaseHelper.COLUMN_TEAM_A_GOALS, 2);
        match5.put(DatabaseHelper.COLUMN_TEAM_B_GOALS, 2);
        db.insert(DatabaseHelper.TABLE_MATCHES, null, match5);

        // Match 6
        ContentValues match6 = new ContentValues();
        match6.put(DatabaseHelper.COLUMN_DATE, "19/12/2022");
        match6.put(DatabaseHelper.COLUMN_CITY, "פתח תקווה");
        match6.put(DatabaseHelper.COLUMN_TEAM_A, "מכבי פתח תקווה");
        match6.put(DatabaseHelper.COLUMN_TEAM_B, "הפועל ת\"א");
        match6.put(DatabaseHelper.COLUMN_TEAM_A_GOALS, 1);
        match6.put(DatabaseHelper.COLUMN_TEAM_B_GOALS, 3);
        db.insert(DatabaseHelper.TABLE_MATCHES, null, match6);

        // Match 7
        ContentValues match7 = new ContentValues();
        match7.put(DatabaseHelper.COLUMN_DATE, "26/12/2022");
        match7.put(DatabaseHelper.COLUMN_CITY, "תל אביב");
        match7.put(DatabaseHelper.COLUMN_TEAM_A, "מכבי ת\"א");
        match7.put(DatabaseHelper.COLUMN_TEAM_B, "מכבי חיפה");
        match7.put(DatabaseHelper.COLUMN_TEAM_A_GOALS, 1);
        match7.put(DatabaseHelper.COLUMN_TEAM_B_GOALS, 0);
        db.insert(DatabaseHelper.TABLE_MATCHES, null, match7);

        // Match 8
        ContentValues match8 = new ContentValues();
        match8.put(DatabaseHelper.COLUMN_DATE, "02/01/2023");
        match8.put(DatabaseHelper.COLUMN_CITY, "הרצליה");
        match8.put(DatabaseHelper.COLUMN_TEAM_A, "מכבי הרצליה");
        match8.put(DatabaseHelper.COLUMN_TEAM_B, "ביתר ירושלים");
        match8.put(DatabaseHelper.COLUMN_TEAM_A_GOALS, 0);
        match8.put(DatabaseHelper.COLUMN_TEAM_B_GOALS, 1);
        db.insert(DatabaseHelper.TABLE_MATCHES, null, match8);
    }

    /**
     * Updates team statistics based on match results.
     * Calculates and updates statistics for all teams by processing
     * all matches in the database.
     * @param db The SQLiteDatabase instance to use for updates
     */
    private void updateStatistics(SQLiteDatabase db) {
        Log.d(TAG, "Calculating team statistics...");

        // Loop through all matches in the database and update stats manually
        Cursor cursor = db.query(DatabaseHelper.TABLE_MATCHES, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Extract match data
                @SuppressLint("Range") String teamA = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEAM_A));
                @SuppressLint("Range") String teamB = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEAM_B));
                @SuppressLint("Range") int teamAGoals = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEAM_A_GOALS));
                @SuppressLint("Range") int teamBGoals = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEAM_B_GOALS));

                // Update statistics for both teams
                updateTeamStats(db, teamA, teamAGoals, teamBGoals, teamAGoals > teamBGoals, teamAGoals == teamBGoals);
                updateTeamStats(db, teamB, teamBGoals, teamAGoals, teamBGoals > teamAGoals, teamAGoals == teamBGoals);

            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    /**
     * Updates statistics for a single team based on a match result.
     * Calculates and updates matches played, wins, draws, losses,
     * goals scored/against, and points.
     * @param db The SQLiteDatabase instance to use for updates
     * @param teamName The name of the team to update
     * @param goalsScored Goals scored by the team in this match
     * @param goalsAgainst Goals scored against the team in this match
     * @param isWin Whether the team won the match
     * @param isDraw Whether the match was a draw
     */
    private void updateTeamStats(SQLiteDatabase db, String teamName, int goalsScored, int goalsAgainst, boolean isWin, boolean isDraw) {
        // Get current team stats
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_TEAM_STATS,
                null,
                DatabaseHelper.COLUMN_TEAM_NAME + "=?",
                new String[]{teamName},
                null, null, null
        );

        ContentValues values = new ContentValues();

        if (cursor != null && cursor.moveToFirst()) {
            // Update existing team stats
            @SuppressLint("Range") int matchesPlayed = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_MATCHES_PLAYED)) + 1;
            @SuppressLint("Range") int wins = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WINS)) + (isWin ? 1 : 0);
            @SuppressLint("Range") int draws = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_DRAWS)) + (isDraw ? 1 : 0);
            @SuppressLint("Range") int losses = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_LOSSES));
            if (!isWin && !isDraw) losses += 1;

            @SuppressLint("Range") int totalGoalsScored = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_GOALS_SCORED)) + goalsScored;
            @SuppressLint("Range") int totalGoalsAgainst = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_GOALS_AGAINST)) + goalsAgainst;
            @SuppressLint("Range") int points = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_POINTS));

            // Calculate points (3 for win, 1 for draw)
            if (isWin) points += 3;
            else if (isDraw) points += 1;

            // Update values
            values.put(DatabaseHelper.COLUMN_MATCHES_PLAYED, matchesPlayed);
            values.put(DatabaseHelper.COLUMN_WINS, wins);
            values.put(DatabaseHelper.COLUMN_DRAWS, draws);
            values.put(DatabaseHelper.COLUMN_LOSSES, losses);
            values.put(DatabaseHelper.COLUMN_GOALS_SCORED, totalGoalsScored);
            values.put(DatabaseHelper.COLUMN_GOALS_AGAINST, totalGoalsAgainst);
            values.put(DatabaseHelper.COLUMN_POINTS, points);

            // Update in database
            db.update(
                    DatabaseHelper.TABLE_TEAM_STATS,
                    values,
                    DatabaseHelper.COLUMN_TEAM_NAME + "=?",
                    new String[]{teamName}
            );

            cursor.close();
        }
    }
}