package com.app.rohit.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment {

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        TextView originalTitleTextView = (TextView) view.findViewById(R.id.movie_details_title);
        TextView releaseDateTextView = (TextView) view.findViewById(R.id.movie_details_release_date);
        TextView voteAverageTextView = (TextView) view.findViewById(R.id.movie_details_vote_average);
        TextView overviewTextView = (TextView) view.findViewById(R.id.movie_details_overview);
        ImageView posterImageView = (ImageView) view.findViewById(R.id.movie_details_poster);

        Intent intent = getActivity().getIntent();

        if(intent != null) {
            if (intent.hasExtra(MainActivityFragment.EXTRA_ORIGINAL_TITLE)) {
                originalTitleTextView.setText(intent.getStringExtra(MainActivityFragment.EXTRA_ORIGINAL_TITLE));
            }
            if (intent.hasExtra(MainActivityFragment.EXTRA_RELEASE_DATE)) {
                releaseDateTextView.setText(intent.getStringExtra(MainActivityFragment.EXTRA_RELEASE_DATE));
            }
            if (intent.hasExtra(MainActivityFragment.EXTRA_VOTE_AVERAGE)) {
                voteAverageTextView.setText(intent.getDoubleExtra(MainActivityFragment.EXTRA_VOTE_AVERAGE, 0) + "");
            }
            if (intent.hasExtra(MainActivityFragment.EXTRA_OVERVIEW)) {
                String overview = intent.getStringExtra(MainActivityFragment.EXTRA_OVERVIEW);
                if(overview == null || overview.equals("null") || overview.trim().equals("")){
                    overviewTextView.setText(getString(R.string.fragment_movie_details_no_overview));
                }else{
                    overviewTextView.setText(overview);
                }
                //feature added to make scroll possible for textview
                //help taken from stackoverflow
                overviewTextView.setMovementMethod(new ScrollingMovementMethod());
            }
            if (intent.hasExtra(MainActivityFragment.EXTRA_POSTER_PATH)) {
                String path = intent.getStringExtra(MainActivityFragment.EXTRA_POSTER_PATH);
                Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185" + path).into(posterImageView);
            }
        }
        return view;
    }
}
