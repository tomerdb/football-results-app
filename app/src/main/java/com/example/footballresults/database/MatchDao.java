package com.example.footballresults.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.footballresults.models.Match;
import com.example.footballresults.utils.DateFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for handling Match-related database operations.
 * This class provides methods for creating, reading, updating, and deleting
 * match records in the database, as well as querying match data in various ways.
 */
public class MatchDao {
    private SQLiteDatabase database;
    private final DatabaseHelper dbHelper;

    /**
     * Constructor that initializes the database helper.
     * @param context The application context
     */
    public MatchDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Opens a writable database connection.
     * @throws SQLException if the database cannot be opened
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Closes the database connection.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Retrieves a match by its ID.
     * @param matchId The ID of the match to retrieve
     * @return The Match object if found, null otherwise
     */
    public Match getMatchById(long matchId) {
        Match match = null;
        String selection = DatabaseHelper.COLUMN_MATCH_ID + " = ?";
        String[] selectionArgs = {String.valueOf(matchId)};

        Cursor cursor = database.query(DatabaseHelper.TABLE_MATCHES, null, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            match = cursorToMatch(cursor);
            cursor.close();
        }
        return match;
    }

    /**
     * Adds a new match to the database.
     * @param match The Match object to add
     * @return The ID of the newly inserted match, or -1 if the insertion failed
     */
    public long addMatch(Match match) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DATE, match.getDate());
        values.put(DatabaseHelper.COLUMN_CITY, match.getCity());
        values.put(DatabaseHelper.COLUMN_TEAM_A, match.getTeamA());
        values.put(DatabaseHelper.COLUMN_TEAM_B, match.getTeamB());
        values.put(DatabaseHelper.COLUMN_TEAM_A_GOALS, match.getTeamAGoals());
        values.put(DatabaseHelper.COLUMN_TEAM_B_GOALS, match.getTeamBGoals());

        return database.insert(DatabaseHelper.TABLE_MATCHES, null, values);
    }

    /**
     * Retrieves all matches sorted by date in descending order (most recent first).
     * @return List of all matches sorted by date
     */
    public List<Match> getAllMatchesSortedByDate() {
        List<Match> matches = new ArrayList<>();

        // First, get all matches
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_MATCHES,
                null,
                null,
                null,
                null,
                null,
                null  // No SQL sorting here
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Match match = cursorToMatch(cursor);
                matches.add(match);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Sort matches by date using a custom comparator with DateFormatter
        Collections.sort(matches, (match1, match2) -> {
            // Convert the date strings to Date objects using DateFormatter
            Date date1 = DateFormatter.parseDate(match1.getDate());
            Date date2 = DateFormatter.parseDate(match2.getDate());

            // Handle null dates (if parsing fails)
            if (date1 == null && date2 == null) return 0;
            if (date1 == null) return 1;
            if (date2 == null) return -1;

            // Most recent dates first (descending order)
            return date2.compareTo(date1);
        });

        return matches;
    }

    /**
     * Updates an existing match in the database.
     * @param match The Match object containing updated data
     * @return true if the update was successful, false otherwise
     */
    public boolean updateMatch(Match match) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DATE, match.getDate());
        values.put(DatabaseHelper.COLUMN_CITY, match.getCity());
        values.put(DatabaseHelper.COLUMN_TEAM_A, match.getTeamA());
        values.put(DatabaseHelper.COLUMN_TEAM_B, match.getTeamB());
        values.put(DatabaseHelper.COLUMN_TEAM_A_GOALS, match.getTeamAGoals());
        values.put(DatabaseHelper.COLUMN_TEAM_B_GOALS, match.getTeamBGoals());

        return database.update(DatabaseHelper.TABLE_MATCHES, values,
                DatabaseHelper.COLUMN_MATCH_ID + " = ?",
                new String[]{String.valueOf(match.getId())}) > 0;
    }

    /**
     * Deletes a match from the database.
     * @param matchId The ID of the match to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteMatch(long matchId) {
        return database.delete(DatabaseHelper.TABLE_MATCHES,
                DatabaseHelper.COLUMN_MATCH_ID + " = ?",
                new String[]{String.valueOf(matchId)}) > 0;
    }

    /**
     * Retrieves all matches from the database.
     * @return List of all matches
     */
    public List<Match> getAllMatches() {
        List<Match> matches = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_MATCHES, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Match match = cursorToMatch(cursor);
                matches.add(match);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return matches;
    }

    /**
     * Retrieves all matches involving a specific team.
     * @param teamName The name of the team to search for
     * @return List of matches where the specified team played
     */
    public List<Match> getMatchesByTeam(String teamName) {
        List<Match> matches = new ArrayList<>();
        String selection = DatabaseHelper.COLUMN_TEAM_A + " = ? OR " + DatabaseHelper.COLUMN_TEAM_B + " = ?";
        String[] selectionArgs = {teamName, teamName};

        Cursor cursor = database.query(DatabaseHelper.TABLE_MATCHES, null, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Match match = cursorToMatch(cursor);
                matches.add(match);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return matches;
    }

    /**
     * Converts a database cursor to a Match object.
     * @param cursor The cursor containing match data
     * @return A Match object populated with the cursor's data
     */
    @SuppressLint("Range")
    private Match cursorToMatch(Cursor cursor) {
        Match match = new Match();
        match.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_MATCH_ID)));
        match.setDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE)));
        match.setCity(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CITY)));
        match.setTeamA(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEAM_A)));
        match.setTeamB(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEAM_B)));
        match.setTeamAGoals(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEAM_A_GOALS)));
        match.setTeamBGoals(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEAM_B_GOALS)));
        return match;
    }
}