package com.app.rohit.popularmovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitDownloadInterface {

    @GET("discover/movie")
    Call<DiscoverMovieData> discoverMovies(
            @Query("sort_by") String sortBy,
            @Query("api_key") String apiKey
    );

}
