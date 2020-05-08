package com.udacity.maluleque.popularmovies.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.udacity.maluleque.popularmovies.model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movies";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract MovieDao MovieDao();
}
