package com.example.poussiere.popularmoviesstage1;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.poussiere.popularmoviesstage1.utilities.ReviewObject;
import com.example.poussiere.popularmoviesstage1.utilities.TrailerObject;

import java.util.ArrayList;

/**
 * Created by poussiere on 12/03/17.
 */

public class MoviesReviewsAdapter extends RecyclerView.Adapter< MoviesReviewsAdapter.MoviesReviewsViewHolder>{

    private ArrayList<ReviewObject> reviewsList ;

    private String reviewAuthor;
    private String reviewContent;




    //Constructor
    public MoviesReviewsAdapter ()
    {

    }


    @Override
    public MoviesReviewsAdapter.MoviesReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews, null, false);
        MoviesReviewsAdapter.MoviesReviewsViewHolder viewHolder = new MoviesReviewsAdapter.MoviesReviewsViewHolder(layoutView);
        return viewHolder;

    }



    @Override
    public void onBindViewHolder(MoviesReviewsAdapter.MoviesReviewsViewHolder holder, int position) {

        ReviewObject reviewObject = reviewsList.get(position);

        reviewAuthor = reviewObject.getAuthor();
        reviewContent = reviewObject.getContent();

        Log.i("coucou", reviewAuthor);
        Log.i("coucou", reviewContent);
        holder.author.setText(reviewAuthor);
        holder.content.setText(reviewContent);



    }


    // Return the size of the posters links array
    @Override
    public int getItemCount() {
        if (reviewsList==null) return 0;
        return reviewsList.size();
    }


    //Method to pass the array with the links of movies posters images paths to the adapter when user switch between sort by popularity and sort by top rated
    public void setReviewsList (ArrayList<ReviewObject>list)

    {
        reviewsList=list;
        notifyDataSetChanged();
    }





    public class MoviesReviewsViewHolder extends RecyclerView.ViewHolder
    {


        public TextView author;
        public TextView content;

        public MoviesReviewsViewHolder(View itemView) {
            super(itemView);
            author= (TextView)itemView.findViewById(R.id.tv_author);
            content=(TextView)itemView.findViewById(R.id.tv_review_content);


        }


    }

}


