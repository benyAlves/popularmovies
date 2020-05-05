package com.udacity.maluleque.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.udacity.maluleque.popularmovies.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {


    private final List<Movie> movies;
    private final MovieItemClickListener movieListListener;


    public MovieAdapter(List<Movie> movies, MovieItemClickListener movieListListener){
        this.movies = movies;
        this.movieListListener = movieListListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView moviePosterImageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePosterImageView = (ImageView) itemView.findViewById(R.id.movie_poster);
        }


        @Override
        public void onClick(View view) {
            int movieItemIndex = getAdapterPosition();
            movieListListener.onMovieItemClicked(movieItemIndex);
        }
    }


    public interface MovieItemClickListener{
        void onMovieItemClicked(int movieIndex);
    }
}
