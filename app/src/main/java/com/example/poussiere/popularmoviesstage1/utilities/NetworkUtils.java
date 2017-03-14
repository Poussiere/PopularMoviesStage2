package com.example.poussiere.popularmoviesstage1.utilities;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;



public class NetworkUtils {

    //API key is private. Please ask for your own API key to TheMovieDB
    private static final String apiKey="ac25d2d29180abf9b84a87eba3ad5316";

    private static final String BASE_URL_REVIEWS_AND_TRAILER_REQUESTS="https://api.themoviedb.org/3/movie";
    private static final String BASE_URL_POPULAR_REQUEST="https://api.themoviedb.org/3/movie/popular";
    private static final String BASE_URL_TOP_RATED_REQUEST="https://api.themoviedb.org/3/movie/top_rated";
    private static final String BASE_URL_POSTER_REQUEST="http://image.tmdb.org/t/p/";
    private static final String SMALL_POSTER_SIZE="/w342/";
    private static final String BIG_POSTER_SIZE="/w500/";
    private static final String VIDEOS_PATH="videos";
    private static final String REVIEWS_PATH="reviews";
    private static final String BASE_URL_YOUTUBE_VIDEO="https://www.youtube.com/watch";

    private static final String YOUTUVE_VIDEO_QUERY_PARAM="v";


    //Check the API documentation to find these params
    final static String KEY_PARAM="api_key";


    //Method that returns the url of a youtube trailer request
    public static URL buildYoutubeRequestUrl(String youtubeMovieId)
    {
        Uri uri=Uri.parse(BASE_URL_YOUTUBE_VIDEO).buildUpon()
                .appendQueryParameter(YOUTUVE_VIDEO_QUERY_PARAM, youtubeMovieId)
                .build();

        URL url = null;
        try {
            url=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }


    //Method that returns the url of the request to obtain the movies list sort by popularity
    public static URL buildUrlSortByPopularity()
    {
    Uri uri=Uri.parse(BASE_URL_POPULAR_REQUEST).buildUpon()
            .appendQueryParameter(KEY_PARAM, apiKey).build();

        URL url = null;
        try {
            url=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }



    //Method that returns the url of the request to obtain the movies list sort by top rated
    public static URL buildUrlSortByTopRated()
    {
    Uri uri=Uri.parse(BASE_URL_TOP_RATED_REQUEST).buildUpon()
            .appendQueryParameter(KEY_PARAM, apiKey).build();

        URL url = null;
        try {
            url=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;


    }

    //Method that returns the url of the poster to display in GridLayout
    //We return a String because the url will be used with Picassa
    public static String buidUrlSmallPoster(String posterPath)
    {

        String stringUrl=BASE_URL_POSTER_REQUEST+SMALL_POSTER_SIZE+posterPath;

        return stringUrl;
    }

    //Method that returns the url of the poster to display in DetailActivity
    //We return a String because the url will be used with Picassa
    public static String buidUrlBigPoster(String posterPath)
    {

        String stringUrl=BASE_URL_POSTER_REQUEST+BIG_POSTER_SIZE+posterPath;
        return stringUrl;
    }



    //Method that returns a url that request for trailers with a movie_id

    public static URL buildTrailersRequestUrl (int movieId)
    {
        Uri uri=Uri.parse(BASE_URL_REVIEWS_AND_TRAILER_REQUESTS).buildUpon()
                .appendPath(""+movieId)
                .appendPath(VIDEOS_PATH)
                .appendQueryParameter(KEY_PARAM, apiKey)
                .build();


        URL url = null;
        try {
            url=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }


    //Method that returns a url that request for reviews with a movie_id

    public static URL buildReviewsRequestUrl (int movieId)
    {
        Uri uri=Uri.parse(BASE_URL_REVIEWS_AND_TRAILER_REQUESTS).buildUpon()
                .appendPath(""+movieId)
                .appendPath(REVIEWS_PATH)
                .appendQueryParameter(KEY_PARAM, apiKey)
                .build();


        URL url = null;
        try {
            url=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }

    //Method that return the result of the http request

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}


