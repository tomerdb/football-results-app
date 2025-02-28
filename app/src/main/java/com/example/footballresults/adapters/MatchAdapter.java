package com.example.footballresults.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballresults.R;
import com.example.footballresults.models.Match;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {
    private final List<Match> matchList;
    private final Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Match match);
    }

    public MatchAdapter(Context context, List<Match> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = matchList.get(position);
        holder.tvDate.setText(match.getDate());
        holder.tvCity.setText(match.getCity());
        holder.tvTeamA.setText(match.getTeamA());
        holder.tvTeamB.setText(match.getTeamB());

        // Set actual goals
        holder.tvTeamAGoals.setText(String.valueOf(match.getTeamAGoals()));
        holder.tvTeamBGoals.setText(String.valueOf(match.getTeamBGoals()));

        // Color-code results
        int teamAGoals = match.getTeamAGoals();
        int teamBGoals = match.getTeamBGoals();

        // Team A result coloring
        if (teamAGoals > teamBGoals) {
            // Team A won
            holder.tvTeamAGoals.setBackgroundResource(R.color.win_color);
        } else if (teamAGoals < teamBGoals) {
            // Team A lost
            holder.tvTeamAGoals.setBackgroundResource(R.color.loss_color);
        } else {
            // Draw
            holder.tvTeamAGoals.setBackgroundResource(R.color.draw_color);
        }

        // Team B result coloring
        if (teamBGoals > teamAGoals) {
            // Team B won
            holder.tvTeamBGoals.setBackgroundResource(R.color.win_color);
        } else if (teamBGoals < teamAGoals) {
            // Team B lost
            holder.tvTeamBGoals.setBackgroundResource(R.color.loss_color);
        } else {
            // Draw
            holder.tvTeamBGoals.setBackgroundResource(R.color.draw_color);
        }
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvCity, tvTeamA, tvTeamB, tvTeamAGoals, tvTeamBGoals;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvCity = itemView.findViewById(R.id.tv_city);
            tvTeamA = itemView.findViewById(R.id.tv_team_a);
            tvTeamB = itemView.findViewById(R.id.tv_team_b);
            tvTeamAGoals = itemView.findViewById(R.id.tv_team_a_goals);
            tvTeamBGoals = itemView.findViewById(R.id.tv_team_b_goals);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(matchList.get(position));
                }
            });
        }
    }
}