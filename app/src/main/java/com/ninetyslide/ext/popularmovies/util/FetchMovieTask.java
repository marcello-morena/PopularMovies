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
import android.os.AsyncTask;

import com.ninetyslide.ext.popularmovies.MovieListActivity;
import com.ninetyslide.ext.popularmovies.R;
import com.ninetyslide.ext.popularmovies.bean.Movie;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.ninetyslide.ext.popularmovies.MovieListActivity.MovieSort.FAVORITES;

/**
 * AsyncTask that handles all the retrieval of movie data.
 *
 * @author Marcello Morena
 */
public class FetchMovieTask extends AsyncTask<Void, Void, List<Movie>> {

    private Context context;
    private MovieListActivity.MovieSort sortOrder;
    private OnFetchMovieResultHandler fetchMovieResultHandler;
    private String errorMessage;

    public FetchMovieTask(Context context, OnFetchMovieResultHandler fetchMovieResultHandler, MovieListActivity.MovieSort sortOrder) {
        this.context = context;
        this.sortOrder = sortOrder;
        this.fetchMovieResultHandler = fetchMovieResultHandler;
    }

    @Override
    protected List<Movie> doInBackground(Void... voids) {

        if (sortOrder == FAVORITES) {
            // Fetch movies from content provider
            return FavoriteMovieUtils.fetchFavoriteMovies(context);
        } else {
            URL movieUrl = NetworkUtils.buildMoviesUrl(sortOrder);

            try {
                return NetworkUtils.fetchMovieData(movieUrl);
            } catch (IOException e) {
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    errorMessage = context.getString(R.string.no_network_error);
                } else {
                    errorMessage = e.getMessage();
                }
                return null;
            }
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (movies == null) {
            fetchMovieResultHandler.onFetchError(errorMessage);
        } else {
            fetchMovieResultHandler.onFetchSuccess(movies);
        }
    }

    public interface OnFetchMovieResultHandler {
        void onFetchSuccess(List<Movie> movies);
        void onFetchError(String errorMessage);
    }

}
