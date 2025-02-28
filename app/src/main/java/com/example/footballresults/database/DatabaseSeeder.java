package com.example.footballresults.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseSeeder {
    private final Context context;
    private final DatabaseHelper dbHelper;

    public DatabaseSeeder(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
    }

    public void seedDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Check if database already has data
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_MATCHES, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        // Only seed if database is empty
        if (count == 0) {
            insertSampleData(db);
        }

        db.close();
    }

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
        } finally {
            db.endTransaction();
        }
    }

    private void insertTeams(SQLiteDatabase db) {
        String[] teams = {"מכבי ת\"א", "הפועל ת\"א", "ביתר ירושלים", "מכבי חיפה", "מכבי נתניה"};

        for (String team : teams) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_TEAM_NAME, team);
            db.insert(DatabaseHelper.TABLE_TEAM_STATS, null, values);
        }
    }

    private void insertMatches(SQLiteDatabase db) {
        // Sample match data using ContentValues
        ContentValues match1 = new ContentValues();
        match1.put(DatabaseHelper.COLUMN_DATE, "22/11/2022");
        match1.put(DatabaseHelper.COLUMN_CITY, "תל אביב");
        match1.put(DatabaseHelper.COLUMN_TEAM_A, "מכבי ת\"א");
        match1.put(DatabaseHelper.COLUMN_TEAM_B, "הפועל ת\"א");
        match1.put(DatabaseHelper.COLUMN_TEAM_A_GOALS, 2);
        match1.put(DatabaseHelper.COLUMN_TEAM_B_GOALS, 0);
        db.insert(DatabaseHelper.TABLE_MATCHES, null, match1);

    }

    private void updateStatistics(SQLiteDatabase db) {
        // Similar to calculateInitialStats from previous example
        // But could also use your StatisticsCalculator here
    }
}