package com.example.roomnote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.roomnote.adapter.Noteadapter;
import com.example.roomnote.database.MyAppDatabase;
import com.example.roomnote.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Entity;

import java.lang.reflect.Executable;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Executor executor;
    MyAppDatabase myAppDatabase;
    Noteadapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// az inja ta 3 khate paein ma refrence dadim .
// hala shekl haye mokhtalefi dare in refrence dadanemon :
        executor = Executors.newSingleThreadExecutor();
        myAppDatabase = MyAppDatabase.getInstance(this);
        final RecyclerView recyclerViewMian = findViewById(R.id.recycelerViewMain);

        //getAllData
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Note> noteList = myAppDatabase.noteDao().getAllNotes();
                noteAdapter = new Noteadapter(MainActivity.this, noteList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewMian.setAdapter(noteAdapter);
                        recyclerViewMian.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    }
                });
            }
        });

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);

            }
        });

        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Note note = noteAdapter.getNoteList().get(viewHolder.getAdapterPosition());
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        myAppDatabase.noteDao().deleteNote(note);
                        updateList();
                    }
                });
                viewHolder.getAdapterPosition();
            }
        });
        touchHelper.attachToRecyclerView(recyclerViewMian);


        recyclerViewMian.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleIemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                int showingRangeItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition() + linearLayoutManager.findLastCompletelyVisibleItemPosition();
                int itemCounts = noteAdapter.getItemCount();
                if (showingRangeItem <= itemCounts - 1) {
                    floatingActionButton.setVisibility(View.VISIBLE);
                } else {
                    if (lastVisibleIemPosition >= itemCounts - 2) {       // yani reside b akhar
                        floatingActionButton.setVisibility(View.INVISIBLE);
                    } else {
                        floatingActionButton.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    public void updateList() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Note> noteList = myAppDatabase.noteDao().getAllNotes();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noteAdapter.adapterUpdate(noteList);
                    }
                });
            }
        });
    }
}
