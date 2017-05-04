package com.example.poussiere.popularmoviesstage1.Widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.poussiere.popularmoviesstage1.MainActivity;
import com.example.poussiere.popularmoviesstage1.R;
import com.example.poussiere.popularmoviesstage1.Utilities.MoviesDbJsonUtils;
import com.example.poussiere.popularmoviesstage1.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by poussiere on 29/03/17.
 */

public class WidgetIntentService extends IntentService {




    public WidgetIntentService() {

        super ("WidgetIntentService");

        Log.i ("widget", "Le service a été lancé");


    }

    @Override
    protected void onHandleIntent(Intent intent) {



        Log.i("Widget", "HandleIntent lancé");
        // Retrieve all of the widget ids to update each one
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                WidgetProvider.class));


        // Make the request of the popular movies list then retrieve the id of the first movie in this list
        URL movieListRequest = null;

        String jsonMovieResponse = null;

        String mostPopularMoviePosterUrl = null;

        movieListRequest = NetworkUtils.buildUrlSortByPopularity();

        try {
            jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieListRequest);


        } catch (Exception e) {
            e.printStackTrace();

        }

        try {
            mostPopularMoviePosterUrl= MoviesDbJsonUtils.getBigPosterFullUrl(jsonMovieResponse, 0); // 0 est l'index du premier film qui est censé être le plus populaire du moment
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("widget", "url du poster récupérée");

        //update each widget that has been set by the user
        for(int appWidgetId : appWidgetIds)
        {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_layout);

        Bitmap btm=null;

         //   Picasso.with(this).load(mostPopularMoviePosterUrl).placeholder(R.drawable.test_draw).into(views, R.id.image_widget, appWidgetIds);
            try {
                btm = Picasso.with(this).load(mostPopularMoviePosterUrl).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            views.setImageViewBitmap(R.id.image_widget, btm);
            Log.i("widget", "image chargee par picassa");
            Intent i=new Intent (this, MainActivity.class);
            PendingIntent pd = PendingIntent.getActivity(this, 0, i, 0);
            views.setOnClickPendingIntent(R.id.widget, pd);
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }



    }
}
