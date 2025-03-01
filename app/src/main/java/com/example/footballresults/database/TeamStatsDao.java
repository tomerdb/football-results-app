package com.example.footballresults.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.footballresults.models.TeamStats;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for handling TeamStats-related database operations.
 * This class provides methods for creating, reading, updating, and deleting
 * team statistics records in the database. It also includes functionality
 * for retrieving sorted statistics and managing team data.
 */
public class TeamStatsDao {
    /** Database instance for executing SQL operations */
    private SQLiteDatabase database;
    
    /** Database helper for managing connections */
    private DatabaseHelper dbHelper;

    /**
     * Constructs a new TeamStatsDao.
     * @param context The application context
     */
    public TeamStatsDao(Context context) {
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
     * Adds a new team with initial statistics to the database.
     * @param team The TeamStats object containing the team's data
     * @return The ID of the newly inserted team record
     */
    public long addTeam(TeamStats team) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TEAM_NAME, team.getTeamName());
        values.put(DatabaseHelper.COLUMN_MATCHES_PLAYED, team.getMatchesPlayed());
        values.put(DatabaseHelper.COLUMN_WINS, team.getWins());
        values.put(DatabaseHelper.COLUMN_DRAWS, team.getDraws());
        values.put(DatabaseHelper.COLUMN_LOSSES, team.getLosses());
        values.put(DatabaseHelper.COLUMN_GOALS_SCORED, team.getGoalsScored());
        values.put(DatabaseHelper.COLUMN_POINTS, team.getPoints());

        return database.insert(DatabaseHelper.TABLE_TEAM_STATS, null, values);
    }

    /**
     * Updates the statistics for an existing team.
     * @param team The TeamStats object containing updated statistics
     * @return true if the update was successful, false otherwise
     */
    public boolean updateTeamStats(TeamStats team) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MATCHES_PLAYED, team.getMatchesPlayed());
        values.put(DatabaseHelper.COLUMN_WINS, team.getWins());
        values.put(DatabaseHelper.COLUMN_DRAWS, team.getDraws());
        values.put(DatabaseHelper.COLUMN_LOSSES, team.getLosses());
        values.put(DatabaseHelper.COLUMN_GOALS_SCORED, team.getGoalsScored());
        values.put(DatabaseHelper.COLUMN_POINTS, team.getPoints());

        return database.update(DatabaseHelper.TABLE_TEAM_STATS, values,
                DatabaseHelper.COLUMN_TEAM_NAME + " = ?",
                new String[]{team.getTeamName()}) > 0;
    }

    /**
     * Retrieves team statistics by team name.
     * @param teamName The name of the team to retrieve
     * @return The TeamStats object if found, null otherwise
     */
    public TeamStats getTeamStatsByName(String teamName) {
        TeamStats teamStats = null;
        String selection = DatabaseHelper.COLUMN_TEAM_NAME + " = ?";
        String[] selectionArgs = {teamName};

        Cursor cursor = database.query(DatabaseHelper.TABLE_TEAM_STATS, null, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            teamStats = cursorToTeamStats(cursor);
            cursor.close();
        }
        return teamStats;
    }

    /**
     * Retrieves all team statistics sorted by points.
     * @param ascending If true, sorts in ascending order; if false, in descending order
     * @return List of all team statistics sorted by points
     */
    public List<TeamStats> getAllTeamStatsSorted(boolean ascending) {
        List<TeamStats> teamStatsList = new ArrayList<>();
        String orderBy = DatabaseHelper.COLUMN_POINTS + (ascending ? " ASC" : " DESC");

        Cursor cursor = database.query(DatabaseHelper.TABLE_TEAM_STATS, null, null, null, null, null, orderBy);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                TeamStats teamStats = cursorToTeamStats(cursor);
                teamStatsList.add(teamStats);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return teamStatsList;
    }

    /**
     * Retrieves all team statistics sorted by points in descending order.
     * @return List of all team statistics
     */
    public List<TeamStats> getAllTeamStats() {
        List<TeamStats> teamStatsList = new ArrayList<>();
        String orderBy = DatabaseHelper.COLUMN_POINTS + " DESC";

        Cursor cursor = database.query(DatabaseHelper.TABLE_TEAM_STATS, null, null, null, null, null, orderBy);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                TeamStats teamStats = cursorToTeamStats(cursor);
                teamStatsList.add(teamStats);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return teamStatsList;
    }

    /**
     * Converts a database cursor to a TeamStats object.
     * @param cursor The cursor containing team statistics data
     * @return A TeamStats object populated with the cursor's data
     */
    @SuppressLint("Range")
    private TeamStats cursorToTeamStats(Cursor cursor) {
        TeamStats teamStats = new TeamStats();
        teamStats.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEAM_ID)));
        teamStats.setTeamName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TEAM_NAME)));
        teamStats.setMatchesPlayed(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_MATCHES_PLAYED)));
        teamStats.setWins(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_WINS)));
        teamStats.setDraws(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_DRAWS)));
        teamStats.setLosses(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_LOSSES)));
        teamStats.setGoalsScored(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_GOALS_SCORED)));
        teamStats.setPoints(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_POINTS)));
        return teamStats;
    }

    /**
     * Deletes a team's statistics from the database.
     * @param teamName The name of the team to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteTeamStats(String teamName) {
        return database.delete(DatabaseHelper.TABLE_TEAM_STATS,
                DatabaseHelper.COLUMN_TEAM_NAME + " = ?",
                new String[]{teamName}) > 0;
    }
}