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

import android.net.Uri;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ninetyslide.ext.popularmovies.MovieListActivity;
import com.ninetyslide.ext.popularmovies.bean.Movie;
import com.ninetyslide.ext.popularmovies.common.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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
     * Build a URL for theMovieDB API request, based on the sort order passed as an argument.
     *
     * @param sortOrder The sort order of the movies.
     * @return A URL appropriate for the sort order passed as an argument.
     */
    public static URL buildUrl(MovieListActivity.MovieSort sortOrder) {
        String uriPath = "";

        switch (sortOrder) {
            case MOST_POPULAR:
                uriPath = Constants.MOVIEDB_API_BASE_URL + Constants.MOVIEDB_API_POPULAR_URL;
                break;
            case TOP_RATED:
                uriPath = Constants.MOVIEDB_API_BASE_URL + Constants.MOVIEDB_API_TOPRATED_URL;
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
     */
    public static List<Movie> fetchMovieData(URL url) throws IOException {
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            // Get the whole JSON response string
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();

            // Parse the JSON string and return a List of Movie
            JsonObject jsonRoot = GsonManager.getJsonParserInstance().parse(response.toString()).getAsJsonObject();
            JsonElement resultMovies = jsonRoot.get(Constants.MOVIEDB_API_JSON_NAME_RESULTS);
            Type listMovie = new TypeToken<List<Movie>>() {}.getType();

            return GsonManager.getGsonInstance().fromJson(resultMovies, listMovie);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

    }
}
