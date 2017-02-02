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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninetyslide.ext.popularmovies.bean.Movie;
import com.ninetyslide.ext.popularmovies.common.Constants;
import com.squareup.picasso.Picasso;

import static com.ninetyslide.ext.popularmovies.util.NetworkUtils.generatePosterUrl;

/**
 * Activity that shows the details of a movie selected in the main activity.
 *
 * @author Marcello Morena
 */
public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Retrieve movie data from intent
        Intent intent = getIntent();
        Movie movie = (Movie) intent.getParcelableExtra(Constants.INTENT_MOVIE_EXTRA);

        // Populate movie description
        setTitle(movie.getTitle());
        ((TextView) findViewById(R.id.tv_movie_original_title)).setText(movie.getOriginalTitle());
        ((TextView) findViewById(R.id.tv_movie_release_date)).setText(movie.getReleaseDate());
        ((TextView) findViewById(R.id.tv_movie_vote_average)).setText(movie.getVoteAverage());
        ((TextView) findViewById(R.id.tv_movie_overview)).setText(movie.getOverview());

        // Use Picasso to load the Movie Poster Thumbnail
        ImageView moviePoster = (ImageView) findViewById(R.id.iv_movie_poster);
        moviePoster.setContentDescription(movie.getOriginalTitle());
        Picasso.with(this)
                .load(generatePosterUrl(movie.getPosterPath(), Constants.MOVIEDB_POSTER_WIDTH_185))
                .into(moviePoster);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
