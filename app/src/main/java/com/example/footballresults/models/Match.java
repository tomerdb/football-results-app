package com.example.footballresults.models;
public class Match {
    private long id;
    private String date;
    private String city;
    private String teamA;
    private String teamB;
    private int teamAGoals;
    private int teamBGoals;

    // Constructor, getters, and setters
    public Match() {
    }

    public Match(String date, String city, String teamA, String teamB, int teamAGoals, int teamBGoals) {
        this.date = date;
        this.city = city;
        this.teamA = teamA;
        this.teamB = teamB;
        this.teamAGoals = teamAGoals;
        this.teamBGoals = teamBGoals;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public int getTeamAGoals() {
        return teamAGoals;
    }

    public void setTeamAGoals(int teamAGoals) {
        this.teamAGoals = teamAGoals;
    }

    public int getTeamBGoals() {
        return teamBGoals;
    }

    public void setTeamBGoals(int teamBGoals) {
        this.teamBGoals = teamBGoals;
    }
}