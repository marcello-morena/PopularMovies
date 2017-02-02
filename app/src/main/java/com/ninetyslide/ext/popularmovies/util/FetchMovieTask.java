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

import android.os.AsyncTask;

import com.ninetyslide.ext.popularmovies.MovieListActivity;
import com.ninetyslide.ext.popularmovies.bean.Movie;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * AsyncTask that handles all the retrieval of movie data.
 *
 * @author Marcello Morena
 */
public class FetchMovieTask extends AsyncTask<Void, Void, List<Movie>> {

    private MovieListActivity.MovieSort sortOrder;
    private MovieListActivity.OnFetchResultHandler fetchResultHandler;
    private String errorMessage;

    public FetchMovieTask(MovieListActivity.OnFetchResultHandler fetchResultHandler, MovieListActivity.MovieSort sortOrder) {
        this.sortOrder = sortOrder;
        this.fetchResultHandler = fetchResultHandler;
    }

    @Override
    protected List<Movie> doInBackground(Void... voids) {
        URL movieUrl = NetworkUtils.buildUrl(sortOrder);

        try {
            return NetworkUtils.fetchMovieData(movieUrl);
        } catch (IOException e){
            errorMessage = e.getMessage();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (movies == null) {
            fetchResultHandler.onFetchError(errorMessage);
        } else {
            fetchResultHandler.onFetchSuccess(movies);
        }
    }
}
