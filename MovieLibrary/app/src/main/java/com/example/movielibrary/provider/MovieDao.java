package com.example.movielibrary.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("select * from movies")
    LiveData<List<Movie>> getAllMovies();

    @Insert
    void insertMovie(Movie movie);

    @Query("delete from movies where movieYear= :year")
    void deleteMovieYear(int year);

    @Query("delete FROM movies")
    void deleteAllMovies();
}
