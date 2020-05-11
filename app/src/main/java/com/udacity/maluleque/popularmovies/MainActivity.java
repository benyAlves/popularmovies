package com.udacity.maluleque.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.maluleque.popularmovies.data.DataUtils;
import com.udacity.maluleque.popularmovies.data.NetworkUtils;
import com.udacity.maluleque.popularmovies.database.AppDatabase;
import com.udacity.maluleque.popularmovies.model.Movie;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener {

    public static final String MOVIES = "movies";
    private static final String TAG = "MainActivity";
    private ArrayList<Movie> movieList;
    private ProgressBar progressBar;
    private RecyclerView moviesRecyclerView;
    private TextView textViewErrorMessage;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRecyclerView = findViewById(R.id.movies_recycler_view);
        database = AppDatabase.getInstance(getApplicationContext());

        /*Get activity current orientation*/
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            /*Show 2 columns if its on portrait orientation*/
            moviesRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false));
        }else{
            /*Show 4 columns if its on landscape orientation*/
            moviesRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 4, GridLayoutManager.VERTICAL, false));
        }

        moviesRecyclerView.setHasFixedSize(true);
        progressBar = findViewById(R.id.progressBar);
        textViewErrorMessage = findViewById(R.id.textViewErrorMessage);


        if(savedInstanceState == null || savedInstanceState.containsKey(MOVIES)) {
            initHttpRequest(NetworkUtils.TOP_RATED_MOVIES_ENDPPOINT);
        }else{
            movieList = savedInstanceState.getParcelableArrayList(MOVIES);
            populateMoviesList(movieList);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.most_popular:
                initHttpRequest(NetworkUtils.POPULAR_MOVIES_ENDPPOINT);
                return true;

            case R.id.top_rated:
                initHttpRequest(NetworkUtils.TOP_RATED_MOVIES_ENDPPOINT);
                return true;

            case R.id.favorite:
                retriveFavoriteMovies();
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }


    }

    private void initHttpRequest(String endpoint){
        if(NetworkUtils.hasInternetConnection(this)){

            URL url = NetworkUtils.buildUrl(endpoint, getApplicationContext());
            new MoviesBackgroundTask().execute(url);

        }else{
            showErrorMessage("Check your internet connection and try again");
        }
    }

    private void retriveFavoriteMovies() {
        showProgressBar();
        LiveData<List<Movie>> allFavoriteMovies = database.MovieDao().getAllFavoriteMovies();
        allFavoriteMovies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                populateMoviesList(movies);
            }
        });
    }



    @Override
    public void onMovieItemClicked(int movieIndex) {
        Movie movie = movieList.get(movieIndex);
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("selected-movie", movie);
        startActivity(intent);
    }

    private void populateMoviesList(List<Movie> moviesList) {
        showMoviesList();
        MovieAdapter movieAdapter = new MovieAdapter(moviesList, MainActivity.this);
        moviesRecyclerView.setAdapter(movieAdapter);
    }


    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        moviesRecyclerView.setVisibility(View.GONE);
        textViewErrorMessage.setVisibility(View.GONE);
    }

    private void showMoviesList(){
        progressBar.setVisibility(View.GONE);
        textViewErrorMessage.setVisibility(View.GONE);
        moviesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String message){
        progressBar.setVisibility(View.GONE);
        moviesRecyclerView.setVisibility(View.GONE);
        textViewErrorMessage.setVisibility(View.VISIBLE);
        textViewErrorMessage.setText(message);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(MOVIES, movieList);
        super.onSaveInstanceState(outState);
    }

    private class MoviesBackgroundTask extends AsyncTask<URL, Void, String> {

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

                /*Passing json result to be parsed and create a list of movies*/
                movieList = DataUtils.parseJson(result);
                populateMoviesList(movieList);

            } else {
                showErrorMessage("An error occurred. Try again later!");
            }
        }
    }
}
