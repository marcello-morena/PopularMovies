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

package com.ninetyslide.ext.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninetyslide.ext.popularmovies.R;
import com.ninetyslide.ext.popularmovies.bean.Movie;
import com.ninetyslide.ext.popularmovies.common.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.ninetyslide.ext.popularmovies.util.NetworkUtils.generatePosterUrl;

/**
 * The Adapter for the RecyclerView of Movies.
 *
 * @author Marcello Morena
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private OnMovieClickHandler clickHandler;

    /**
     * Create a new adapter providing the handler for clicks on a movie.
     *
     * @param clickHandler The handler of clicks on a movie.
     */
    public MovieAdapter(OnMovieClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(
                R.layout.item_movie_list,
                parent,
                false
        );
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder movieHolder, int position) {
        Movie movie = movies.get(position);
        TextView movieTitle = movieHolder.movieTitle;
        ImageView moviePosterThumb = movieHolder.moviePosterThumb;
        Context context = moviePosterThumb.getContext();

        // Set the title of the movie in the TextView and in the description of ImageView
        movieTitle.setText(trimStringIfTooLong(movie.getTitle()));
        moviePosterThumb.setContentDescription(movie.getTitle());

        // Use Picasso to load the Movie Poster Thumbnail
        Picasso.with(context)
                .load(generatePosterUrl(movie.getPosterPath(), Constants.MOVIEDB_POSTER_WIDTH_185))
                .into(moviePosterThumb);
    }

    /**
     * The number of movies to display.
     *
     * @return The number of movies available
     */
    @Override
    public int getItemCount() {
        return movies != null ? movies.size() : 0;
    }

    /**
     * Set a new list of movies in the adapter and notify the change.
     *
     * @param movies The new list of movies.
     */
    public void setMovieData(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    private String trimStringIfTooLong(String originalString) {
        if (originalString.length() > Constants.STRING_MAX_LENGTH) {
            return originalString.substring(0, Constants.STRING_MAX_LENGTH-3) + "...";
        }

        return originalString;
    }

    /**
     * The ViewHolder class for the movies views.
     */
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView movieTitle;
        final ImageView moviePosterThumb;

        /**
         * Set the click listener when the object is ceated.
         *
         * @param itemView The created view
         */
        public MovieViewHolder(View itemView) {
            super(itemView);
            movieTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
            moviePosterThumb = (ImageView) itemView.findViewById(R.id.iv_movie_poster_thumb);
            moviePosterThumb.setOnClickListener(this);
        }

        /**
         * When a movie is clicked, invoke the main click handler passing the ID of the Movie.
         *
         * @param view The clicked view
         */
        @Override
        public void onClick(View view) {
            Movie movie = movies.get(getAdapterPosition());
            clickHandler.onClick(movie, view.findViewById(R.id.iv_movie_poster_thumb));
        }
    }

    /**
     * The interface to handle a click event on a movie in the list.
     */
    public interface OnMovieClickHandler {
        void onClick(Movie movie, View iv);
    }
}
