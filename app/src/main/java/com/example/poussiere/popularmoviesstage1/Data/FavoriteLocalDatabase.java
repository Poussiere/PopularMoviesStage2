package com.example.poussiere.popularmoviesstage1.Data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by poussiere on 10/03/17.
 */



    @Database(version = FavoriteLocalDatabase.VERSION)

    public final class FavoriteLocalDatabase {

    public static final int VERSION = 1;

    @Table(FavoriteDatabaseContract.class)
    public static final String FAVORITES_TABLE = "favorites_table";
}
