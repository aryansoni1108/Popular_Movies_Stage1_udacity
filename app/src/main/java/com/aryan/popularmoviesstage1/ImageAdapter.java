package com.aryan.popularmoviesstage1;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ImageAdapter extends BaseAdapter {
    private final Context mContext;
    private ArrayList<Movies> mMovieurl = new ArrayList<>();

    public ImageAdapter(Context context,ArrayList<Movies> movieurl) {
        mContext = context;
        mMovieurl = movieurl;
    }

    @Override
    public int getCount() {
        if(mMovieurl.size()==0){
            return -1;
        }
        else {
            return mMovieurl.size();
        }
    }

    @Override
    public Object getItem(int i) {
        if (mMovieurl.size() == 0) {
            return null;
        }

        return mMovieurl.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }

        if (mContext != null) {
            Picasso.with(mContext)
                    .load(mMovieurl.get(i).getmPosterPath())
                    .resize(mContext.getResources().getInteger(R.integer.tmdb_poster_w185_width),
                            mContext.getResources().getInteger(R.integer.tmdb_poster_w185_height))
                    .error(R.drawable.no_image)
                    .placeholder(R.drawable.placeholderimage)
                    .into(imageView);
            Log.e("dkdk9999",mMovieurl.get(i).getmPosterPath());
        }

        return imageView;

    }






}
