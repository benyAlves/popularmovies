package com.udacity.maluleque.popularmovies.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.udacity.maluleque.popularmovies.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAllFavoriteMovies();

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<Movie> findById(long id);

    @Insert
    void insert(Movie movie);

    @Delete
    void delete(Movie movie);

}
