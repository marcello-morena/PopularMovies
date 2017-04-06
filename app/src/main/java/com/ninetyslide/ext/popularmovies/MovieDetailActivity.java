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
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ninetyslide.ext.popularmovies.bean.Movie;
import com.ninetyslide.ext.popularmovies.bean.Review;
import com.ninetyslide.ext.popularmovies.bean.Video;
import com.ninetyslide.ext.popularmovies.common.Constants;
import com.ninetyslide.ext.popularmovies.util.FetchReviewTask;
import com.ninetyslide.ext.popularmovies.util.FetchVideoTask;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ninetyslide.ext.popularmovies.util.FavoriteMovieUtils.isMovieFavorite;
import static com.ninetyslide.ext.popularmovies.util.FavoriteMovieUtils.toggleFavorite;
import static com.ninetyslide.ext.popularmovies.util.NetworkUtils.generatePosterUrl;

/**
 * Activity that shows the details of a movie selected in the main activity.
 *
 * @author Marcello Morena
 */
public class MovieDetailActivity extends AppCompatActivity {

    private Movie movie;

    @BindView(R.id.ib_favorite_toggle) ImageButton favoriteToggle;

    @BindView(R.id.pb_video_load) ProgressBar videoProgressBar;
    @BindView(R.id.tv_video_error) TextView videoErrorView;
    @BindView(R.id.iv_video_1) TextView videoTv1;
    @BindView(R.id.iv_video_2) TextView videoTv2;
    @BindView(R.id.iv_video_3) TextView videoTv3;
    @BindView(R.id.iv_play_1) ImageView videoIv1;
    @BindView(R.id.iv_play_2) ImageView videoIv2;
    @BindView(R.id.iv_play_3) ImageView videoIv3;
    @BindView(R.id.sep_videos_1) View videoSep1;
    @BindView(R.id.sep_videos_2) View videoSep2;

    @BindView(R.id.pb_review_load) ProgressBar reviewProgressBar;
    @BindView(R.id.tv_review_error) TextView reviewErrorView;
    @BindView(R.id.tv_review_author_1) TextView reviewAuthor1;
    @BindView(R.id.tv_review_content_1) TextView reviewContent1;
    @BindView(R.id.tv_review_author_2) TextView reviewAuthor2;
    @BindView(R.id.tv_review_content_2) TextView reviewContent2;
    @BindView(R.id.tv_review_author_3) TextView reviewAuthor3;
    @BindView(R.id.tv_review_content_3) TextView reviewContent3;
    @BindView(R.id.sep_reviews_1) View reviewSep1;
    @BindView(R.id.sep_reviews_2) View reviewSep2;

    private FetchVideoTask.OnFetchVideoResultHandler fetchVideoResultHandler = new FetchVideoTask.OnFetchVideoResultHandler() {
        @Override
        public void onFetchSuccess(List<Video> videos) {
            videoProgressBar.setVisibility(View.GONE);

            if (videos.isEmpty()) {
                videoErrorView.setText(getString(R.string.no_video_error));
                videoErrorView.setVisibility(View.VISIBLE);
                return;
            }

            Video video;

            try {
                if ((video = videos.get(0)) != null) {
                    videoTv1.setText(video.getName());
                    videoTv1.setVisibility(View.VISIBLE);
                    videoTv1.setTag(video.getKey());
                    videoIv1.setVisibility(View.VISIBLE);
                    videoIv1.setTag(video.getKey());
                }

                if ((video = videos.get(1)) != null) {
                    videoSep1.setVisibility(View.VISIBLE);
                    videoTv2.setText(video.getName());
                    videoTv2.setVisibility(View.VISIBLE);
                    videoTv2.setTag(video.getKey());
                    videoIv2.setVisibility(View.VISIBLE);
                    videoIv2.setTag(video.getKey());
                }

                if ((video = videos.get(2)) != null) {
                    videoSep2.setVisibility(View.VISIBLE);
                    videoTv3.setText(video.getName());
                    videoTv3.setVisibility(View.VISIBLE);
                    videoTv3.setTag(video.getKey());
                    videoIv3.setVisibility(View.VISIBLE);
                    videoIv3.setTag(video.getKey());
                }
            } catch (IndexOutOfBoundsException e) {
                return;
            }
        }

        @Override
        public void onFetchError(String errorMessage) {
            videoProgressBar.setVisibility(View.GONE);

            videoErrorView.setText(errorMessage);
            videoErrorView.setVisibility(View.VISIBLE);
        }
    };

