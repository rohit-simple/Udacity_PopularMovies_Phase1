package com.app.rohit.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private ArrayList<MovieDetails> movieDetailsList;
    private List<String> moviePathList;
    private GridView gridView;

    private final Integer FROM_MAINACTIVITY_TO_SETTINGS = 111;
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    static String EXTRA_ORIGINAL_TITLE = "original_title";
    static String EXTRA_OVERVIEW = "overview";
    static String EXTRA_RELEASE_DATE = "release_date";
    static String EXTRA_POSTER_PATH = "poster_path";
    static String EXTRA_VOTE_AVERAGE = "vote_average";

    final String KEY_MOVIE_DETAILS_LIST = "movie_details_list";

    public MainActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) view.findViewById(R.id.grid_movies);
        if(savedInstanceState == null || !savedInstanceState.containsKey(KEY_MOVIE_DETAILS_LIST)){
            new DiscoverMoviesTask().execute();
        }else{
            movieDetailsList = savedInstanceState.getParcelableArrayList(KEY_MOVIE_DETAILS_LIST);
            setListView();
        }
        decideTitle();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_fragment, menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_MOVIE_DETAILS_LIST, movieDetailsList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                startActivityForResult(new Intent(getActivity(), SettingsActivity.class), FROM_MAINACTIVITY_TO_SETTINGS);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void decideTitle(){
        String title = getString(R.string.title_activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = sharedPreferences.getString(getString(R.string.action_sort_by_key), getString(R.string.action_sort_by_default));
        if(sortBy.equals(getString(R.string.action_sort_by_value_most_popular))){
            title = title + "-" + getString(R.string.action_sort_by_entry_most_popular);
        }else if(sortBy.equals(getString(R.string.action_sort_by_value_highest_rated))){
            title = title + "-" + getString(R.string.action_sort_by_entry_highest_rated);
        }
        getActivity().setTitle(title);
    }

    private void setListView(){
        moviePathList = new ArrayList<>();
        for(MovieDetails movieDetails: movieDetailsList){
            moviePathList.add(movieDetails.posterPath);
        }

        MoviePosterAdapter moviePosterAdapter = new MoviePosterAdapter(getActivity(), moviePathList);
        gridView.setAdapter(moviePosterAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                MovieDetails movieDetails = movieDetailsList.get(i);
                intent.putExtra(EXTRA_ORIGINAL_TITLE, movieDetails.originalTitle);
                intent.putExtra(EXTRA_OVERVIEW, movieDetails.overview);
                intent.putExtra(EXTRA_POSTER_PATH, movieDetails.posterPath);
                intent.putExtra(EXTRA_RELEASE_DATE, movieDetails.releaseDate);
                intent.putExtra(EXTRA_VOTE_AVERAGE, movieDetails.voteAverage);
                getActivity().startActivity(intent);
            }
        });
    }

    private class DiscoverMoviesTask extends AsyncTask<Void, Void, Void>{
        private final String LOG_TAG = DiscoverMoviesTask.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String sortMethod = null;

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortMethodInPreference = sharedPreferences.getString(getString(R.string.action_sort_by_key), getString(R.string.action_sort_by_default));
            if(sortMethodInPreference.equals(getString(R.string.action_sort_by_value_most_popular))){
                sortMethod = "popularity.desc";
            }else if(sortMethodInPreference.equals(getString(R.string.action_sort_by_value_highest_rated))){
                sortMethod = "vote_average.desc";
            }
            String apiKey = BuildConfig.THEMOVIEDB_API_KEY;

            try {
                final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
                final String SORTBY = "sort_by";
                final String APIKEY = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(SORTBY, sortMethod)
                        .appendQueryParameter(APIKEY, apiKey)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    movieDetailsList = null;
                    moviePathList = null;
                }
                else{
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line.concat("\n"));
                    }

                    if (buffer.length() == 0) {
                        movieDetailsList = null;
                        moviePathList = null;
                    }
                    Log.v(LOG_TAG, buffer.toString());
                    movieDetailsList = new ArrayList<>(retrieveMovieDetails(buffer.toString()));
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage());
                movieDetailsList = null;
                moviePathList = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, e.getMessage());
                        movieDetailsList = null;
                        moviePathList = null;
                    }
                }
            }
            return null;
        }

        private List<MovieDetails> retrieveMovieDetails(String str) throws JSONException{
            final String RESULTS = "results";
            final String POSTERPATH = "poster_path";
            final String OVERVIEW = "overview";
            final String RELEASEDATE = "release_date";
            final String ORIGINALTITLE = "original_title";
            final String VOTEAVERAGE = "vote_average";

            JSONObject jsonReceived = new JSONObject(str);
            JSONArray results = jsonReceived.getJSONArray(RESULTS);
            List<MovieDetails> result = new ArrayList<>();
            for(int i = 0; i < results.length(); i++){
                JSONObject jsonObject = results.getJSONObject(i);
                String path = jsonObject.getString(POSTERPATH);
                /*
                If path contains null, no image can be shown.
                As question just demands gridview to contain thumbnails, then there will be no movie name or image for user to know about the movie.
                Hence, skipping such movies at all.
                 */
                if(!path.equals("null")){
                    result.add(new MovieDetails(jsonObject.getString(POSTERPATH), jsonObject.getString(OVERVIEW), jsonObject.getString(RELEASEDATE), jsonObject.getString(ORIGINALTITLE), jsonObject.getDouble(VOTEAVERAGE)));
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Void voidval) {
            super.onPostExecute(voidval);
            setListView();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FROM_MAINACTIVITY_TO_SETTINGS){
            decideTitle();
            new DiscoverMoviesTask().execute();
        }
    }
}
