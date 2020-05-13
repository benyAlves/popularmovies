package com.udacity.maluleque.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.udacity.maluleque.popularmovies.data.DataUtils;
import com.udacity.maluleque.popularmovies.data.NetworkUtils;
import com.udacity.maluleque.popularmovies.database.AppDatabase;
import com.udacity.maluleque.popularmovies.database.AppExecutors;
import com.udacity.maluleque.popularmovies.model.Movie;
import com.udacity.maluleque.popularmovies.model.Review;
import com.udacity.maluleque.popularmovies.model.Trailer;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerAdapter.TrailerItemClickListener {

    private static final String TRAILERS = "trailers";
    private static final String REVIEWS = "reviews";
    AppDatabase database;
    private Movie movie;
    private Button buttonAddAsFavorite;
    private boolean isFavorite;
    private RecyclerView recyclerViewTrailer;
    private ArrayList<Trailer> trailers;
    private ProgressBar progressBar;
    private TextView textViewErrorMessage;
    private TextView textViewErrorMessageReviews;
    private RecyclerView recyclerViewReviews;
    private ProgressBar progressBarReviews;
    private ArrayList<Review> reviews;

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


        progressBar = findViewById(R.id.progressBar);
        textViewErrorMessage = findViewById(R.id.textViewErrorMessage);

        progressBarReviews = findViewById(R.id.progressBarReviews);
        textViewErrorMessageReviews = findViewById(R.id.textViewErrorMessageReviews);

        recyclerViewTrailer = findViewById(R.id.recyclerViewTrailer);
        recyclerViewTrailer.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewTrailer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewTrailer.setHasFixedSize(true);


        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        recyclerViewReviews.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewReviews.setHasFixedSize(true);

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

            if (savedInstanceState == null || !savedInstanceState.containsKey(TRAILERS)) {
                getTrailers();
            } else {
                trailers = savedInstanceState.getParcelableArrayList(TRAILERS);
                populateTrailersList(trailers);
            }


            if (savedInstanceState == null || !savedInstanceState.containsKey(REVIEWS)) {
                getReviews();
            } else {
                reviews = savedInstanceState.getParcelableArrayList(REVIEWS);
                populateReviewsList(reviews);
            }


        }
    }

    private void getTrailers() {
        if (NetworkUtils.hasInternetConnection(this)) {

            URL url = NetworkUtils.buildUrl(String.format(Locale.getDefault(), NetworkUtils.TRAILERS_ENDPPOINT, movie.getId()), getApplicationContext());
            new TrailersBackgroundTask().execute(url);

        } else {
            showErrorMessage("Connect to the internet to watch trailers");
        }
    }


    private void getReviews() {
        if (NetworkUtils.hasInternetConnection(this)) {

            URL url = NetworkUtils.buildUrl(String.format(Locale.getDefault(), NetworkUtils.REVIEWS_ENDPPOINT, movie.getId()), getApplicationContext());
            new ReviewsBackgroundTask().execute(url);

        } else {
            showErrorMessageReview("Connect to the internet to watch reviews");
        }
    }

    @Override
    public void onTrailerItemClicked(int trailerIndex) {
        Trailer trailer = trailers.get(trailerIndex);
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private void populateTrailersList(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
        showMoviesList();
        TrailerAdapter movieAdapter = new TrailerAdapter(trailers, MovieDetailsActivity.this);
        recyclerViewTrailer.setAdapter(movieAdapter);
    }

    private void populateReviewsList(ArrayList<Review> reviews) {
        this.reviews = reviews;
        showReviewsList();
        ReviewAdapter reviewAdapter = new ReviewAdapter(reviews);
        recyclerViewReviews.setAdapter(reviewAdapter);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewTrailer.setVisibility(View.GONE);
        textViewErrorMessage.setVisibility(View.GONE);
    }

    private void showMoviesList() {
        progressBar.setVisibility(View.GONE);
        textViewErrorMessage.setVisibility(View.GONE);
        recyclerViewTrailer.setVisibility(View.VISIBLE);
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

        final MovieViewModelFactory factory = new MovieViewModelFactory(database, movie.getId());
        final MovieViewModel movieViewModel = new ViewModelProvider(this, factory).get(MovieViewModel.class);

        movieViewModel.getMovie().observe(MovieDetailsActivity.this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                movieViewModel.getMovie().removeObserver(this);
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

    private void showErrorMessage(String message) {
        progressBarReviews.setVisibility(View.GONE);
        recyclerViewReviews.setVisibility(View.GONE);
        textViewErrorMessageReviews.setVisibility(View.VISIBLE);
        textViewErrorMessageReviews.setText(message);
    }

    private void showProgressBarReview() {
        progressBarReviews.setVisibility(View.VISIBLE);
        recyclerViewReviews.setVisibility(View.GONE);
        textViewErrorMessageReviews.setVisibility(View.GONE);
    }

    private void showReviewsList() {
        progressBarReviews.setVisibility(View.GONE);
        textViewErrorMessageReviews.setVisibility(View.GONE);
        recyclerViewReviews.setVisibility(View.VISIBLE);
    }

    private void showErrorMessageReview(String message) {
        progressBarReviews.setVisibility(View.GONE);
        recyclerViewReviews.setVisibility(View.GONE);
        textViewErrorMessageReviews.setVisibility(View.VISIBLE);
        textViewErrorMessageReviews.setText(message);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TRAILERS, trailers);
        outState.putParcelableArrayList(REVIEWS, reviews);
    }

    private String formatRating(double rating) {
        return String.format(Locale.getDefault(), "%.1f/%.1f", rating, 10.0);
    }

    private class TrailersBackgroundTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            return NetworkUtils.fecthData(url);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && !result.isEmpty()) {

                /*Passing json result to be parsed and create a list of trailers*/
                ArrayList<Trailer> trailers = DataUtils.parseTrailerJson(result);
                if (trailers != null) {
                    populateTrailersList(trailers);
                    if (trailers.isEmpty()) {
                        showErrorMessage("No trailers");
                    }
                } else {
                    showErrorMessage("No trailers");
                }

            } else {
                showErrorMessage("No trailers. Try again later!");
            }
        }
    }

    private class ReviewsBackgroundTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBarReview();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            return NetworkUtils.fecthData(url);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && !result.isEmpty()) {

                /*Passing json result to be parsed and create a list of trailers*/
                ArrayList<Review> reviews = DataUtils.parseReviewsJson(result);

                if (reviews != null) {
                    populateReviewsList(reviews);
                    if (reviews.isEmpty()) {
                        showErrorMessageReview("No reviews");
                    }
                } else {
                    showErrorMessageReview("No reviews");
                }

            } else {
                showErrorMessageReview("Try again later!");
            }
        }
    }

}
