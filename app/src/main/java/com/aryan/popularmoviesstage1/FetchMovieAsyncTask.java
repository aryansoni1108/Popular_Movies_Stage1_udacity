package com.aryan.popularmoviesstage1;

import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;




public class FetchMovieAsyncTask extends AsyncTask<String, Void, ArrayList<Movies>> {

    private final String LOG_TAG = FetchMovieAsyncTask.class.getSimpleName();
    private final String mApiKey;
    private final OnTaskCompleted mListener;
    public FetchMovieAsyncTask(OnTaskCompleted listener, String apiKey) {
        super();

        mListener = listener;
        mApiKey = apiKey;
    }


    @Override
    protected ArrayList<Movies> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Holds data returned from the API
        String moviesJsonStr = null;

        try {
            URL url = getApiUrl(params);

            // Start connecting to get JSON
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();

            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Adds '\n' at last line if not already there.
                // This supposedly makes it easier to debug.
                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {
                // No data found. Nothing more to do here.
                return null;
            }

            moviesJsonStr = builder.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            // Tidy up: release url connection and buffered reader
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            // Make sense of the JSON data
            return getMoviesDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList getMoviesDataFromJson(String moviesJsonStr) throws JSONException {
        // JSON tags
        final String TAG_RESULTS = "results";
        final String TAG_ORIGINAL_TITLE = "original_title";
        final String TAG_POSTER_PATH = "poster_path";
        final String TAG_OVERVIEW = "overview";
        final String TAG_VOTE_AVERAGE = "vote_average";
        final String TAG_RELEASE_DATE = "release_date";


        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = moviesJson.getJSONArray(TAG_RESULTS);


        ArrayList<Movies> movieslist = new ArrayList<Movies>();

        // Traverse through movies one by one and get data
        for (int i = 0; i < resultsArray.length(); i++) {
            // Initialize each object before it can be used


            // Object contains all tags we're looking for
            JSONObject movieObject = resultsArray.getJSONObject(i);

            // Store data in movie object
            String title = movieObject.getString(TAG_ORIGINAL_TITLE);
            String poster_path = movieObject.getString(TAG_POSTER_PATH);
            String overview = movieObject.getString(TAG_OVERVIEW);
            Double voteAverage = movieObject.getDouble(TAG_VOTE_AVERAGE);
            String releasedate = movieObject.getString(TAG_RELEASE_DATE);
            Movies movies = new Movies(title,poster_path,overview,voteAverage,releasedate);
            movieslist.add(movies);
            Log.e("dkdkd",movieslist.get(i).getmPosterPath());
        }

        return movieslist;
    }


    private URL getApiUrl(String[] parameters) throws MalformedURLException {
        final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/"+parameters[0];

        final String API_KEY_PARAM = "api_key";


        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()

                .appendQueryParameter(API_KEY_PARAM, mApiKey)
                .build();
        Log.e("uri:",String.valueOf(builtUri));

        return new URL(builtUri.toString());
    }

    @Override
    protected void onPostExecute(ArrayList<Movies> movies) {
        super.onPostExecute(movies);

        // Notify UI
        mListener.onFetchMoviesTaskCompleted(movies);
    }
}