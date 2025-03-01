package com.example.footballresults.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite database helper class for managing the football results database.
 * This class handles database creation, schema management, and version upgrades.
 * It defines the structure for two main tables:
 * 1. Matches table - stores individual match results
 * 2. Team Stats table - stores cumulative team statistics
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    /** Database name and version */
    private static final String DATABASE_NAME = "football.db";
    private static final int DATABASE_VERSION = 1;

    /** Match table constants */
    public static final String TABLE_MATCHES = "matches";
    public static final String COLUMN_MATCH_ID = "match_id";
    public static final String COLUMN_DATE = "match_date";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_TEAM_A = "team_a";
    public static final String COLUMN_TEAM_B = "team_b";
    public static final String COLUMN_TEAM_A_GOALS = "team_a_goals";
    public static final String COLUMN_TEAM_B_GOALS = "team_b_goals";

    /** Team Stats table constants */
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

    /** SQL statement to create the matches table */
    private static final String CREATE_MATCHES_TABLE = "CREATE TABLE " + TABLE_MATCHES + "("
            + COLUMN_MATCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DATE + " TEXT NOT NULL, "
            + COLUMN_CITY + " TEXT NOT NULL, "
            + COLUMN_TEAM_A + " TEXT NOT NULL, "
            + COLUMN_TEAM_B + " TEXT NOT NULL, "
            + COLUMN_TEAM_A_GOALS + " INTEGER NOT NULL, "
            + COLUMN_TEAM_B_GOALS + " INTEGER NOT NULL"
            + ")";

    /** SQL statement to create the team stats table */
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

    /**
     * Constructor for DatabaseHelper.
     * @param context The application context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     * Creates both the matches and team stats tables.
     * @param db The database being created
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MATCHES_TABLE);
        db.execSQL(CREATE_TEAM_STATS_TABLE);
    }

    /**
     * Called when the database needs to be upgraded to a new version.
     * Currently implements a simple drop and recreate strategy.
     * @param db The database being upgraded
     * @param oldVersion The old database version
     * @param newVersion The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM_STATS);
        // Recreate tables
        onCreate(db);
    }
}