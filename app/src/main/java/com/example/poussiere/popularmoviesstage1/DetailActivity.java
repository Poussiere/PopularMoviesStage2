package com.example.poussiere.popularmoviesstage1;

import android.content.ActivityNotFoundException;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.poussiere.popularmoviesstage1.Data.FavoriteContentProvider;
import com.example.poussiere.popularmoviesstage1.Data.FavoriteDatabaseContract;
import com.example.poussiere.popularmoviesstage1.utilities.MoviesDbJsonUtils;
import com.example.poussiere.popularmoviesstage1.utilities.NetworkUtils;
import com.example.poussiere.popularmoviesstage1.utilities.TrailerObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

import static com.example.poussiere.popularmoviesstage1.Data.FavoriteContentProvider.FavoriteMovies.CONTENT_URI;
import static com.example.poussiere.popularmoviesstage1.MainActivity.MOVIE_ID;

public class DetailActivity extends AppCompatActivity implements MoviesTrailersAdapter.MoviesTrailersAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<String> {


    private ImageView poster;
    private TextView tvTitle;
    private TextView tvReleaseDate;
    private TextView tvOverview;
    private TextView tvRate;
    private RatingBar rtRate;
    private ImageButton favoriteStar;
    private RecyclerView trailersRecyclerView;
    private int movieId;
    private String originalTitle;
    private String posterFullUrl;
    private String releaseDate;
    private String overview;
    private float noteAverage;
    private Toolbar toolbar;
    private Bundle bundle;

    ArrayList<TrailerObject>mList = new ArrayList<TrailerObject>();

    private MoviesTrailersAdapter trailersAdapter;

    //we'll check in the onCreate wether the movie is already in the favorite database or not
    private static final int CURSOR_LOADER_CHECK_FAVORITE_ID = 3;
    private static final int TRAILER_LOADER_ID = 4;
    private boolean isFavorite;


    public static final String[] PROJECTION = {
            FavoriteDatabaseContract.TMDB_ID,
            FavoriteDatabaseContract.ORIGINAL_TITLE,
            FavoriteDatabaseContract.OVERVIEW

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Toolbar
       toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
       setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.detail_activity_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);


        poster = (ImageView) findViewById(R.id.iv_detailPoster);
        tvTitle=(TextView) findViewById(R.id.tv_title);
        tvReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        tvOverview = (TextView) findViewById(R.id.tv_overview);
        tvRate=(TextView)findViewById((R.id.tv_note));
        rtRate = (RatingBar) findViewById(R.id.rt_note_average);
        favoriteStar = (ImageButton) findViewById(R.id.favorite);
        trailersRecyclerView = (RecyclerView) findViewById(R.id.trailers_recycler_view);


        trailersRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailActivity.this);
        trailersRecyclerView.setLayoutManager(linearLayoutManager);
        trailersAdapter=new MoviesTrailersAdapter(this);
        trailersRecyclerView.setAdapter(trailersAdapter);

        Intent i = getIntent();


        movieId=i.getIntExtra(MOVIE_ID, 0);
        originalTitle = i.getStringExtra(MainActivity.ORIGINAL_TITLE);
        posterFullUrl = i.getStringExtra(MainActivity.POSTER_FULL_URL);
        releaseDate = i.getStringExtra(MainActivity.RELEASE_DATE);
        overview = i.getStringExtra(MainActivity.OVERVIEW);
        noteAverage = i.getFloatExtra(MainActivity.NOTE_AVERAGE, 0);


        Picasso.with(this).load(posterFullUrl).into(poster);
        tvTitle.setText(originalTitle);
        tvReleaseDate.setText(releaseDate);
        tvOverview.setText(overview);
        tvRate.setText(noteAverage+"/5");
        rtRate.setRating(noteAverage);

        bundle=new Bundle();
        bundle.putInt(FavoriteDatabaseContract.TMDB_ID, movieId);

        //The cursor loader is initialized with the id of the selected movie to check if the movie is in the favorite database
        //It will check wether the movie is already in favorites or not and update the image button of the star
        getSupportLoaderManager().initLoader(CURSOR_LOADER_CHECK_FAVORITE_ID, bundle, cursorLoaderCallback);
        getSupportLoaderManager().initLoader(TRAILER_LOADER_ID, null, this);


    }
