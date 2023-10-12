package com.example.to_do_list.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.to_do_list.Models.Notes;

import java.util.List;

@Dao
public interface MainDao {
    @Insert
    void insert(Notes notes);


    //update
    @Query("UPDATE notes SET title = :title, notes = :notes WHERE id = :id")
    void update(int id, String title, String notes);

    //select
    @Query("SELECT * FROM Notes ORDER BY id DESC")
    List<Notes> getAll();

    @Delete
    void delete(Notes notes);
    @Query("UPDATE notes SET pinned = :pin WHERE id = :id")
    void pin(int id, boolean pin);

}
