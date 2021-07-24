package com.example.roomnote.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.roomnote.model.Note;

@Database(entities = {Note.class}, version = 3, exportSchema = false)
public abstract class MyAppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "noteapp";    // esme table ro tosh gharar dadim .
    private static MyAppDatabase instance;
    static final Migration migration = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table note add column userNmae text default 'jamshid'");
        }
    } ;

    public static synchronized MyAppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, MyAppDatabase.class, DATABASE_NAME)
                    .addMigrations(migration)
                    //.fallbackToDestructiveMigration()
                    // khate bala yani agha har zaman k verson number ro update kardm va shema ha avaz shode bod
                    // , to database ha ro az aval besaz va dade haye toosh ro negah nadar .
                    .build();
        }
        return instance;
    }

    public abstract NoteDao noteDao();
    // add all your DAO'S interfacees in here
}