@Override
protected void onResume()
{
    super.onResume();



}

    private void checkIfMovieIsFavorite()
    {

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> movieQuerryLoader = loaderManager.getLoader(CURSOR_LOADER_CHECK_FAVORITE_ID);
        if (movieQuerryLoader == null) {
            loaderManager.initLoader(CURSOR_LOADER_CHECK_FAVORITE_ID, bundle, cursorLoaderCallback);
        } else {
            loaderManager.restartLoader(CURSOR_LOADER_CHECK_FAVORITE_ID, bundle, cursorLoaderCallback);
        }

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

    // When star is clicked, data are inserted in the favorites database
    public void onStarClick(View v)
    {
        // If the movie is not already a favorite, it will be added to the database and the star button will be updated
        if (!isFavorite) {

            ContentValues cv = new ContentValues();
            cv.put(FavoriteDatabaseContract.TMDB_ID, movieId);
            cv.put(FavoriteDatabaseContract.ORIGINAL_TITLE, originalTitle);
            cv.put(FavoriteDatabaseContract.POSTER_FULL_URL,posterFullUrl);
            cv.put(FavoriteDatabaseContract.RELEASE_DATE, releaseDate);
            cv.put(FavoriteDatabaseContract.OVERVIEW, overview);
            cv.put(FavoriteDatabaseContract.NOTE_AVERAGE, noteAverage);
            getContentResolver().insert(CONTENT_URI, cv );

            isFavorite=true;
            changeStarStatut(isFavorite);


        }

        //if the movie is already a favorite, we'll delete it from the database and the star button will be updated
        else
        {

        Uri movieToDelete=FavoriteContentProvider.FavoriteMovies.favoriteWithId(movieId);
        getContentResolver().delete(movieToDelete, null, null);

            isFavorite=false;
            changeStarStatut(isFavorite);

        }

    }


    private LoaderManager.LoaderCallbacks<Cursor> cursorLoaderCallback
            = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {


            switch (loaderId) {

                case CURSOR_LOADER_CHECK_FAVORITE_ID:

                    int TmdbMovieId = bundle.getInt(FavoriteDatabaseContract.TMDB_ID);
                    Uri CheckIfExistUri = FavoriteContentProvider.FavoriteMovies.favoriteWithId(TmdbMovieId);

                    return new CursorLoader(getApplicationContext(),
                            CheckIfExistUri,
                            null,
                            null,
                            null,
                            null);

                default:
                    throw new RuntimeException("Loader Not Implemented: " + loaderId);
            }
        }


        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            //if the cursor size = 0, that means that the movie is not in the favorite base
            int cursorSize = data.getCount();
            if (cursorSize != 0) isFavorite = true;
            else isFavorite = false;
            changeStarStatut(isFavorite); //Method that changes the image of the star button

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };
    //Change star button image when clicked or wether a movie is a favorite or not
    public void changeStarStatut(boolean isFavorite)
    {
        if (isFavorite) favoriteStar.setImageResource(android.R.drawable.btn_star_big_on);
        else favoriteStar.setImageResource(android.R.drawable.btn_star_big_off);
    }


    //we use an asyncLoader to load datas about trailers via an http request

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

                URL trailersListRequest = null;

                String jsonMovieResponse = null;


                trailersListRequest=NetworkUtils.buildTrailersRequestUrl(movieId);

                try {
                    jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(trailersListRequest);


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
                mList= MoviesDbJsonUtils.getTrailersFullUrl(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            trailersAdapter.setTrailersList(mList);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void whatTrailerIndex(int index) {
        TrailerObject trailerObject = mList.get(index);
        URL youtubeLink = trailerObject.getTrailerUrl();


            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(youtubeLink.toString()));

            startActivity(intent);
        }

    // When the note average or the rating bar is clicked, a new activity is launched that show the reviews
    public void onClickSeeReviews (View v)
    {
        Intent i = new Intent (DetailActivity.this, ReviewsActivity.class);
        i.putExtra(MOVIE_ID, movieId );
        startActivity(i);

    }

    }

