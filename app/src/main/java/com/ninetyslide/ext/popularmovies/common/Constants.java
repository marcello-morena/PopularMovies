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

package com.ninetyslide.ext.popularmovies.common;

import com.ninetyslide.ext.popularmovies.MovieListActivity;

/**
 * Class containing all the constants common to the whole project.
 *
 * @author Marcello Morena
 */
public final class Constants {

    private Constants() {
    }

    // Constants related to querying theMovieDB's API
    public static final String MOVIEDB_API_KEY = "CHANGE_ME_WITH_A_VALID_KEY!"; // TODO: Replace this value with a valid TheMovieDB API key
    public static final String MOVIEDB_API_BASE_URL = "https://api.themoviedb.org/3";
    public static final String MOVIEDB_API_TOPRATED_URL = "/movie/top_rated";
    public static final String MOVIEDB_API_POPULAR_URL = "/movie/popular";
    public static final String MOVIEDB_API_REVIEWS_URL_PATTERN = "/movie/%s/reviews";
    public static final String MOVIEDB_API_VIDEOS_URL_PATTERN = "/movie/%s/videos";
    public static final String MOVIEDB_API_PARAMETER_APIKEY = "api_key";
    public static final String MOVIEDB_API_JSON_NAME_RESULTS = "results";

    // Constants related to URLs of theMovieDB's posters
    public static final String MOVIEDB_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String MOVIEDB_POSTER_WIDTH_185 = "w185";
    public static final String MOVIEDB_POSTER_WIDTH_342 = "w342";

    // Constants related to the grid layout
    public static final int GRID_DEFAULT_COLUMN_NUMBER = 3;

    public static final MovieListActivity.MovieSort MOVIE_DEFAULT_SORT_ORDER = MovieListActivity.MovieSort.MOST_POPULAR;
    public static final String INTENT_MOVIE_EXTRA = "movie";
    public static final int STRING_MAX_LENGTH = 15;

}
