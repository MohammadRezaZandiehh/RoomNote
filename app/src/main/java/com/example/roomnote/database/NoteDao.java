package com.example.roomnote.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.roomnote.model.Note;
import java.util.List;

import java.util.List;
@Dao
public interface NoteDao {

    @Insert
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Update
    void updateNote(Note note);

    @Query("select * from note")
    List<Note> getAllNotes();

    @Query("select * from note where id = :id")
    Note getNoteById(String id);
}
