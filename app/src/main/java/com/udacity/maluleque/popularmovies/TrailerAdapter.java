package com.udacity.maluleque.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.maluleque.popularmovies.model.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {


    private final List<Trailer> trailers;
    private final TrailerItemClickListener trailerListListener;


    public TrailerAdapter(List<Trailer> trailers, TrailerItemClickListener trailerListListener) {
        this.trailers = trailers;
        this.trailerListListener = trailerListListener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        holder.trailerTextView.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public interface TrailerItemClickListener {
        void onTrailerItemClicked(int trailerIndex);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView trailerTextView;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            trailerTextView = itemView.findViewById(R.id.textViewTrailerName);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int trailerIndex = getAdapterPosition();
            trailerListListener.onTrailerItemClicked(trailerIndex);
        }
    }
}
