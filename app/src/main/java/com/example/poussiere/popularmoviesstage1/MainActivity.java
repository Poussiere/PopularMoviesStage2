package com.example.poussiere.popularmoviesstage1;


import android.content.Context;
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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.poussiere.popularmoviesstage1.Data.FavoriteContentProvider;
import com.example.poussiere.popularmoviesstage1.Utilities.MoviesDbJsonUtils;
import com.example.poussiere.popularmoviesstage1.Utilities.NetworkUtils;
import org.json.JSONException;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MoviesPostersAdapter.MoviesPostersAdapterOnClickHandler, LoaderManager.LoaderCallbacks<String> {

    private static final int ASYNC_LOADER_ID = 42;
    private static final int CURSOR_LOADER_ID = 63;

    private final static int SORT_BY_POPULARITY = 0;
    private final static int SORT_BY_TOP_RATED = 1;
    private final static int SORT_BY_FAVORITES = 2;


    public final static String INDEX = "index";
    public final static String JSON_STRING="json";

    private int sortChoice = SORT_BY_POPULARITY ;// By default movie posters are sort by popularity
    private int tempSortChoice=SORT_BY_POPULARITY ; // A int to remember what was the sort choice before item in spinner was clicked if user
    //clicks on favorits and when database is empty
    
    private RecyclerView postersRecyclerView;
    private MoviesPostersAdapter moviesPostersAdapter;

    private Toolbar toolbar = null;

    //array for the spinner (choice between popular vs top rated
    private String [] sortBy = null;
    private Spinner spinner;
    private String jsonStringResult;

    // Key for intent extras that will be passed to detail activity
    public static final String MOVIE_ID="movie_id";
    public static final String ORIGINAL_TITLE="original_title";
    public static final String POSTER_FULL_URL="poster_full_url";
    public static final String RELEASE_DATE="release_date";
    public static final String OVERVIEW="overview";
    public static final String NOTE_AVERAGE="note_average";

    private int movieId;
    private String originalTitle;
    private String posterFullUrl;
    private String releaseDate;
    private String  overview;
    private float noteAverage;
    private Cursor favoritesCursor; // Cursor who loads the results of the favorit movies database request
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner)findViewById(R.id.spinner);

        //Configure toolbar and spinner that let the user choose between top rated et popular
        sortBy = getResources().getStringArray(R.array.sort_by);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    

        // Make title white
       toolbar.setTitleTextColor(Color.WHITE);

        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.sort_by, R.layout.spinner_items);



        spinner.setAdapter(spinnerAdapter);


        // Depends on what item is clicked in the spinner, a cursorLoader is initialised for favorites or an asyncLodaer is initialised for the 2 other choices
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortChoice=position;

                if (sortChoice==SORT_BY_FAVORITES)
                {
                    loadFavoritesMoviesData();
                }

                else if (sortChoice==SORT_BY_POPULARITY) {
                    tempSortChoice=SORT_BY_POPULARITY;
                    loadMoviesData();
                }

                else if (sortChoice==SORT_BY_TOP_RATED)
                {tempSortChoice=SORT_BY_TOP_RATED;
                    loadMoviesData();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Configure recycler view in GrilLayout
        postersRecyclerView=(RecyclerView)findViewById(R.id.posters_recyclerview);
        postersRecyclerView.setHasFixedSize(true);
        GridLayoutManager  gridLayoutManager = new GridLayoutManager(MainActivity.this, calculateNoOfColumns(getBaseContext())); // 2 = number of items on each row
        postersRecyclerView.setLayoutManager(gridLayoutManager);
        moviesPostersAdapter=new MoviesPostersAdapter(this);
        postersRecyclerView.setAdapter(moviesPostersAdapter);

        // By default, the Asyncloader with movies sorted by popularity is initialized
        getSupportLoaderManager().initLoader(ASYNC_LOADER_ID, null, this);


    }

    //Method that initialize or resume the Asyncloader that loads datas for popular and top rated requests
    private void loadMoviesData()
    {


        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> movieQuerryLoader = loaderManager.getLoader(ASYNC_LOADER_ID);
        if (movieQuerryLoader == null) {
            loaderManager.initLoader(ASYNC_LOADER_ID, null, this);
        } else {
            loaderManager.restartLoader(ASYNC_LOADER_ID, null, this);
        }

    }

    //Method that initializes or resume the Cursor loader that loads datas for favorits requests
    private void loadFavoritesMoviesData()
    {

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> FavoritMovieQuerryLoader = loaderManager.getLoader(CURSOR_LOADER_ID);
        if (FavoritMovieQuerryLoader == null) {
            loaderManager.initLoader(CURSOR_LOADER_ID, null, cursorLoaderCallback);
        } else {
            loaderManager.restartLoader(CURSOR_LOADER_ID, null, cursorLoaderCallback);
        }
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

                    URL movieListRequest = null;

                    String jsonMovieResponse = null;


                    if (sortChoice == SORT_BY_POPULARITY) {
                        movieListRequest = NetworkUtils.buildUrlSortByPopularity();
                    } else {
                        movieListRequest = NetworkUtils.buildUrlSortByTopRated();
                    }

                    try {
                        jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieListRequest);


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


            jsonStringResult = jsonString;

            String[] postersFullUrl = null;

            if (jsonString != null) {

                try {
                    postersFullUrl = MoviesDbJsonUtils.getPostersFullUrl(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                moviesPostersAdapter.setMoviesPostersUrl(postersFullUrl);
            }
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
      
        }


    private LoaderManager.LoaderCallbacks<Cursor> cursorLoaderCallback
            = new LoaderManager.LoaderCallbacks<Cursor>() {


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            switch (id) {

                case CURSOR_LOADER_ID:

                    Uri getAllFavorites = FavoriteContentProvider.FavoriteMovies.CONTENT_URI;

                    return new CursorLoader(getApplicationContext(),
                            getAllFavorites,
                            null,
                            null,
                            null,
                            null);

                default:
                    throw new RuntimeException("Loader Not Implemented: " + id);
            }
        }
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            String [] postersFullUrl = new String[data.getCount()];

            favoritesCursor=data; // We'll want to retrieve this cursor when an item will be clicked

            if (data.moveToFirst())
            {
                for (int i=0; i<data.getCount(); i++)
                {
                    postersFullUrl[i]=data.getString(3);
                    data.moveToNext();
                }


                moviesPostersAdapter.setMoviesPostersUrl(postersFullUrl);
            }

            else {
                sortChoice = tempSortChoice; // if the favorite data base is empty, the sortchoice comes back to its previous state (before item was clicked).
                Toast.makeText(getApplicationContext(), R.string.empty_bdd_alert, Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };


    //This method is trigered when an item is clicked
    @Override
    public void whatMovieIndex(int index) {
        Intent i = new Intent (MainActivity.this, DetailActivity.class);

        
        
        if(sortChoice==SORT_BY_FAVORITES)
        {
            favoritesCursor.moveToPosition(index);
            movieId=favoritesCursor.getInt(1);
            originalTitle=favoritesCursor.getString(2);
            posterFullUrl=favoritesCursor.getString(3);
            releaseDate=favoritesCursor.getString(4);
            overview=favoritesCursor.getString(5);
            noteAverage=favoritesCursor.getFloat(6);


        }

        else
        {
            try {
                movieId = MoviesDbJsonUtils.getMovieIdFromJson(jsonStringResult, index);
                originalTitle = MoviesDbJsonUtils.getOriginalTitleFromJson(jsonStringResult, index);
                posterFullUrl = MoviesDbJsonUtils.getBigPosterFullUrl(jsonStringResult, index);
                releaseDate = MoviesDbJsonUtils.getReleaseDate(jsonStringResult, index);
                overview = MoviesDbJsonUtils.getOverview(jsonStringResult, index);
                noteAverage = (float) MoviesDbJsonUtils.getNoteAverage(jsonStringResult, index);
                noteAverage = noteAverage / 2; //we want only 5 stars max but by default the rate is /10

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        i.putExtra(MOVIE_ID, movieId);
        i.putExtra(ORIGINAL_TITLE, originalTitle);
        i.putExtra(POSTER_FULL_URL, posterFullUrl);
        i.putExtra(RELEASE_DATE, releaseDate);
        i.putExtra(OVERVIEW, overview);
        i.putExtra(NOTE_AVERAGE, noteAverage);
        
        startActivity(i);


    }


    // Calculate the number of columns of the gridlayout

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
       
         int noOfColumns;
        // If we are on tablet, the posters we'll appeaR bigger
        if (dpWidth>=600)
        { noOfColumns = (int) (dpWidth / 375);}
      
         else 
         {noOfColumns = (int) (dpWidth / 180);}
        return noOfColumns;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_menu, menu);
        return true;
    }




}
