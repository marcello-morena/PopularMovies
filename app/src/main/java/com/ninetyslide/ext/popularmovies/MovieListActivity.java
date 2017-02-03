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

package com.ninetyslide.ext.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ninetyslide.ext.popularmovies.adapter.MovieAdapter;
import com.ninetyslide.ext.popularmovies.bean.Movie;
import com.ninetyslide.ext.popularmovies.common.Constants;
import com.ninetyslide.ext.popularmovies.util.FetchMovieTask;

import java.util.List;

/**
 * Main Activity of the app. Shows a list of movies.
 *
 * @author Marcello Morena
 */
public class MovieListActivity extends AppCompatActivity {

    private RecyclerView movieList;
    private TextView errorDisplay;
    private ProgressBar progressBar;

    private MovieAdapter movieAdapter;

    private MovieAdapter.OnMovieClickHandler clickHandler = new MovieAdapter.OnMovieClickHandler() {
        @Override
        public void onClick(Movie movie, View iv) {
            // Launch the Detail activity with Movie info
            Intent intent = new Intent(MovieListActivity.this, MovieDetailActivity.class);
            intent.putExtra(Constants.INTENT_MOVIE_EXTRA, movie);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(MovieListActivity.this, iv, getString(R.string.poster_detail_transition));
            startActivity(intent, options.toBundle());
        }
    };

    private OnFetchResultHandler fetchActions = new OnFetchResultHandler() {
        @Override
        public void onFetchSuccess(List<Movie> movies) {
            changeUi(UiState.SHOW_MOVIE, null);
            movieAdapter.setMovieData(movies);
        }

        @Override
        public void onFetchError(String errorMessage) {
            changeUi(UiState.SHOW_ERROR, errorMessage);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        // Retrieve views
        errorDisplay = (TextView) findViewById(R.id.tv_list_error);
        progressBar = (ProgressBar) findViewById(R.id.pb_list_load);

        // Retrieve and setup the RecyclerView
        movieList = (RecyclerView) findViewById(R.id.rv_movie_list);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, calculateNoOfColumns(this), LinearLayoutManager.VERTICAL, false);
        movieList.setLayoutManager(layoutManager);

        movieList.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(clickHandler);
        movieList.setAdapter(movieAdapter);

        // Load Movie data using default sort order
        loadMovieData(Constants.MOVIE_DEFAULT_SORT_ORDER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_popular:
                loadMovieData(MovieSort.MOST_POPULAR);
                break;
            case R.id.action_toprated:
                loadMovieData(MovieSort.TOP_RATED);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Change the views visible in the UI base on the UI State passed as an argument.
     *
     * @param state The state of the UI
     * @param errorMessage The error message. This parameter must be provided only in case of error.
     */
    private void changeUi(UiState state, String errorMessage) {
        switch (state) {
            case LOADING:
                progressBar.setVisibility(View.VISIBLE);
                errorDisplay.setVisibility(View.INVISIBLE);
                movieList.setVisibility(View.INVISIBLE);
                break;
            case SHOW_ERROR:
                progressBar.setVisibility(View.INVISIBLE);
                errorDisplay.setVisibility(View.VISIBLE);
                errorDisplay.setText(errorMessage);
                movieList.setVisibility(View.INVISIBLE);
                break;
            case SHOW_MOVIE:
                progressBar.setVisibility(View.INVISIBLE);
                errorDisplay.setVisibility(View.INVISIBLE);
                movieList.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * Fetch movie data from theMovieDB with the specified sort order.
     *
     * @param sortOrder The sort order to use during data fetch.
     */
    private void loadMovieData(MovieSort sortOrder) {
        changeUi(UiState.LOADING, null);
        new FetchMovieTask(fetchActions, sortOrder).execute();
    }

    /**
     * Calculate the number of columns of the grid to properly fit the screen of the device.
     *
     * @param context The context of the app
     * @return The number of columns that best fit the device screen
     */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 128);
    }

    public enum MovieSort {
        MOST_POPULAR,
        TOP_RATED
    }

    public enum UiState {
        LOADING,
        SHOW_MOVIE,
        SHOW_ERROR
    }

    public interface OnFetchResultHandler {
        void onFetchSuccess(List<Movie> movies);
        void onFetchError(String errorMessage);
    }
}
