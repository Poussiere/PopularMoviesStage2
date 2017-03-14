package com.example.poussiere.popularmoviesstage1.utilities;

/**
 * Created by poussiere on 12/03/17.
 */

public class ReviewObject {

    String author;
    String content;

    public ReviewObject (String a, String c)
    {
        author=a;
        content=c;
    }

    public String getAuthor()
    {return author;}

    public String getContent()
    {return content;}

}
