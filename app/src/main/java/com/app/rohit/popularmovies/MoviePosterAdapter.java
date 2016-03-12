package com.app.rohit.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rohit on 3/12/2016.
 */
public class MoviePosterAdapter extends ArrayAdapter<String> {
    final private String LOG_TAG = MoviePosterAdapter.class.getSimpleName();

    public MoviePosterAdapter(Activity activity, List<String> list){
        super(activity, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String path = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_movie, parent, false);
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.grid_movie_poster);
            convertView.setTag(viewHolder);
        }

        String httpPath = "http://image.tmdb.org/t/p/w185" + path;
        Picasso.with(getContext()).load(httpPath).into(viewHolder.imageView);

        return convertView;
    }

    private class ViewHolder{
        ImageView imageView;
    }
}
