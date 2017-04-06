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

import com.ninetyslide.ext.popularmovies.R;
import com.ninetyslide.ext.popularmovies.bean.Video;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * AsyncTask that handles all the retrieval of videos data.
 *
 * @author Marcello Morena
 */

public class FetchVideoTask extends AsyncTask<Void, Void, List<Video>> {

    private Context context;
    private String errorMessage;
    private URL movieVideoUrl;
    private OnFetchVideoResultHandler fetchVideoResultHandler;

    public FetchVideoTask(Context context, OnFetchVideoResultHandler fetchVideoResultHandler, String movieId) {
        this.context = context;
        this.fetchVideoResultHandler = fetchVideoResultHandler;
        this.movieVideoUrl = NetworkUtils.buildVideosUrl(movieId);
    }

    @Override
    protected List<Video> doInBackground(Void... voids) {
        try {
            return NetworkUtils.fetchVideoData(movieVideoUrl);
        } catch (IOException e) {
            if (!NetworkUtils.isNetworkAvailable(context)) {
                errorMessage = context.getString(R.string.no_network_error);
            } else {
                errorMessage = e.getMessage();
            }
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Video> videos) {
        if (videos == null) {
            fetchVideoResultHandler.onFetchError(errorMessage);
        } else {
            fetchVideoResultHandler.onFetchSuccess(videos);
        }
    }

    public interface OnFetchVideoResultHandler {
        void onFetchSuccess(List<Video> movies);
        void onFetchError(String errorMessage);
    }

}
