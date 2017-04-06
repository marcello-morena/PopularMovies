/*
 * Copyright 2017 NinetySlide
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ninetyslide.ext.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ninetyslide.ext.popularmovies.MovieListActivity;
import com.ninetyslide.ext.popularmovies.bean.Movie;
import com.ninetyslide.ext.popularmovies.bean.Review;
import com.ninetyslide.ext.popularmovies.bean.Reviews;
import com.ninetyslide.ext.popularmovies.bean.Video;
import com.ninetyslide.ext.popularmovies.bean.Videos;
import com.ninetyslide.ext.popularmovies.common.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.ninetyslide.ext.popularmovies.common.Constants.MOVIEDB_API_BASE_URL;

/**
 * Class containing the utilities to perform movie data fetch from the network.
 *
 * @author Marcello Morena
 */
public final class NetworkUtils {

    private NetworkUtils() {
    }

    /**
     * Generate the URL of the poster to retrieve using the relative path and the width passed as
     * parameters.
     *
     * @param posterPath The relative path of the poster to retrieve
     * @param width The width of the poster to retrieve
     * @return The full URL of the poster to retrieve
     */
    public static String generatePosterUrl(String posterPath, String width) {
        return Constants.MOVIEDB_POSTER_BASE_URL +
                width +
                posterPath;
    }

    /**
     * Generate the URL to retrieve the reviews for a specific movie.
     *
     * @param movieId The ID of the movie.
     * @return The URL to retrieve the reviews of the movie.
     */
    public static URL buildReviewUrl(String movieId) {
        String uriPath = Constants.MOVIEDB_API_BASE_URL +
                String.format(Constants.MOVIEDB_API_REVIEWS_URL_PATTERN, movieId);

        Uri uri = Uri.parse(uriPath).buildUpon()
                .appendQueryParameter(Constants.MOVIEDB_API_PARAMETER_APIKEY, Constants.MOVIEDB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            return null;
        }

        return url;
    }

    /**
     * Generate the URL to retrieve the videos for a specific movie.
     *
     * @param movieId The ID of the movie.
     * @return The URL to retrieve the videos of the movie.
     */
    public static URL buildVideosUrl(String movieId) {
        String uriPath = Constants.MOVIEDB_API_BASE_URL +
                String.format(Constants.MOVIEDB_API_VIDEOS_URL_PATTERN, movieId);

        Uri uri = Uri.parse(uriPath).buildUpon()
                .appendQueryParameter(Constants.MOVIEDB_API_PARAMETER_APIKEY, Constants.MOVIEDB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            return null;
        }

        return url;
    }

    /**
     * Build a URL for theMovieDB API request, based on the sort order passed as an argument.
     *
     * @param sortOrder The sort order of the movies.
     * @return A URL appropriate for the sort order passed as an argument.
     */
    public static URL buildMoviesUrl(MovieListActivity.MovieSort sortOrder) {
        String uriPath = "";

        switch (sortOrder) {
            case MOST_POPULAR:
                uriPath = MOVIEDB_API_BASE_URL + Constants.MOVIEDB_API_POPULAR_URL;
                break;
            case TOP_RATED:
                uriPath = MOVIEDB_API_BASE_URL + Constants.MOVIEDB_API_TOPRATED_URL;
                break;
        }

        Uri uri = Uri.parse(uriPath).buildUpon()
                .appendQueryParameter(Constants.MOVIEDB_API_PARAMETER_APIKEY, Constants.MOVIEDB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            return null;
        }

        return url;
    }

    /**
     * Retrieve a list of movies from theMovieDB using the URL passed as a parameter.
     *
     * @param url The URL to use for the connection
     * @return A list of movies from theMovieDB
     * @throws IOException In case of network problems
     */
    public static List<Movie> fetchMovieData(URL url) throws IOException {
        String rawMovie = fetchData(url);

        // Parse the JSON string and return a List of Movie
        JsonObject jsonRoot = GsonManager.getJsonParserInstance().parse(rawMovie).getAsJsonObject();
        JsonElement resultMovies = jsonRoot.get(Constants.MOVIEDB_API_JSON_NAME_RESULTS);
        Type listMovie = new TypeToken<List<Movie>>() {}.getType();

        return GsonManager.getGsonInstance().fromJson(resultMovies, listMovie);
    }

    /**
     * Retrieve a list of reviews from theMovieDB for a specific movie using the URL passed as a
     * parameter.
     *
     * @param url The URL to use for the connection
     * @return A list of reviews for a specific movie
     * @throws IOException In case of network problems
     */
    public static List<Review> fetchReviewData(URL url) throws IOException {
        String rawReview = fetchData(url);

        Reviews reviewResult = GsonManager.getGsonInstance().fromJson(rawReview, Reviews.class);

        if (reviewResult != null && reviewResult.getResults() != null) {
            return reviewResult.getResults();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieve a list of videos from theMovieDB for a specific movie using the URL passed as a
     * parameter.
     *
     * @param url The URL to use for the connection
     * @return A list of videos for a specific movie
     * @throws IOException In case of network problems
     */
    public static List<Video> fetchVideoData(URL url) throws IOException {
        String rawVideo = fetchData(url);

        Videos videoResult = GsonManager.getGsonInstance().fromJson(rawVideo, Videos.class);

        if (videoResult != null && videoResult.getResults() != null) {
            return videoResult.getResults();
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Check whether the network is accessible.
     *
     * @param context The context to use to check for the network availability
     * @return True is network is accessible, false otherwise
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Retrieve raw data from a URL.
     *
     * @param url The URL to use for the connection
     * @return A String with raw data
     * @throws IOException In case of network problems
     */
    private static String fetchData(URL url) throws IOException {
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            //conn.setDoOutput(true);

            // Get the whole JSON response string
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();

            return response.toString();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

}
