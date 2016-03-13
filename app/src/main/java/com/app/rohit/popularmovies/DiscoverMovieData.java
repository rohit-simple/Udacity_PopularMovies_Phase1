package com.app.rohit.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class DiscoverMovieData {
    Integer page;
    List<Result> results;

    public DiscoverMovieData(Integer page, List<Result> results) {
        this.page = page;
        this.results = results;
    }

    public Integer getPage() {
        return page;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    static class Result implements Parcelable{
        String poster_path;
        String overview;
        String release_date;
        String original_title;
        Integer id;
        Double vote_average;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(poster_path);
            parcel.writeString(overview);
            parcel.writeString(release_date);
            parcel.writeString(original_title);
            parcel.writeInt(id);
            parcel.writeDouble(vote_average);
        }

        private Result(Parcel in){
            this.poster_path = in.readString();
            this.overview = in.readString();
            this.release_date = in.readString();
            this.original_title = in.readString();
            this.id = in.readInt();
            this.vote_average = in.readDouble();
        }

        private final Parcelable.Creator<Result> CREATOR = new Creator<Result>() {
            @Override
            public Result createFromParcel(Parcel parcel) {
                return new Result(parcel);
            }

            @Override
            public Result[] newArray(int i) {
                return new Result[0];
            }
        };

        public Result(String poster_path, String overview, String release_date, String original_title, Integer id, Double vote_average) {
            this.poster_path = poster_path;
            this.overview = overview;
            this.release_date = release_date;
            this.original_title = original_title;
            this.id = id;
            this.vote_average = vote_average;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public String getOverview() {
            return overview;
        }

        public String getRelease_date() {
            return release_date;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public Integer getId() {
            return id;
        }

        public Double getVote_average() {
            return vote_average;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public void setVote_average(Double vote_average) {
            this.vote_average = vote_average;
        }
    }
}
