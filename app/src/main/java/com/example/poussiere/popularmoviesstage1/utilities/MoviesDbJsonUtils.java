package com.example.poussiere.popularmoviesstage1.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;


public class MoviesDbJsonUtils {



   // Keys for the JSonFile returned by TMDB with populare and top rated request

    final static String TMDB_MOVIES_ARRAY="results";
    final static String TMDB_POSTER_PATH="poster_path";
    final static String TMDB_ORIGINAL_TITLE="original_title";
    final static String TMDB_OVERVIEW="overview";
    final static String TMDB_VOTE_AVERAGE="vote_average";
    final static String TMDB_RELEASE_DATE="release_date";
    final static String TMDB_MOVIE_ID="id";

    //Keys for the JSonfile returned for videos request
    final static String TMDB_VIDEOS_ARRAY="results";
    final static String TMDB_VIDEO_NAME="name";
    final static String TMDB_VIDEO_SITE="site";
    final static String TMDB_VIDEO_TYPE="type";
    final static String TMDB_YOUTUBE_VIDEO_KEY="key";
    final static String TMDB_DESIRED_SITE="YouTube";
    final static String TMDB_DESIRED_TYPE="Trailer";

    //Keys for the JSonFile returned for reviews request
    final static String TMDB_REVIEWS_ARRAY="results";
    final static String TMDB_REVIEW_AUTHOR = "author";
    final static String TMDB_REVIEW_CONTENT = "content";


    //number of results for each page of results for populare and top rated requests
    final static int RESULTS_NUMBER=20;


    //Key to handle error messages from the movie db Json
    final static String TMDB_ERROR="status_code";




    //Get a tab with all the poster paths from the JSONfile result to display posters in a GridLayout
    public static String [] getPostersFullUrl(String jsonString) throws JSONException

    {


        JSONObject jsonObject = new JSONObject(jsonString);

        //handle possible error messages
        if (hasErrorMessage(jsonObject)) return null;
        JSONArray jsonMoviesArray = jsonObject.getJSONArray(TMDB_MOVIES_ARRAY);


        JSONObject tabRow;
        String [] postersUrl=new String [RESULTS_NUMBER];
        for (int i=0; i<postersUrl.length; i++)
        {
            //We get each row of the JsonArray
            tabRow=jsonMoviesArray.getJSONObject(i);

            //We convert each row of the JsonArray to String and pass it as argument to the method that construtcs the full url of posters
            postersUrl[i]=NetworkUtils.buidUrlSmallPoster(tabRow.getString(TMDB_POSTER_PATH));

        }


        return postersUrl;

        //


    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Methods for the detail activity


    //Get the ID of the selected movie
    //The id is necessary to request video and reviews

    public static int getMovieIdFromJson (String jsonString, int index) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(jsonString);

        //handle possible error messages
        if (hasErrorMessage(jsonObject)) return 0;
        JSONArray jsonMoviesArray = jsonObject.getJSONArray(TMDB_MOVIES_ARRAY);
        JSONObject object =jsonMoviesArray.getJSONObject(index);

        int movieId=object.getInt(TMDB_MOVIE_ID);
        return movieId;

    }

    public static String getOriginalTitleFromJson (String jsonString, int index) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(jsonString);

