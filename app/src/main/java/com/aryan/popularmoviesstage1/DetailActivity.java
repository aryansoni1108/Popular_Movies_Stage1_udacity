package com.aryan.popularmoviesstage1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TextView ratingnum = findViewById(R.id.Rating_numView);
        TextView date = findViewById(R.id.date_view);

        TextView description_view = findViewById(R.id.description_view);
        ImageView posterView = findViewById(R.id.detailimage);
        TextView moviename = findViewById(R.id.movie_name_view);

        RatingBar ratingBar = findViewById(R.id.RatingBar);
        ratingBar.setNumStars(5);
        Movies movies = getIntent().getParcelableExtra(getString(R.string.parcel));
        date.setText(movies.getmReleaseDate());
        moviename.setText(movies.getmOriginalTitle());
        Double x = movies.getmVoteAverage();
        float ratings = (float) ((x*5)/10);

        ratingBar.setRating(ratings);

        ratingnum.setText(String.valueOf(movies.getmVoteAverage()));
        description_view.setText(movies.getmOverview());
        Picasso.with(this).load(movies.getmPosterPath()).placeholder(R.drawable.no_image).error(R.drawable.placeholderimage).into(posterView);
    }
}
