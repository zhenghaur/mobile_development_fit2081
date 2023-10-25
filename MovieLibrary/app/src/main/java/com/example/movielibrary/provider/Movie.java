package com.example.movielibrary.provider;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "movieID")
    private int id;

    @ColumnInfo(name = "movieTitle")
    private String title;

    @ColumnInfo(name = "movieYear")
    private int year;

    @ColumnInfo(name = "movieCountry")
    private String country;

    @ColumnInfo(name = "movieGenre")
    private String genre;

    @ColumnInfo(name = "movieCost")
    private int cost;

    @ColumnInfo(name = "movieKeyword")
    private String keyword;

    public Movie(String title, int year, String country, String genre, int cost, String keyword) {
        this.title = title;
        this.year = year;
        this.country = country;
        this.genre = genre;
        this.cost = cost;
        this.keyword = keyword;
    }

    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
