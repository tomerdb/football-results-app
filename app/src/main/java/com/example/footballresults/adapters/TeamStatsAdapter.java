package com.example.footballresults.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballresults.R;
import com.example.footballresults.models.TeamStats;

import java.util.List;

public class TeamStatsAdapter extends RecyclerView.Adapter<TeamStatsAdapter.TeamStatsViewHolder> {
    private List<TeamStats> teamStatsList;
    private Context context;

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
        holder.tvTeamName.setText(teamStats.getTeamName());
        holder.tvMatchesPlayed.setText(String.valueOf(teamStats.getMatchesPlayed()));
        holder.tvWins.setText(String.valueOf(teamStats.getWins()));
        holder.tvDraws.setText(String.valueOf(teamStats.getDraws()));
        holder.tvLosses.setText(String.valueOf(teamStats.getLosses()));
        holder.tvGoalsScored.setText(String.valueOf(teamStats.getGoalsScored()));
        holder.tvGoalsAgainst.setText(String.valueOf(teamStats.getGoalsAgainst()));
        holder.tvPoints.setText(String.valueOf(teamStats.getPoints()));
    }

    @Override
    public int getItemCount() {
        return teamStatsList.size();
    }

    class TeamStatsViewHolder extends RecyclerView.ViewHolder {
        TextView tvTeamName, tvMatchesPlayed, tvWins, tvDraws, tvLosses, tvGoalsScored, tvGoalsAgainst, tvPoints;

        public TeamStatsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTeamName = itemView.findViewById(R.id.tv_team_name);
            tvMatchesPlayed = itemView.findViewById(R.id.tv_matches_played);
            tvWins = itemView.findViewById(R.id.tv_wins);
            tvDraws = itemView.findViewById(R.id.tv_draws);
            tvLosses = itemView.findViewById(R.id.tv_losses);
            tvGoalsScored = itemView.findViewById(R.id.tv_goals_scored);
            tvGoalsAgainst = itemView.findViewById(R.id.tv_goals_against);
            tvPoints = itemView.findViewById(R.id.tv_points);
        }
    }
}