package com.app.rohit.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rohit on 3/12/2016.
 */
public class MovieDetails implements Parcelable{
    String posterPath, overview, releaseDate, originalTitle;
    Double voteAverage;

    public MovieDetails(String posterPath, String overview, String releaseDate, String originalTitle, Double voteAverage){
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeString(originalTitle);
        parcel.writeDouble(voteAverage);
    }

    private MovieDetails(Parcel parcel){
        this.posterPath = parcel.readString();
        this.overview = parcel.readString();
        this.releaseDate = parcel.readString();
        this.originalTitle = parcel.readString();
        this.voteAverage = parcel.readDouble();
    }

    public final Parcelable.Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel parcel) {
            return new MovieDetails(parcel);
        }

        @Override
        public MovieDetails[] newArray(int i) {
            return new MovieDetails[0];
        }
    };
}
