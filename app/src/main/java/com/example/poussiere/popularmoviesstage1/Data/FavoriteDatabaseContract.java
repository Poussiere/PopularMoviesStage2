package com.example.poussiere.popularmoviesstage1.Data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by poussiere on 10/03/17.
 */

public interface FavoriteDatabaseContract {

    @DataType(INTEGER) @PrimaryKey
    @AutoIncrement
    public static final String _ID = "_id";

    @DataType(TEXT) @NotNull
    public static final String TMDB_ID = "tmdb_id";  // id of the movie in the movie database. Different from the id in the local database

    @DataType(TEXT) @NotNull
    public static final String ORIGINAL_TITLE = "original_title";

    @DataType(TEXT) @NotNull
    public static final String POSTER_FULL_URL = "poster_full_url";  // id of the movie in the movie database. Different from the id in the local database

    @DataType(TEXT) @NotNull
    public static final String RELEASE_DATE = "release_date";

    @DataType(TEXT) @NotNull
    public static final String OVERVIEW="overview";

    @DataType(REAL) @NotNull
    public static final String NOTE_AVERAGE="note_average";

}
