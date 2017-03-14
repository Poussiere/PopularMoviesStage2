package com.example.poussiere.popularmoviesstage1;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.poussiere.popularmoviesstage1.utilities.TrailerObject;
import java.util.ArrayList;

/**
 * Created by poussiere on 12/03/17.
 */

public class MoviesTrailersAdapter extends RecyclerView.Adapter< MoviesTrailersAdapter.MoviesTrailersViewHolder> {

    private ArrayList<TrailerObject> trailersList ;
    private final MoviesTrailersAdapter.MoviesTrailersAdapterOnClickHandler mClickHandler;
    private String trailerName;




    public interface MoviesTrailersAdapterOnClickHandler {

        void whatTrailerIndex(int index);
    }


    //Constructor
    public MoviesTrailersAdapter (MoviesTrailersAdapter.MoviesTrailersAdapterOnClickHandler mp)
    {
        mClickHandler=mp;
    }


    @Override
    public MoviesTrailersAdapter.MoviesTrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailers, null, false);
        MoviesTrailersAdapter.MoviesTrailersViewHolder viewHolder = new MoviesTrailersAdapter.MoviesTrailersViewHolder(layoutView);
        return viewHolder;

    }



    @Override
    public void onBindViewHolder(MoviesTrailersAdapter.MoviesTrailersViewHolder holder, int position) {

        TrailerObject trailerObject = trailersList.get(position);

        trailerName = trailerObject.getTrailerName();

        holder.trailerNameTextView.setText(trailerName);



    }


    // Return the size of the posters links array
    @Override
    public int getItemCount() {
        if (trailersList==null) return 0;
        return trailersList.size();
    }


    //Method to pass the array with the links of movies posters images paths to the adapter when user switch between sort by popularity and sort by top rated
    public void setTrailersList (ArrayList<TrailerObject>list)

    {
        trailersList=list;
        notifyDataSetChanged();
    }





    public class MoviesTrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {


        public TextView trailerNameTextView;
        public ImageButton playButton;

        public MoviesTrailersViewHolder(View itemView) {
            super(itemView);
            trailerNameTextView = (TextView)itemView.findViewById(R.id.tv_trailer);
            playButton=(ImageButton) itemView.findViewById(R.id.play_image);
            playButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int adapterPosition=getAdapterPosition();
            mClickHandler.whatTrailerIndex(adapterPosition);


        }
    }

}
