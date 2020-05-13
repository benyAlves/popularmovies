package com.udacity.maluleque.popularmovies.data;

import com.udacity.maluleque.popularmovies.model.Movie;
import com.udacity.maluleque.popularmovies.model.Review;
import com.udacity.maluleque.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataUtils {


    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String SIZE = "w185";

    /*
    * @param json Response from network request
    *
    * @return Java List of Movie objects
    * */
    public static ArrayList<Movie> parseJson(String json) {
        ArrayList<Movie> movies = new ArrayList<>();
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
        return new ArrayList<>();
    }


    /*
     * @param json Response from network request
     *
     * @return Java List of Trailers objects
     * */
    public static ArrayList<Trailer> parseTrailerJson(String json) {
        ArrayList<Trailer> trailers = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject trailerObject = results.getJSONObject(i);
                String id = trailerObject.getString("id");
                String key = trailerObject.getString("key");
                String name = trailerObject.getString("name");
                String site = trailerObject.getString("site");
                int size = trailerObject.getInt("size");
                Trailer trailer = new Trailer(id, key, size, name, site);
                trailers.add(trailer);
            }
            return trailers;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    /*
     * @param json Response from network request
     *
     * @return Java List of Trailers objects
     * */
    public static ArrayList<Review> parseReviewsJson(String json) {
        ArrayList<Review> reviews = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject reviewObject = results.getJSONObject(i);
                String id = reviewObject.getString("id");
                String author = reviewObject.getString("author");
                String content = reviewObject.getString("content");
                String url = reviewObject.getString("url");

                Review review = new Review(id, author, content, url);

                reviews.add(review);
            }
            return reviews;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static String buildPosterUrl(String posterPath) {
        return IMAGE_BASE_URL.concat(SIZE).concat(posterPath);
    }
}
