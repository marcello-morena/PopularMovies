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

package com.ninetyslide.ext.popularmovies.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Bean representing a Movie with the most important information returned from the theMovieDB API.
 *
 * @author Marcello Morena
 */
public class Movie implements Parcelable {

    private String posterPath; // Show in the details
    private String overview; // Show in the details
    private long id;
    private String title;
    private String originalTitle; // Show in the details
    private String releaseDate; // Show in the details
    private String voteAverage; // Show in the details

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(posterPath);
        out.writeString(overview);
        out.writeLong(id);
        out.writeString(title);
        out.writeString(originalTitle);
        out.writeString(releaseDate);
        out.writeString(voteAverage);
    }

    public Movie() {
    }

    public Movie(Parcel in) {
        posterPath = in.readString();
        overview = in.readString();
        id = in.readLong();
        title = in.readString();
        originalTitle = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }
}
