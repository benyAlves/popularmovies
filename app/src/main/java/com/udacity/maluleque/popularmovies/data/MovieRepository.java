package com.udacity.maluleque.popularmovies.data;

import com.udacity.maluleque.popularmovies.database.AppExecutors;
import com.udacity.maluleque.popularmovies.database.MovieDao;

public class MovieRepository {
    private static MovieRepository repository;
    private final MovieDao movieDao;
    private final AppExecutors appExecutors;

    public MovieRepository(MovieDao movieDao, AppExecutors appExecutors) {
        this.movieDao = movieDao;
        this.appExecutors = appExecutors;
    }


}
