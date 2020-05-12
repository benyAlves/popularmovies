package com.udacity.maluleque.popularmovies;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.squareup.picasso.Picasso;
import com.udacity.maluleque.popularmovies.database.AppDatabase;
import com.udacity.maluleque.popularmovies.database.AppExecutors;
import com.udacity.maluleque.popularmovies.model.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    AppDatabase database;
    private Movie movie;
    private Button buttonAddAsFavorite;
    private boolean isFavorite;

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
        buttonAddAsFavorite = findViewById(R.id.buttonAddAsFavorite);
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

            checkIfIsFavorite();
        }
    }

    private void addAsFavorite() {
        if (isFavorite) {
            removeMovie(movie);
            Toast.makeText(MovieDetailsActivity.this, R.string.remove_movie_message, Toast.LENGTH_SHORT).show();
            Drawable drawable = ContextCompat.getDrawable(MovieDetailsActivity.this, R.drawable.ic_favorite_border_black_24dp);
            updateFavoriteButtonDrawable(drawable);
        } else {
            addMovie();
            Toast.makeText(MovieDetailsActivity.this, R.string.favorite_movie_message, Toast.LENGTH_SHORT).show();
            Drawable drawable = ContextCompat.getDrawable(MovieDetailsActivity.this, R.drawable.ic_favorite_black_24dp);
            updateFavoriteButtonDrawable(drawable);
        }
    }


    private void checkIfIsFavorite() {
        final LiveData<Movie> movieLiveData = database.MovieDao().findById(movie.getId());
        movieLiveData.observe(MovieDetailsActivity.this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                movieLiveData.removeObserver(this);
                if (movie != null) {
                    Drawable drawable = ContextCompat.getDrawable(MovieDetailsActivity.this, R.drawable.ic_favorite_black_24dp);
                    updateFavoriteButtonDrawable(drawable);
                    isFavorite = true;
                } else {
                    Drawable drawable = ContextCompat.getDrawable(MovieDetailsActivity.this, R.drawable.ic_favorite_border_black_24dp);
                    updateFavoriteButtonDrawable(drawable);
                    isFavorite = false;
                }
            }
        });
    }


    private void removeMovie(final Movie movie) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.MovieDao().delete(movie);
                isFavorite = false;
            }
        });
    }

    private void updateFavoriteButtonDrawable(Drawable drawable) {
        drawable.setBounds(0, 0, 60, 60);
        buttonAddAsFavorite.setCompoundDrawables(drawable, null, null, null);
    }

    private void addMovie() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.MovieDao().insert(movie);
                isFavorite = true;
            }
        });
    }


    private String formatRating(double rating) {
        return String.format("%.1f/%.1f", rating, 10.0);
    }

}
