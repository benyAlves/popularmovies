package com.udacity.maluleque.popularmovies.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.udacity.maluleque.popularmovies.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    public static final String POPULAR_MOVIES_ENDPPOINT = "movie/popular";
    public static final String TOP_RATED_MOVIES_ENDPPOINT = "movie/top_rated";
    private static final String PARAM_API_KEY = "api_key";
    private static String API_KEY;
    private static String BASE_URL = "http://api.themoviedb.org/3";

    private static final String TAG = "NetworkUtils";

    public static URL buildUrl(String endPoint, Context context) {
        API_KEY = context.getString(R.string.themoviedb_api_key);

        Uri buildUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendEncodedPath(endPoint)
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

    /*
     *
     * This method checks if mobile has internet connection
     *
     * @param context   Android Context to access preferences and resources
     * */
    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network netInfo = cm.getActiveNetwork();

        if (netInfo == null) {
            return false;
        }

        NetworkCapabilities networkCapabilities =
                cm.getNetworkCapabilities(netInfo);

        return networkCapabilities != null
                && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }
}
