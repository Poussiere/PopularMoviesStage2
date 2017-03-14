package com.example.poussiere.popularmoviesstage1.utilities;

import java.net.URL;

/**
 * Created by poussiere on 12/03/17.
 */

public class TrailerObject {

    private String name;
    private URL url;

    public TrailerObject (String n, URL u)
    {
        name = n;
        url = u;

    }


    public String getTrailerName ()
    {return name;}

    public URL getTrailerUrl()
    {
        return url;

    }

}
