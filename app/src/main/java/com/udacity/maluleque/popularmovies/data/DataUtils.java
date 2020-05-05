package com.udacity.maluleque.popularmovies.data;

import com.udacity.maluleque.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataUtils {


    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String SIZE = "w185";

    public static List<Movie> parseJson(String json) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieObject = results.getJSONObject(i);
                long id = movieObject.getLong("id");
                double voteAverage = movieObject.getDouble("vote_average");
                String originalTitle = movieObject.getString("original_title");
                String overview = movieObject.getString("overview");
                String posterUrl = buildPosterUrl(movieObject.getString("poster_path"));
                String releaseDate = movieObject.getString("release_date");
                Movie movie = new Movie(id, originalTitle, posterUrl, overview, voteAverage, releaseDate);
                movies.add(movie);
            }
            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String buildPosterUrl(String posterPath) {
        return BASE_URL.concat(SIZE).concat(posterPath);
    }
}
