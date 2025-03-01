package com.example.footballresults.models;

/**
 * Model class representing a football match.
 * This class stores all relevant information about a single football match,
 * including the date, location, teams involved, and the final score.
 */
public class Match {
    /** Unique identifier for the match */
    private long id;
    
    /** Date when the match was played (in DD/MM/YYYY format) */
    private String date;
    
    /** City where the match was played */
    private String city;
    
    /** Name of the first team */
    private String teamA;
    
    /** Name of the second team */
    private String teamB;
    
    /** Number of goals scored by team A */
    private int teamAGoals;
    
    /** Number of goals scored by team B */
    private int teamBGoals;

    /**
     * Default constructor for creating an empty match object.
     */
    public Match() {
    }

    /**
     * Constructor for creating a new match with all required details.
     *
     * @param date The date of the match (DD/MM/YYYY)
     * @param city The city where the match was played
     * @param teamA The name of the first team
     * @param teamB The name of the second team
     * @param teamAGoals Number of goals scored by team A
     * @param teamBGoals Number of goals scored by team B
     */
    public Match(String date, String city, String teamA, String teamB, int teamAGoals, int teamBGoals) {
        this.date = date;
        this.city = city;
        this.teamA = teamA;
        this.teamB = teamB;
        this.teamAGoals = teamAGoals;
        this.teamBGoals = teamBGoals;
    }

    // Getters and Setters with documentation

    /**
     * Gets the unique identifier of the match.
     * @return The match ID
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the match.
     * @param id The match ID to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the date when the match was played.
     * @return The match date in DD/MM/YYYY format
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date when the match was played.
     * @param date The match date in DD/MM/YYYY format
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the city where the match was played.
     * @return The city name
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city where the match was played.
     * @param city The city name
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the name of team A.
     * @return Team A's name
     */
    public String getTeamA() {
        return teamA;
    }

    /**
     * Sets the name of team A.
     * @param teamA Team A's name
     */
    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    /**
     * Gets the name of team B.
     * @return Team B's name
     */
    public String getTeamB() {
        return teamB;
    }

    /**
     * Sets the name of team B.
     * @param teamB Team B's name
     */
    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    /**
     * Gets the number of goals scored by team A.
     * @return Team A's goals
     */
    public int getTeamAGoals() {
        return teamAGoals;
    }

    /**
     * Sets the number of goals scored by team A.
     * @param teamAGoals Team A's goals
     */
    public void setTeamAGoals(int teamAGoals) {
        this.teamAGoals = teamAGoals;
    }

    /**
     * Gets the number of goals scored by team B.
     * @return Team B's goals
     */
    public int getTeamBGoals() {
        return teamBGoals;
    }

    /**
     * Sets the number of goals scored by team B.
     * @param teamBGoals Team B's goals
     */
    public void setTeamBGoals(int teamBGoals) {
        this.teamBGoals = teamBGoals;
    }
}