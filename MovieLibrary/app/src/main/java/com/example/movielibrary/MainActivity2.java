package com.example.movielibrary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.movielibrary.provider.Movie;
import com.example.movielibrary.provider.MovieViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyRecyclerViewAdapter adapter;

//    ArrayList<Movie> movieListRecycle = new ArrayList<Movie>();

    static MovieViewModel mMovieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        SharedPreferences sp = getSharedPreferences("db1", 0);
//        String dbStr = sp.getString("MOV_LIST", "");
//        Type type = new TypeToken<ArrayList<Movie>>(){}.getType();
//        Gson gson = new Gson();
//        movieListRecycle = gson.fromJson(dbStr, type);

        adapter = new MyRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        mMovieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        mMovieViewModel.getAllMovies().observe(this, newData -> {
            adapter.setMovie(newData);
            adapter.notifyDataSetChanged();
        });

        FloatingActionButton fab2 = findViewById(R.id.floatingActionButton2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}