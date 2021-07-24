package com.example.roomnote.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.roomnote.helpers.Utility;

import java.util.UUID;

@Entity(tableName = "note")
public class Note {

    @PrimaryKey
    @NonNull
    public String id;
    public String title;
    public int isDone;
    public String date;
    @ColumnInfo(defaultValue = "jamshid")
    public String userNmae;

    public Note (String title, int isDone) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.isDone = isDone;

        this.date = Utility.getDateInPersian();


    }
}