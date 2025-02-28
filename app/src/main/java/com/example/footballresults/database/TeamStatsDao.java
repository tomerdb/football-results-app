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

public class TeamStatsDao {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public TeamStatsDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addTeam(TeamStats team) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TEAM_NAME, team.getTeamName());
        values.put(DatabaseHelper.COLUMN_MATCHES_PLAYED, team.getMatchesPlayed());
        values.put(DatabaseHelper.COLUMN_WINS, team.getWins());
        values.put(DatabaseHelper.COLUMN_DRAWS, team.getDraws());
        values.put(DatabaseHelper.COLUMN_LOSSES, team.getLosses());
        values.put(DatabaseHelper.COLUMN_GOALS_SCORED, team.getGoalsScored());
        values.put(DatabaseHelper.COLUMN_GOALS_AGAINST, team.getGoalsAgainst());
        values.put(DatabaseHelper.COLUMN_POINTS, team.getPoints());

        return database.insert(DatabaseHelper.TABLE_TEAM_STATS, null, values);
    }

    public boolean updateTeamStats(TeamStats team) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MATCHES_PLAYED, team.getMatchesPlayed());
        values.put(DatabaseHelper.COLUMN_WINS, team.getWins());
        values.put(DatabaseHelper.COLUMN_DRAWS, team.getDraws());
        values.put(DatabaseHelper.COLUMN_LOSSES, team.getLosses());
        values.put(DatabaseHelper.COLUMN_GOALS_SCORED, team.getGoalsScored());
        values.put(DatabaseHelper.COLUMN_GOALS_AGAINST, team.getGoalsAgainst());
        values.put(DatabaseHelper.COLUMN_POINTS, team.getPoints());

        return database.update(DatabaseHelper.TABLE_TEAM_STATS, values,
                DatabaseHelper.COLUMN_TEAM_NAME + " = ?",
                new String[]{team.getTeamName()}) > 0;
    }

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
        teamStats.setGoalsAgainst(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_GOALS_AGAINST)));
        teamStats.setPoints(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_POINTS)));
        return teamStats;
    }

    public boolean deleteTeamStats(String teamName) {
        return database.delete(DatabaseHelper.TABLE_TEAM_STATS,
                DatabaseHelper.COLUMN_TEAM_NAME + " = ?",
                new String[]{teamName}) > 0;
    }
}