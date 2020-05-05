package com.udacity.maluleque.popularmovies.data;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String PARAM_QUERY = "movie/popular";
    private static final String PARAM_SORT = "";
    private static final String TAG = "NetworkUtils";
    private static final String PARAM_API_KEY = "api_key";
    private static final String API_KEY = "2071442523ce286868d10f51dfb50a9d";
    private static String BASE_URL = "http://api.themoviedb.org/3";

    public static URL buildUrl() {
        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(PARAM_QUERY)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "buildUrl: ", e);
        }

        return url;
    }

    public static String fecthData(URL url) {
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return readStream(in);
        } catch (IOException e) {
            Log.e(TAG, "fecthData: ", e);
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }

    private static String readStream(InputStream in) {
        Scanner s = new Scanner(in).useDelimiter("\\A");
        return s.hasNext() ? s.next() : null;
    }
}
