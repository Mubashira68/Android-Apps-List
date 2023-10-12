package com.example.to_do_list.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.to_do_list.Models.Notes;

@Database(entities = Notes.class, version = 1, exportSchema = false)
public abstract class DBHelper extends RoomDatabase {
    private static DBHelper instance;
    private static String DATABASE_NAME = "NoteApp";
    public static synchronized DBHelper getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, DBHelper.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract MainDao mainDao();
}
