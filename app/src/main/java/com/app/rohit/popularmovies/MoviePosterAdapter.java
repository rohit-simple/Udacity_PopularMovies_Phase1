package com.app.rohit.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoviePosterAdapter extends ArrayAdapter<String> {
    private final String LOG_TAG = MoviePosterAdapter.class.getSimpleName();
    private final Integer MY_ROW_TYPE = 0;

    public MoviePosterAdapter(Activity activity, List<String> list){
        super(activity, 0, list);
    }

    @Override
    public int getItemViewType(int position) {
        return MY_ROW_TYPE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        String path = getItem(position);

        if(convertView == null || getItemViewType(position) != MY_ROW_TYPE){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_movie, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String httpPath = "http://image.tmdb.org/t/p/w185" + path;
        Picasso.with(getContext())
                .load(httpPath)
                .placeholder(R.drawable.loading)
                .error(R.drawable.no_image)
                .into(viewHolder.imageView);

        return convertView;
    }

    static class ViewHolder{
        @Bind(R.id.grid_movie_poster) ImageView imageView;

        ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
