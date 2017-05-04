package com.example.poussiere.popularmoviesstage1.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.poussiere.popularmoviesstage1.MainActivity;
import com.example.poussiere.popularmoviesstage1.R;

/**
 * Created by poussiere on 27/03/17.
 */

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate (Context context, AppWidgetManager appWidgetManager, int [] appWidgetIds)
    {

       Intent i = new Intent (context, WidgetIntentService.class);
        context.startService(i);
    }

}
