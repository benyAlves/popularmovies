package com.udacity.maluleque.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.maluleque.popularmovies.data.DataUtils;
import com.udacity.maluleque.popularmovies.data.NetworkUtils;
import com.udacity.maluleque.popularmovies.model.Movie;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        URL url = NetworkUtils.buildUrl();
        new MoviesBackgoundTask().execute(url);


    }

    @Override
    public void onMovieItemClicked(int movieIndex) {

    }

    private class MoviesBackgoundTask extends AsyncTask<URL, Void, String> {

        private static final String TAG = "MoviesBackgoundTask";

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            return NetworkUtils.fecthData(url);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && !result.isEmpty()) {
                List<Movie> movieList = DataUtils.parseJson(result);
                Log.i(TAG, "onPostExecute: " + movieList);
                RecyclerView moviesRecyclerView = findViewById(R.id.movies_recycler_view);
                moviesRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false));
                moviesRecyclerView.setHasFixedSize(true);
                MovieAdapter movieAdapter = new MovieAdapter(movieList, MainActivity.this);
                moviesRecyclerView.setAdapter(movieAdapter);

            } else {
                Log.i(TAG, "onPostExecute: error");
            }
            //TODO pass to list and recyclerView
        }
    }

}
