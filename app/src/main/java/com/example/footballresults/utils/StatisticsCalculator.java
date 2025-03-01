package com.example.footballresults.utils;

import android.content.Context;

import com.example.footballresults.database.MatchDao;
import com.example.footballresults.database.TeamStatsDao;
import com.example.footballresults.models.Match;
import com.example.footballresults.models.TeamStats;

import java.util.List;

public class StatisticsCalculator {
    private Context context;
    private MatchDao matchDao;
    private TeamStatsDao teamStatsDao;

    public StatisticsCalculator(Context context) {
        this.context = context;
        this.matchDao = new MatchDao(context);
        this.teamStatsDao = new TeamStatsDao(context);
    }

    public void removeMatchStats(Match match) {
        matchDao.open();
        teamStatsDao.open();

        // Get team stats
        TeamStats teamAStats = teamStatsDao.getTeamStatsByName(match.getTeamA());
        TeamStats teamBStats = teamStatsDao.getTeamStatsByName(match.getTeamB());

        if (teamAStats != null && teamBStats != null) {
            // Update matches played
            teamAStats.setMatchesPlayed(teamAStats.getMatchesPlayed() - 1);
            teamBStats.setMatchesPlayed(teamBStats.getMatchesPlayed() - 1);

            // Update goals
            teamAStats.setGoalsScored(teamAStats.getGoalsScored() - match.getTeamAGoals());
            teamBStats.setGoalsScored(teamBStats.getGoalsScored() - match.getTeamBGoals());

            // Update wins, draws, losses, and points
            if (match.getTeamAGoals() > match.getTeamBGoals()) {
                // Team A won
                teamAStats.setWins(teamAStats.getWins() - 1);
                teamAStats.setPoints(teamAStats.getPoints() - 3);
                teamBStats.setLosses(teamBStats.getLosses() - 1);
            } else if (match.getTeamAGoals() < match.getTeamBGoals()) {
                // Team B won
                teamBStats.setWins(teamBStats.getWins() - 1);
                teamBStats.setPoints(teamBStats.getPoints() - 3);
                teamAStats.setLosses(teamAStats.getLosses() - 1);
            } else {
                // Draw
                teamAStats.setDraws(teamAStats.getDraws() - 1);
                teamBStats.setDraws(teamBStats.getDraws() - 1);
                teamAStats.setPoints(teamAStats.getPoints() - 1);
                teamBStats.setPoints(teamBStats.getPoints() - 1);
            }

            // Check if team A stats are all zero
            if (isTeamStatsEmpty(teamAStats)) {
                teamStatsDao.deleteTeamStats(teamAStats.getTeamName());
            } else {
                // Save updated stats
                teamStatsDao.updateTeamStats(teamAStats);
            }

            // Check if team B stats are all zero
            if (isTeamStatsEmpty(teamBStats)) {
                teamStatsDao.deleteTeamStats(teamBStats.getTeamName());
            } else {
                // Save updated stats
                teamStatsDao.updateTeamStats(teamBStats);
            }
        }

        matchDao.close();
        teamStatsDao.close();
    }

    // Helper method to check if all team stats are zero
    private boolean isTeamStatsEmpty(TeamStats stats) {
        return stats.getMatchesPlayed() == 0 &&
                stats.getWins() == 0 &&
                stats.getDraws() == 0 &&
                stats.getLosses() == 0 &&
                stats.getGoalsScored() == 0 &&
                stats.getPoints() == 0;
    }
    public void calculateStatsForMatch(Match match) {
        matchDao.open();
        teamStatsDao.open();

        // Get team stats or create if they don't exist
        TeamStats teamAStats = teamStatsDao.getTeamStatsByName(match.getTeamA());
        if (teamAStats == null) {
            teamAStats = new TeamStats(match.getTeamA());
            teamStatsDao.addTeam(teamAStats);
        }

        TeamStats teamBStats = teamStatsDao.getTeamStatsByName(match.getTeamB());
        if (teamBStats == null) {
            teamBStats = new TeamStats(match.getTeamB());
            teamStatsDao.addTeam(teamBStats);
        }

        // Update matches played
        teamAStats.setMatchesPlayed(teamAStats.getMatchesPlayed() + 1);
        teamBStats.setMatchesPlayed(teamBStats.getMatchesPlayed() + 1);

        // Update goals
        teamAStats.setGoalsScored(teamAStats.getGoalsScored() + match.getTeamAGoals());
        teamBStats.setGoalsScored(teamBStats.getGoalsScored() + match.getTeamBGoals());

        // Update wins, draws, losses, and points
        if (match.getTeamAGoals() > match.getTeamBGoals()) {
            // Team A wins
            teamAStats.setWins(teamAStats.getWins() + 1);
            teamAStats.setPoints(teamAStats.getPoints() + 3);
            teamBStats.setLosses(teamBStats.getLosses() + 1);
        } else if (match.getTeamAGoals() < match.getTeamBGoals()) {
            // Team B wins
            teamBStats.setWins(teamBStats.getWins() + 1);
            teamBStats.setPoints(teamBStats.getPoints() + 3);
            teamAStats.setLosses(teamAStats.getLosses() + 1);
        } else {
            // Draw
            teamAStats.setDraws(teamAStats.getDraws() + 1);
            teamBStats.setDraws(teamBStats.getDraws() + 1);
            teamAStats.setPoints(teamAStats.getPoints() + 1);
            teamBStats.setPoints(teamBStats.getPoints() + 1);
        }

        // Save updated stats
        teamStatsDao.updateTeamStats(teamAStats);
        teamStatsDao.updateTeamStats(teamBStats);

        matchDao.close();
        teamStatsDao.close();
    }

    public void recalculateAllStats() {
        matchDao.open();
        teamStatsDao.open();

        // Reset all team stats
        List<TeamStats> allTeams = teamStatsDao.getAllTeamStats();
        for (TeamStats team : allTeams) {
            team.setMatchesPlayed(0);
            team.setWins(0);
            team.setDraws(0);
            team.setLosses(0);
            team.setGoalsScored(0);
            team.setPoints(0);
            teamStatsDao.updateTeamStats(team);
        }

        // Recalculate based on all matches
        List<Match> allMatches = matchDao.getAllMatches();
        for (Match match : allMatches) {
            calculateStatsForMatch(match);
        }

        matchDao.close();
        teamStatsDao.close();
    }
}