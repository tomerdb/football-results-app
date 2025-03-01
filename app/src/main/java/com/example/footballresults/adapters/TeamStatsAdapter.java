package com.example.footballresults.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballresults.R;
import com.example.footballresults.models.TeamStats;

import java.util.List;

/**
 * RecyclerView adapter for displaying team statistics in a tabular format.
 * This adapter handles the presentation of team statistics including matches played,
 * wins, draws, losses, goals scored, and total points. It also implements
 * alternating row colors for better readability.
 */
public class TeamStatsAdapter extends RecyclerView.Adapter<TeamStatsAdapter.TeamStatsViewHolder> {
    /** List of team statistics to display */
    private List<TeamStats> teamStatsList;
    
    /** Context for resource access */
    private Context context;

    /**
     * Constructs a new TeamStatsAdapter.
     * @param context The context for inflating layouts
     * @param teamStatsList The list of team statistics to display
     */
    public TeamStatsAdapter(Context context, List<TeamStats> teamStatsList) {
        this.context = context;
        this.teamStatsList = teamStatsList;
    }

    @NonNull
    @Override
    public TeamStatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_team_stats, parent, false);
        return new TeamStatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamStatsViewHolder holder, int position) {
        TeamStats teamStats = teamStatsList.get(position);

        // Set the statistics values
        holder.tvTeamName.setText(teamStats.getTeamName());
        holder.tvMatchesPlayed.setText(String.valueOf(teamStats.getMatchesPlayed()));
        holder.tvWins.setText(String.valueOf(teamStats.getWins()));
        holder.tvDraws.setText(String.valueOf(teamStats.getDraws()));
        holder.tvLosses.setText(String.valueOf(teamStats.getLosses()));
        holder.tvGoalsScored.setText(String.valueOf(teamStats.getGoalsScored()));
        holder.tvPoints.setText(String.valueOf(teamStats.getPoints()));

        // Apply alternating row colors for better readability
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF")); // White
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#F5F5F5")); // Light gray
        }
    }

    @Override
    public int getItemCount() {
        return teamStatsList.size();
    }

    /**
     * ViewHolder class for team statistics items.
     * Holds references to all views within a team statistics row layout.
     */
    class TeamStatsViewHolder extends RecyclerView.ViewHolder {
        /** TextViews for displaying team statistics */
        TextView tvTeamName, tvMatchesPlayed, tvWins, tvDraws, tvLosses, tvGoalsScored, tvPoints;

        /**
         * Constructs a new TeamStatsViewHolder.
         * Initializes all view references for displaying team statistics.
         * @param itemView The View object representing a team statistics row
         */
        public TeamStatsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTeamName = itemView.findViewById(R.id.tv_team_name);
            tvMatchesPlayed = itemView.findViewById(R.id.tv_matches_played);
            tvWins = itemView.findViewById(R.id.tv_wins);
            tvDraws = itemView.findViewById(R.id.tv_draws);
            tvLosses = itemView.findViewById(R.id.tv_losses);
            tvGoalsScored = itemView.findViewById(R.id.tv_goals_scored);
            tvPoints = itemView.findViewById(R.id.tv_points);
        }
    }
}