    private FetchReviewTask.OnFetchReviewResultHandler fetchReviewResultHandler = new FetchReviewTask.OnFetchReviewResultHandler() {
        @Override
        public void onFetchSuccess(List<Review> reviews) {
            reviewProgressBar.setVisibility(View.GONE);

            if (reviews.isEmpty()) {
                reviewErrorView.setText(getString(R.string.no_review_error));
                reviewErrorView.setVisibility(View.VISIBLE);
                return;
            }

            Review review;

            try {
                if ((review = reviews.get(0)) != null) {
                    reviewAuthor1.setText(review.getAuthor());
                    reviewAuthor1.setVisibility(View.VISIBLE);
                    reviewContent1.setText(review.getContent());
                    reviewContent1.setVisibility(View.VISIBLE);
                }

                if ((review = reviews.get(1)) != null) {
                    reviewSep1.setVisibility(View.VISIBLE);
                    reviewAuthor2.setText(review.getAuthor());
                    reviewAuthor2.setVisibility(View.VISIBLE);
                    reviewContent2.setText(review.getContent());
                    reviewContent2.setVisibility(View.VISIBLE);
                }

                if ((review = reviews.get(2)) != null) {
                    reviewSep2.setVisibility(View.VISIBLE);
                    reviewAuthor3.setText(review.getAuthor());
                    reviewAuthor3.setVisibility(View.VISIBLE);
                    reviewContent3.setText(review.getContent());
                    reviewContent3.setVisibility(View.VISIBLE);
                }
            } catch (IndexOutOfBoundsException e) {
                return;
            }
        }

        @Override
        public void onFetchError(String errorMessage) {
            reviewProgressBar.setVisibility((View.GONE));

            reviewErrorView.setText(errorMessage);
            reviewErrorView.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        // Retrieve movie data from intent
        Intent intent = getIntent();
        movie = intent.getParcelableExtra(Constants.INTENT_MOVIE_EXTRA);

        // Populate movie description
        setTitle(movie.getTitle());
        ((TextView) findViewById(R.id.tv_movie_original_title)).setText(movie.getOriginalTitle());
        ((TextView) findViewById(R.id.tv_movie_release_date)).setText(movie.getReleaseDate());
        ((TextView) findViewById(R.id.tv_movie_vote_average)).setText(movie.getVoteAverage());
        ((TextView) findViewById(R.id.tv_movie_overview)).setText(movie.getOverview());

        // Show the right image for the favorite button
        updateFavoriteButton(isMovieFavorite(this, movie.getId()));

        favoriteToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFavoriteButton(toggleFavorite(MovieDetailActivity.this, movie));
            }
        });

        // Use Picasso to load the Movie Poster Thumbnail
        ImageView moviePoster = (ImageView) findViewById(R.id.iv_movie_poster);
        moviePoster.setContentDescription(movie.getOriginalTitle());
        Picasso.with(this)
                .load(generatePosterUrl(movie.getPosterPath(), Constants.MOVIEDB_POSTER_WIDTH_185))
                .placeholder(R.mipmap.poster_placeholder)
                .error(R.mipmap.poster_load_error)
                .into(moviePoster);

        // Load Videos from the network
        loadVideos(String.valueOf(movie.getId()));

        // Load Reviews from the network
        loadReviews(String.valueOf(movie.getId()));
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

    @OnClick({R.id.iv_video_1, R.id.iv_video_2, R.id.iv_video_3, R.id.iv_play_1, R.id.iv_play_2, R.id.iv_play_3})
    public void openYoutubeVideo(View view) {
        String youtubeKey = (String) view.getTag();

        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeKey));

        if (youtubeIntent.resolveActivity(getPackageManager()) == null) {
            youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + youtubeKey));
        }

        startActivity(youtubeIntent);
    }

    private void updateFavoriteButton(boolean isFavorite) {
        if (isFavorite) {
            favoriteToggle.setImageResource(R.mipmap.star_selected);
        } else {
            favoriteToggle.setImageResource(R.mipmap.star_unselected);
        }
    }

    private void loadVideos(String movieId) {
        videoProgressBar.setVisibility(View.VISIBLE);
        new FetchVideoTask(this, fetchVideoResultHandler, movieId).execute();
    }

    private void loadReviews(String movieId) {
        reviewProgressBar.setVisibility(View.VISIBLE);
        new FetchReviewTask(this, fetchReviewResultHandler, movieId).execute();
    }
}
