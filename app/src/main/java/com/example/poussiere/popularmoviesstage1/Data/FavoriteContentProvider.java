package com.example.poussiere.popularmoviesstage1.Data;

import android.graphics.Path;
import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import static com.example.poussiere.popularmoviesstage1.Data.FavoriteContentProvider.Path.MOVIES;


/**
 * Created by poussiere on 10/03/17.
 */

@ContentProvider(authority = FavoriteContentProvider.AUTHORITY, database = FavoriteLocalDatabase.class)

public final class FavoriteContentProvider {



    public static final String AUTHORITY = "com.example.poussiere.popularmoviesstage1";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static Uri buildUri(String... paths) {

        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();

        for (String Path : paths) {
            builder.appendPath(Path);

        }
        return builder.build();
    }

    interface Path{
        String MOVIES = FavoriteLocalDatabase.FAVORITES_TABLE;

    }

    @TableEndpoint(table = FavoriteLocalDatabase.FAVORITES_TABLE)
    public static class FavoriteMovies {

        @ContentUri(
                path = Path.MOVIES,
                type = "vnd.android.cursor.dir/favorites_list",
                defaultSort = FavoriteDatabaseContract._ID + " ASC")

        public static final Uri CONTENT_URI = buildUri(Path.MOVIES);


        @InexactContentUri(
                path = Path.MOVIES + "/#",
                name = "tmdb_id",
                type = "vnd.android.cursor.item/favorite_movie",
                whereColumn = FavoriteDatabaseContract.TMDB_ID,
                pathSegment = 1)

        public static Uri favoriteWithId(int id) {
            return  buildUri(Path.MOVIES, String.valueOf(id));
        }
    }
}