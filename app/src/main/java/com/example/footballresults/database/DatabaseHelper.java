package com.example.footballresults.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "football.db";
    private static final int DATABASE_VERSION = 1;

    // Match table
    public static final String TABLE_MATCHES = "matches";
    public static final String COLUMN_MATCH_ID = "match_id";
    public static final String COLUMN_DATE = "match_date";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_TEAM_A = "team_a";
    public static final String COLUMN_TEAM_B = "team_b";
    public static final String COLUMN_TEAM_A_GOALS = "team_a_goals";
    public static final String COLUMN_TEAM_B_GOALS = "team_b_goals";

    // Team Stats table
    public static final String TABLE_TEAM_STATS = "team_stats";
    public static final String COLUMN_TEAM_ID = "team_id";
    public static final String COLUMN_TEAM_NAME = "team_name";
    public static final String COLUMN_MATCHES_PLAYED = "matches_played";
    public static final String COLUMN_WINS = "wins";
    public static final String COLUMN_DRAWS = "draws";
    public static final String COLUMN_LOSSES = "losses";
    public static final String COLUMN_GOALS_SCORED = "goals_scored";
    public static final String COLUMN_GOALS_AGAINST = "goals_against";
    public static final String COLUMN_POINTS = "points";

    // Create table statements
    private static final String CREATE_MATCHES_TABLE = "CREATE TABLE " + TABLE_MATCHES + "("
            + COLUMN_MATCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DATE + " TEXT NOT NULL, "
            + COLUMN_CITY + " TEXT NOT NULL, "
            + COLUMN_TEAM_A + " TEXT NOT NULL, "
            + COLUMN_TEAM_B + " TEXT NOT NULL, "
            + COLUMN_TEAM_A_GOALS + " INTEGER NOT NULL, "
            + COLUMN_TEAM_B_GOALS + " INTEGER NOT NULL"
            + ")";

    private static final String CREATE_TEAM_STATS_TABLE = "CREATE TABLE " + TABLE_TEAM_STATS + "("
            + COLUMN_TEAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TEAM_NAME + " TEXT UNIQUE NOT NULL, "
            + COLUMN_MATCHES_PLAYED + " INTEGER DEFAULT 0, "
            + COLUMN_WINS + " INTEGER DEFAULT 0, "
            + COLUMN_DRAWS + " INTEGER DEFAULT 0, "
            + COLUMN_LOSSES + " INTEGER DEFAULT 0, "
            + COLUMN_GOALS_SCORED + " INTEGER DEFAULT 0, "
            + COLUMN_GOALS_AGAINST + " INTEGER DEFAULT 0, "
            + COLUMN_POINTS + " INTEGER DEFAULT 0"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MATCHES_TABLE);
        db.execSQL(CREATE_TEAM_STATS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM_STATS);
        onCreate(db);
    }
}