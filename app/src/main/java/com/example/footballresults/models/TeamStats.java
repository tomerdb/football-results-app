package com.example.footballresults.models;

/**
 * Model class representing a team's statistics in the football league.
 * This class maintains cumulative statistics for a team, including their
 * match results, goals scored, and total points earned.
 */
public class TeamStats {
    /** Unique identifier for the team stats record */
    private long id;
    
    /** Name of the team */
    private String teamName;
    
    /** Total number of matches played by the team */
    private int matchesPlayed;
    
    /** Number of matches won by the team */
    private int wins;
    
    /** Number of matches drawn by the team */
    private int draws;
    
    /** Number of matches lost by the team */
    private int losses;
    
    /** Total number of goals scored by the team */
    private int goalsScored;
    
    /** Total points earned by the team (3 for win, 1 for draw, 0 for loss) */
    private int points;

    /**
     * Default constructor for creating an empty team stats object.
     */
    public TeamStats() {
    }

    /**
     * Constructor for creating a new team stats record.
     * Initializes all statistics to zero.
     *
     * @param teamName The name of the team
     */
    public TeamStats(String teamName) {
        this.teamName = teamName;
        this.matchesPlayed = 0;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.goalsScored = 0;
        this.points = 0;
    }

    /**
     * Gets the unique identifier of the team stats record.
     * @return The team stats ID
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the team stats record.
     * @param id The team stats ID to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the name of the team.
     * @return The team name
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * Sets the name of the team.
     * @param teamName The team name to set
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * Gets the total number of matches played by the team.
     * @return Number of matches played
     */
    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    /**
     * Sets the total number of matches played by the team.
     * @param matchesPlayed Number of matches played to set
     */
    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    /**
     * Gets the number of matches won by the team.
     * @return Number of wins
     */
    public int getWins() {
        return wins;
    }

    /**
     * Sets the number of matches won by the team.
     * @param wins Number of wins to set
     */
    public void setWins(int wins) {
        this.wins = wins;
    }

    /**
     * Gets the number of matches drawn by the team.
     * @return Number of draws
     */
    public int getDraws() {
        return draws;
    }

    /**
     * Sets the number of matches drawn by the team.
     * @param draws Number of draws to set
     */
    public void setDraws(int draws) {
        this.draws = draws;
    }

    /**
     * Gets the number of matches lost by the team.
     * @return Number of losses
     */
    public int getLosses() {
        return losses;
    }

    /**
     * Sets the number of matches lost by the team.
     * @param losses Number of losses to set
     */
    public void setLosses(int losses) {
        this.losses = losses;
    }

    /**
     * Gets the total number of goals scored by the team.
     * @return Number of goals scored
     */
    public int getGoalsScored() {
        return goalsScored;
    }

    /**
     * Sets the total number of goals scored by the team.
     * @param goalsScored Number of goals scored to set
     */
    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    /**
     * Gets the total points earned by the team.
     * Points are calculated as: 3 points for a win, 1 point for a draw, 0 points for a loss.
     * @return Total points earned
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets the total points earned by the team.
     * @param points Total points to set
     */
    public void setPoints(int points) {
        this.points = points;
    }
}