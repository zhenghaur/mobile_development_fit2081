package com.example.movielibrary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movielibrary.provider.Movie;
import com.example.movielibrary.provider.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    List<Movie> movieListRecycle = new ArrayList<>();


    public void setMovie(List<Movie> data){
        this.movieListRecycle = data;
    }

    public MyRecyclerViewAdapter(){
    }

    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.title.setText("Title " + movieListRecycle.get(position).getTitle());
        holder.year.setText("Year " + movieListRecycle.get(position).getYear());
        holder.country.setText("Country " + movieListRecycle.get(position).getCountry());
        holder.genre.setText("Genre " + movieListRecycle.get(position).getGenre());
        holder.cost.setText("Cost " + movieListRecycle.get(position).getCost());
        holder.keywords.setText("Keywords " + movieListRecycle.get(position).getKeyword());

        // extra task w6
        int newPosition = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            int year = movieListRecycle.get(newPosition).getYear();
            @Override public void onClick(View v) {
                MainActivity2.mMovieViewModel.deleteMovieYear(year);
                Toast msg = Toast.makeText(v.getContext(), "Movie No." + (newPosition + 1) + " with title: " + movieListRecycle.get(newPosition).getTitle() + " is selected", Toast.LENGTH_SHORT);
//                msg.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return movieListRecycle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView year;
        public TextView country;
        public TextView genre;
        public TextView cost;
        public TextView keywords;

        public ViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.cardTitle);
            year = itemView.findViewById(R.id.cardYear);
            country = itemView.findViewById(R.id.cardCountry);
            genre = itemView.findViewById(R.id.cardGenre);
            cost = itemView.findViewById(R.id.cardCost);
            keywords = itemView.findViewById(R.id.cardKeyword);
        }
    }
}