        //handle possible error messages
        if (hasErrorMessage(jsonObject)) return null;
        JSONArray jsonMoviesArray = jsonObject.getJSONArray(TMDB_MOVIES_ARRAY);
        JSONObject object =jsonMoviesArray.getJSONObject(index);
        String originalTitle=object.getString(TMDB_ORIGINAL_TITLE);
        return originalTitle;

    }


    public static String getBigPosterFullUrl (String jsonString, int index) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(jsonString);

        //handle possible error messages
        if (hasErrorMessage(jsonObject)) return null;
        JSONArray jsonMoviesArray = jsonObject.getJSONArray(TMDB_MOVIES_ARRAY);
        JSONObject object =jsonMoviesArray.getJSONObject(index);
        String posterFullUr=NetworkUtils.buidUrlBigPoster(object.getString(TMDB_POSTER_PATH));
        return posterFullUr;


    }


    public static String getOverview (String jsonString, int index) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(jsonString);

        //handle possible error messages
        if (hasErrorMessage(jsonObject)) return null;
        JSONArray jsonMoviesArray = jsonObject.getJSONArray(TMDB_MOVIES_ARRAY);
        JSONObject object =jsonMoviesArray.getJSONObject(index);
        String overview=object.getString(TMDB_OVERVIEW);
        return overview;

    }

    public static double getNoteAverage (String jsonString, int index) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(jsonString);

        //handle possible error messages
        if (hasErrorMessage(jsonObject)) return 0;
        JSONArray jsonMoviesArray = jsonObject.getJSONArray(TMDB_MOVIES_ARRAY);
        JSONObject object =jsonMoviesArray.getJSONObject(index);
        double noteAverage=(double)object.getDouble(TMDB_VOTE_AVERAGE);
        return noteAverage;

    }

    public static String getReleaseDate (String jsonString, int index) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(jsonString);

        //handle possible error messages
        if (hasErrorMessage(jsonObject)) return null;
        JSONArray jsonMoviesArray = jsonObject.getJSONArray(TMDB_MOVIES_ARRAY);
        JSONObject object =jsonMoviesArray.getJSONObject(index);
        String releaseDate=object.getString(TMDB_RELEASE_DATE);
        return releaseDate;

    }

    public static ArrayList<TrailerObject> getTrailersFullUrl(String jsonString) throws JSONException

    {
        ArrayList<TrailerObject> trailersList=new ArrayList<TrailerObject>();

        JSONObject jsonObject = new JSONObject(jsonString);

        //handle possible error messages
        if (hasErrorMessage(jsonObject)) return null;


        JSONArray jsonMoviesArray = jsonObject.getJSONArray(TMDB_VIDEOS_ARRAY);

        JSONObject trailerJsonObject;
        TrailerObject trailerObject;
        String name;
        String youtubeMovieId;
        String type;
        String site;
        URL trailerUrl;

        int length = jsonMoviesArray.length();

        //For each video, we check if it is a trailer and if it is on Youtube site

        for (int i=0; i<length; i++)
        {
        trailerJsonObject=jsonMoviesArray.getJSONObject(i);
            type=trailerJsonObject.getString(TMDB_VIDEO_TYPE);
            site=trailerJsonObject.getString(TMDB_VIDEO_SITE);

            if (type.equals(TMDB_DESIRED_TYPE) && site.equals(TMDB_DESIRED_SITE))
            {
                name=trailerJsonObject.getString(TMDB_VIDEO_NAME);
                youtubeMovieId=trailerJsonObject.getString(TMDB_YOUTUBE_VIDEO_KEY);
                trailerUrl=NetworkUtils.buildYoutubeRequestUrl(youtubeMovieId);
                trailerObject=new TrailerObject(name, trailerUrl);
                trailersList.add(trailerObject);
                Collections.reverse(trailersList);
            }

        }

    return trailersList;

    }

    public static ArrayList<ReviewObject> getReviewsFullUrl(String jsonString) throws JSONException

    {
        ArrayList<ReviewObject> reviewsList=new ArrayList<ReviewObject>();

        JSONObject jsonObject = new JSONObject(jsonString);

        //handle possible error messages
        if (hasErrorMessage(jsonObject)) return null;


        JSONArray jsonReviewsArray = jsonObject.getJSONArray(TMDB_REVIEWS_ARRAY);

        JSONObject reviewsJsonObject;
        ReviewObject reviewObject;

        String reviewAuthor;
        String reviewContent;

        int length = jsonReviewsArray.length();

        //For each video, we check if it is a trailer and if it is on Youtube site

        for (int i=0; i<length; i++)
        {
            reviewsJsonObject=jsonReviewsArray.getJSONObject(i);

                reviewAuthor=reviewsJsonObject.getString(TMDB_REVIEW_AUTHOR);
                reviewContent=reviewsJsonObject.getString(TMDB_REVIEW_CONTENT);
                reviewObject=new ReviewObject(reviewAuthor,reviewContent);
                reviewsList.add(reviewObject);
                Collections.reverse(reviewsList);
        }

        return reviewsList;

    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////
    //Method that allows to handle possible error messages in the Json Files from the movie db
    public static boolean hasErrorMessage(JSONObject jsonObject) throws JSONException
    {

        if (jsonObject.has(TMDB_ERROR))
        {
            int errorCode = jsonObject.getInt(TMDB_ERROR);

            switch (errorCode) {

                case 7:
                    return true;
                //invalid api key
                case 34:
                    return true;
                //requested resource not found

            }

        }
        return false;
    }



}
