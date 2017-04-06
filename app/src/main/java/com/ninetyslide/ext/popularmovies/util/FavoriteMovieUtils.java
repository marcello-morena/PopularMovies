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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import com.ninetyslide.ext.popularmovies.bean.Movie;

import java.util.ArrayList;
import java.util.List;

import static com.ninetyslide.ext.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID;
import static com.ninetyslide.ext.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.COLUMN_ORIGINAL_TITLE;
import static com.ninetyslide.ext.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.COLUMN_OVERVIEW;
import static com.ninetyslide.ext.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POSTER_PATH;
import static com.ninetyslide.ext.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE;
import static com.ninetyslide.ext.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE;
import static com.ninetyslide.ext.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE;
import static com.ninetyslide.ext.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI;

/**
 * Class containing the utilities to perform movie data fetch from the content provider.
 *
 * @author Marcello Morena
 */

public final class FavoriteMovieUtils {

    private FavoriteMovieUtils() {
    }

    /**
     * Fetch the list of favorite movies from content provider.
     *
     * @param context The context used to access the content provider
     * @return The list of favorite movies
     */
    public static List<Movie> fetchFavoriteMovies(Context context) {

        // Query the Content Provider
        Cursor favoriteMoviesCursor = context.getContentResolver().query(
                CONTENT_URI,
                null,
                null,
                null,
                null);

        List<Movie> favoriteMovies = new ArrayList<>();

        if (favoriteMoviesCursor == null) {
            return favoriteMovies;
        }

        // Get the indices
        int movieIdIndex = favoriteMoviesCursor.getColumnIndex(COLUMN_MOVIE_ID);
        int originalTitleIndex = favoriteMoviesCursor.getColumnIndex(COLUMN_ORIGINAL_TITLE);
        int overviewIndex = favoriteMoviesCursor.getColumnIndex(COLUMN_OVERVIEW);
        int titleIndex = favoriteMoviesCursor.getColumnIndex(COLUMN_TITLE);
        int releaseDateIndex = favoriteMoviesCursor.getColumnIndex(COLUMN_RELEASE_DATE);
        int voteAverageIndex = favoriteMoviesCursor.getColumnIndex(COLUMN_VOTE_AVERAGE);
        int posterPathIndex = favoriteMoviesCursor.getColumnIndex(COLUMN_POSTER_PATH);

        // Create a list of movies from the cursor content
        favoriteMoviesCursor.moveToFirst();
        while (!favoriteMoviesCursor.isAfterLast()) {
            Movie movie = new Movie();
            movie.setId(favoriteMoviesCursor.getLong(movieIdIndex));
            movie.setOriginalTitle(favoriteMoviesCursor.getString(originalTitleIndex));
            movie.setOverview(favoriteMoviesCursor.getString(overviewIndex));
            movie.setTitle(favoriteMoviesCursor.getString(titleIndex));
            movie.setReleaseDate(favoriteMoviesCursor.getString(releaseDateIndex));
            movie.setVoteAverage(favoriteMoviesCursor.getString(voteAverageIndex));
            movie.setPosterPath(favoriteMoviesCursor.getString(posterPathIndex));

            favoriteMovies.add(movie);
            favoriteMoviesCursor.moveToNext();
        }

        favoriteMoviesCursor.close();

        return favoriteMovies;
    }

    /**
     * Create the URI to query the content provider about a specific movie.
     *
     * @param movieId The id of the desired movie
     * @return The created URI
     */
    public static Uri buildUriMovieWithId(long movieId) {
        return CONTENT_URI.buildUpon()
                .appendPath(String.valueOf(movieId))
                .build();
    }

    /**
     * Check whether a movie is favorite.
     *
     * @param movieId The ID of the movie to check
     * @param context The context used to access the content provider
     * @return True if is favorite, false if not
     */
    public static boolean isMovieFavorite(Context context, long movieId) {
        Cursor cursor = context.getContentResolver().query(
                buildUriMovieWithId(movieId),
                null,
                null,
                null,
                null
        );

        if (cursor == null || cursor.getCount() == 0) {
            return false;
        } else {
            cursor.close();
            return true;
        }
    }

    /**
     * Toggle the favorite state of a movie.
     *
     * @param movie The movie to toggle
     * @param context The context used to access the content provider
     * @return The state of the movie after the toggle
     */
    public static boolean toggleFavorite(Context context, Movie movie) {
        if (isMovieFavorite(context, movie.getId())) {
            return deleteFavoriteMovie(context, movie.getId());
        } else {
            return insertFavoriteMovie(context, movie);
        }
    }

    /**
     * Insert a movie among the favorites.
     *
     * @param context The context used to access the content provider
     * @param movie The movie to insert
     * @return The state of the movie after the insertion attempt
     */
    private static boolean insertFavoriteMovie(Context context, Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MOVIE_ID, movie.getId());
        cv.put(COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        cv.put(COLUMN_OVERVIEW, movie.getOverview());
        cv.put(COLUMN_POSTER_PATH, movie.getPosterPath());
        cv.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
        cv.put(COLUMN_TITLE, movie.getTitle());
        cv.put(COLUMN_VOTE_AVERAGE, movie.getVoteAverage());

        try {
            context.getContentResolver().insert(CONTENT_URI, cv);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Delete a movie from the favorites.
     *
     * @param context The context used to access the content provider
     * @param movieId The movie to delete
     * @return The state of the movie after the deletion attempt
     */
    private static boolean deleteFavoriteMovie(Context context, long movieId) {
        context.getContentResolver().delete(
                buildUriMovieWithId(movieId),
                null,
                null
        );

        return isMovieFavorite(context, movieId);
    }

}
