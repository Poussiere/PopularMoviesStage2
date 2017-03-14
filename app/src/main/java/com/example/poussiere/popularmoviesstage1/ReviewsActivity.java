package com.example.poussiere.popularmoviesstage1;

import android.graphics.Color;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.poussiere.popularmoviesstage1.utilities.MoviesDbJsonUtils;
import com.example.poussiere.popularmoviesstage1.utilities.NetworkUtils;
import com.example.poussiere.popularmoviesstage1.utilities.ReviewObject;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

import static com.example.poussiere.popularmoviesstage1.MainActivity.MOVIE_ID;

public class ReviewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final int REVIEWS_LOADER_ID = 78;
    private int movieId;
    private ArrayList<ReviewObject> rList = new ArrayList<ReviewObject>();
    private RecyclerView reviewsRecyclerView;
  private  MoviesReviewsAdapter moviesReviewsAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_reviews);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.reviews_activity_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);



        reviewsRecyclerView=(RecyclerView)findViewById(R.id.reviews_recycler_view);

        reviewsRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReviewsActivity.this);
        reviewsRecyclerView.setLayoutManager(linearLayoutManager);
        moviesReviewsAdapter=new MoviesReviewsAdapter();
        reviewsRecyclerView.setAdapter(moviesReviewsAdapter);


         movieId =  getIntent().getIntExtra(MOVIE_ID, 0);
        getSupportLoaderManager().initLoader(REVIEWS_LOADER_ID, null, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>( this) {


            String jsonResult;

            @Override
            protected void onStartLoading() {


                if (jsonResult != null) {
                    deliverResult(jsonResult);
                } else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {

                URL reviewsListRequest = null;

                String jsonMovieResponse = null;


                reviewsListRequest= NetworkUtils.buildReviewsRequestUrl(movieId);

                try {
                    jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(reviewsListRequest);


                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }


                return jsonMovieResponse;
            }


            @Override
            public void deliverResult(String jsonMovieResponse) {
                jsonResult = jsonMovieResponse;
                super.deliverResult(jsonResult);

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String jsonString) {



        if (jsonString != null) {

            try {
                rList= MoviesDbJsonUtils.getReviewsFullUrl(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            moviesReviewsAdapter.setReviewsList(rList);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    //Toolbar back button listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
