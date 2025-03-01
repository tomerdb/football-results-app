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

/**
 * RecyclerView adapter for displaying football match items.
 * This adapter handles the presentation of match data including date, location,
 * team names, and scores. It also provides visual feedback through color-coding
 * of match results (win/loss/draw).
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {
    /** List of matches to display */
    private final List<Match> matchList;
    
    /** Context for resource access */
    private final Context context;
    
    /** Listener for handling item click events */
    private OnItemClickListener listener;

    /**
     * Interface for handling click events on match items.
     */
    public interface OnItemClickListener {
        /**
         * Called when a match item is clicked.
         * @param match The Match object that was clicked
         */
        void onItemClick(Match match);
    }

    /**
     * Constructs a new MatchAdapter.
     * @param context The context for inflating layouts
     * @param matchList The list of matches to display
     */
    public MatchAdapter(Context context, List<Match> matchList) {
        this.context = context;
        this.matchList = matchList;
    }

    /**
     * Sets the click listener for match items.
     * @param listener The OnItemClickListener to handle click events
     */
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
        
        // Set match details
        holder.tvDate.setText(match.getDate());
        holder.tvCity.setText(match.getCity());
        holder.tvTeamA.setText(match.getTeamA());
        holder.tvTeamB.setText(match.getTeamB());

        // Set actual goals
        holder.tvTeamAGoals.setText(String.valueOf(match.getTeamAGoals()));
        holder.tvTeamBGoals.setText(String.valueOf(match.getTeamBGoals()));

        // Color-code results based on win/loss/draw
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

    /**
     * ViewHolder class for match items.
     * Holds references to all views within a match item layout.
     */
    class MatchViewHolder extends RecyclerView.ViewHolder {
        /** TextViews for displaying match information */
        TextView tvDate, tvCity, tvTeamA, tvTeamB, tvTeamAGoals, tvTeamBGoals;

        /**
         * Constructs a new MatchViewHolder.
         * Initializes all view references and sets up click listener.
         * @param itemView The View object representing a match item
         */
        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvCity = itemView.findViewById(R.id.tv_city);
            tvTeamA = itemView.findViewById(R.id.tv_team_a);
            tvTeamB = itemView.findViewById(R.id.tv_team_b);
            tvTeamAGoals = itemView.findViewById(R.id.tv_team_a_goals);
            tvTeamBGoals = itemView.findViewById(R.id.tv_team_b_goals);

            // Set up click listener for the entire item
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(matchList.get(position));
                }
            });
        }
    }
}