package com.example.poussiere.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.squareup.picasso.Picasso;

public class MoviesPostersAdapter extends RecyclerView.Adapter< MoviesPostersAdapter.MoviesPostersViewHolder> {

    private String [] posterList;
    private final MoviesPostersAdapterOnClickHandler mClickHandler;



    public interface MoviesPostersAdapterOnClickHandler {

        void whatMovieIndex(int index);
    }


    //Constructor
    public MoviesPostersAdapter (MoviesPostersAdapterOnClickHandler mp)
    {
    mClickHandler=mp;
    }


    @Override
    public MoviesPostersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.posters, null, false);
        MoviesPostersViewHolder viewHolder = new MoviesPostersViewHolder(layoutView);
        return viewHolder;

    }



    @Override
    public void onBindViewHolder(MoviesPostersViewHolder holder, int position) {


        String posterUrl = posterList [position];
        Context context =holder.posterView.getContext();
       Picasso.with(context).load(posterUrl).into(holder.posterView);


    }


    // Return the size of the posters links array
    @Override
    public int getItemCount() {
       if (posterList==null) return 0;
        return posterList.length;
    }


    //Method to pass the array with the links of movies posters images paths to the adapter when user switch between sort by popularity and sort by top rated
    public void setMoviesPostersUrl (String[]  postersUrl)
    {
        posterList=postersUrl;
        notifyDataSetChanged();
    }





    public class MoviesPostersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {


        public ImageView posterView;
        public ProgressBar progressBar;

        public MoviesPostersViewHolder(View itemView) {
            super(itemView);
            posterView = (ImageView)itemView.findViewById(R.id.poster_view);
            progressBar=(ProgressBar)itemView.findViewById(R.id.progress_bar_cardview);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int adapterPosition=getAdapterPosition();
            mClickHandler.whatMovieIndex(adapterPosition);

        }
    }






}
