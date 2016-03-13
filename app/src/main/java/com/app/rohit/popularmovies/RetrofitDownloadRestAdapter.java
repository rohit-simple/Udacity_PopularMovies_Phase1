package com.app.rohit.popularmovies;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

public class RetrofitDownloadRestAdapter {
    private RetrofitDownloadInterface retrofitDownloadInterface;
    private final String API_KEY = BuildConfig.THEMOVIEDB_API_KEY;

    public RetrofitDownloadRestAdapter(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitDownloadInterface = retrofit.create(RetrofitDownloadInterface.class);
    }

    public Call<DiscoverMovieData> discoverMovies(String sortBy){
        return retrofitDownloadInterface.discoverMovies(sortBy, API_KEY);
    }
}
