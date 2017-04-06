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

package com.ninetyslide.ext.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.ninetyslide.ext.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID;
import static com.ninetyslide.ext.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI;
import static com.ninetyslide.ext.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME;

/**
 * Content Provider for Favorite movies.
 *
 * @author Marcello Morena
 */

public class FavoriteMovieContentProvider extends ContentProvider {

    public static final int FAVORITE_MOVIES = 100;
    public static final int FAVORITE_MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoriteMovieDbHelper favoriteMovieDbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_FAVORITE_MOVIES, FAVORITE_MOVIES);
        matcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.PATH_FAVORITE_MOVIES + "/#", FAVORITE_MOVIE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        favoriteMovieDbHelper = new FavoriteMovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase favMoviesDb = favoriteMovieDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor resultCursor;

        switch (match) {
            case FAVORITE_MOVIES:
                resultCursor =  favMoviesDb.query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITE_MOVIE_WITH_ID:
                String[] movieId = new String[]{uri.getLastPathSegment()};
                resultCursor =  favMoviesDb.query(
                        TABLE_NAME,
                        projection,
                        COLUMN_MOVIE_ID + "=?",
                        movieId,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        resultCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return resultCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase favMoviesDb = favoriteMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORITE_MOVIES:
                long resultId = favMoviesDb.insert(TABLE_NAME, null, values);
                if (resultId > 0) {
                    returnUri = ContentUris.withAppendedId(CONTENT_URI, resultId);
                } else {
                    throw new android.database.SQLException("Failed to insert movie into DB " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase favMoviesDb = favoriteMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int moviesDeleted;

        switch (match) {
            case FAVORITE_MOVIE_WITH_ID:
                String[] movieId = new String[]{uri.getLastPathSegment()};
                moviesDeleted = favMoviesDb.delete(TABLE_NAME, COLUMN_MOVIE_ID + "=?", movieId);
                if (moviesDeleted >= 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return moviesDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not needed at this time, so not implemented!");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not needed at this time, so not implemented!");
    }
}
