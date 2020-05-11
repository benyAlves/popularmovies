package com.udacity.maluleque.popularmovies;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.udacity.maluleque.popularmovies.database.AppDatabase;
import com.udacity.maluleque.popularmovies.database.AppExecutors;
import com.udacity.maluleque.popularmovies.model.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    AppDatabase database;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        database = AppDatabase.getInstance(getApplicationContext());

        ImageView posterImageView = findViewById(R.id.posterImageView);
        TextView textViewDate = findViewById(R.id.textViewDate);
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewOverview = findViewById(R.id.textViewOverview);
        TextView textViewRating = findViewById(R.id.textViewRating);
        Button buttonAddAsFavorite = findViewById(R.id.buttonAddAsFavorite);
        buttonAddAsFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAsFavorite();
            }
        });


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            movie = bundle.getParcelable("selected-movie");

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

    private void addAsFavorite() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.MovieDao().insert(movie);
            }
        });
    }


    private String formatRating(double rating) {
        return String.format("%.1f/%.1f", rating, 10.0);
    }

}
