package com.app.rohit.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Response;

public class MainActivityFragment extends Fragment {
    private List<DiscoverMovieData.Result> movieDetailsList;
    private List<String> moviePathList;
    private MoviePosterAdapter moviePosterAdapter;

    @Bind(R.id.grid_movies) protected GridView gridView;

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
        ButterKnife.bind(this, view);
        if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_MOVIE_DETAILS_LIST)) {
            new DownloadTask().execute();
        } else {
            movieDetailsList = savedInstanceState.getParcelableArrayList(KEY_MOVIE_DETAILS_LIST);
            if (movieDetailsList != null && !movieDetailsList.isEmpty()) {
                setListView();
            } else {
                new DownloadTask().execute();
                Log.d(LOG_TAG, "onCreateView() -> savedInstanceState contains KEY_MOVIE_DETAILS_LIST's value empty or null");
            }
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
        if (movieDetailsList != null && !movieDetailsList.isEmpty()) {
            outState.putParcelableArrayList(KEY_MOVIE_DETAILS_LIST, new ArrayList<>(movieDetailsList));
        } else {
            Log.d(LOG_TAG, "onSaveInstanceState() -> movieDetailsList is empty or null");
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivityForResult(new Intent(getActivity(), SettingsActivity.class), FROM_MAINACTIVITY_TO_SETTINGS);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void decideTitle() {
        String title = getString(R.string.title_activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPreferences.contains(getString(R.string.action_sort_by_key))) {
            String sortBy = sharedPreferences.getString(getString(R.string.action_sort_by_key), getString(R.string.action_sort_by_default));
            if (sortBy.equals(getString(R.string.action_sort_by_value_most_popular))) {
                title = title + "-" + getString(R.string.action_sort_by_entry_most_popular);
            } else if (sortBy.equals(getString(R.string.action_sort_by_value_highest_rated))) {
                title = title + "-" + getString(R.string.action_sort_by_entry_highest_rated);
            }
        } else {
            Log.d(LOG_TAG, "decideTitle() -> sharedPreferences doesn't contain action_sort_by_key");
        }
        getActivity().setTitle(title);
    }

    private void setListView() {
        moviePathList = new ArrayList<>();
        for (DiscoverMovieData.Result movieDetails : movieDetailsList) {
            moviePathList.add(movieDetails.getPoster_path());
        }

        moviePosterAdapter = new MoviePosterAdapter(getActivity(), moviePathList);
        gridView.setAdapter(moviePosterAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                DiscoverMovieData.Result movieDetails = movieDetailsList.get(i);
                if (movieDetails != null) {
                    intent.putExtra(EXTRA_ORIGINAL_TITLE, movieDetails.getOriginal_title());
                    intent.putExtra(EXTRA_OVERVIEW, movieDetails.getOverview());
                    intent.putExtra(EXTRA_POSTER_PATH, movieDetails.getPoster_path());
                    intent.putExtra(EXTRA_RELEASE_DATE, movieDetails.getRelease_date());
                    intent.putExtra(EXTRA_VOTE_AVERAGE, movieDetails.getVote_average());
                } else {
                    Log.d(LOG_TAG, "gridView onItemClickListener() -> movieDetails retrieved is null");
                }
                getActivity().startActivity(intent);
            }
        });
    }

    private class DownloadTask extends AsyncTask<Void, Integer, Void> {
        private final String LOG_TAG = DownloadTask.class.getSimpleName();
        private Integer INTERNET_ERROR_CODE = 1;

        @Override
        protected Void doInBackground(Void... voids) {
            String sortMethod = null;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            if (sharedPreferences.contains(getString(R.string.action_sort_by_key))) {
                String sortMethodInPreference = sharedPreferences.getString(getString(R.string.action_sort_by_key), getString(R.string.action_sort_by_default));
                if (sortMethodInPreference.equals(getString(R.string.action_sort_by_value_most_popular))) {
                    sortMethod = "popularity.desc";
                } else if (sortMethodInPreference.equals(getString(R.string.action_sort_by_value_highest_rated))) {
                    sortMethod = "vote_average.desc";
                }
            } else {
                Log.d(LOG_TAG, "donInBackground() -> sharedPreferences doesn't contain action_sort_by_key");
            }

            RetrofitDownloadRestAdapter retrofitDownloadRestAdapter = new RetrofitDownloadRestAdapter();
            try {
                Response<DiscoverMovieData> response = retrofitDownloadRestAdapter.discoverMovies(sortMethod).execute();
                DiscoverMovieData discoverMovieData = response.body();
                if (discoverMovieData != null) {
                    movieDetailsList = discoverMovieData.getResults();
                    /*
                    logic to remove movies where poster path is null
                    because we are just showing poster to user
                    and if there is no poster, user will get no information about what movie it is
                     */
                    int size = movieDetailsList.size();
                    for(int i = 0; i < size; i++){
                        DiscoverMovieData.Result result = movieDetailsList.get(i);
                        if (result.getPoster_path() == null || result.getPoster_path().equals("null")) {
                            movieDetailsList.remove(result);
                        }
                    }
                } else {
                    Log.d(LOG_TAG, "doInBackground() -> reponse from server has no body");
                }
            } catch (IOException e) {
                publishProgress(INTERNET_ERROR_CODE);
                Log.e(LOG_TAG, e.getMessage());
                movieDetailsList = null;
                moviePathList = null;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0].equals(INTERNET_ERROR_CODE)){
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.main_activity_fragment_toast_internet_connectivity_prob), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (movieDetailsList != null && !movieDetailsList.isEmpty()) {
                setListView();
            } else {
                Log.d(LOG_TAG, "onPostExecute -> movieDetailsList is empty or null");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FROM_MAINACTIVITY_TO_SETTINGS) {
            moviePosterAdapter.clear();
            decideTitle();
            new DownloadTask().execute();
        }
    }
}
