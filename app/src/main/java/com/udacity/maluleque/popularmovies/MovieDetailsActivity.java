package com.udacity.maluleque.popularmovies;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.udacity.maluleque.popularmovies.model.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImageView posterImageView = findViewById(R.id.posterImageView);
        TextView textViewDate = findViewById(R.id.textViewDate);
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewOverview = findViewById(R.id.textViewOverview);
        TextView textViewRating = findViewById(R.id.textViewRating);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Movie movie = bundle.getParcelable("selected-movie");

            Picasso.get()
                    .load(movie.getPosterPath())
                    .fit()
                    .into(posterImageView);


            textViewDate.setText(movie.getReleaseDate());
            textViewName.setText(movie.getOriginalTitle());
            textViewRating.setText(formatRating(movie.getRating()));
            textViewOverview.setText(movie.getSynopsis());
        }
    }


    private String formatRating(double rating) {
        return String.format("%.1f/%.1f", rating, 10.0);
    }

}
