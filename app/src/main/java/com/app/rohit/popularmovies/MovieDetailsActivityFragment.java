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

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailsActivityFragment extends Fragment {
    @Bind(R.id.movie_details_title) protected TextView originalTitleTextView;
    @Bind(R.id.movie_details_release_date) protected TextView releaseDateTextView;
    @Bind(R.id.movie_details_vote_average) protected TextView voteAverageTextView;
    @Bind(R.id.movie_details_overview) protected TextView overviewTextView;
    @Bind(R.id.movie_details_poster) protected ImageView posterImageView;

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);

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
                Picasso.with(getActivity())
                        .load("http://image.tmdb.org/t/p/w185" + path)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.no_image)
                        .into(posterImageView);
            }
        }
        return view;
    }
}
