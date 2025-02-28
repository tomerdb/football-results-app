package com.example.footballresults.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.footballresults.models.Match;

import java.util.ArrayList;
import java.util.List;

public class MatchDao {
    private SQLiteDatabase database;
    private final DatabaseHelper dbHelper;

    public MatchDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

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

    public boolean deleteMatch(long matchId) {
        return database.delete(DatabaseHelper.TABLE_MATCHES,
                DatabaseHelper.COLUMN_MATCH_ID + " = ?",
                new String[]{String.valueOf(matchId)}) > 0;
    }

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