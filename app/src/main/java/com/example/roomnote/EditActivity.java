package com.example.roomnote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.roomnote.database.MyAppDatabase;
import com.example.roomnote.helpers.Utility;
import com.example.roomnote.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditActivity extends AppCompatActivity {
    MyAppDatabase myAppDatabase;
    Executor executor;
    TextInputEditText textInputEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        myAppDatabase = MyAppDatabase.getInstance(EditActivity.this);
        executor = Executors.newSingleThreadExecutor();

        String noteId = getIntent().getStringExtra("noteId");
        if (noteId != null && !noteId.equals("")) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Note note = myAppDatabase.noteDao().getNoteById(noteId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textInputEditText.setText(note.title);
                        }
                    });
                }
            });
        }


        FloatingActionButton floatingActionButtonEdit = findViewById(R.id.floatingActionButtonEdit);
        textInputEditText = findViewById(R.id.textInputEditTextNoteTitle);
        floatingActionButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textInputEditText.getText().toString().equals("")) {
                    Toast.makeText(EditActivity.this, "متن را وارد کنید", Toast.LENGTH_SHORT).show();
                    return;
                }

                String content = textInputEditText.getText().toString();
                if (noteId != null && !noteId.equals("")) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            Note note = myAppDatabase.noteDao().getNoteById(noteId);
                            note.title = content;
                            note.date = Utility.getDateInPersian();

                            myAppDatabase.noteDao().updateNote(note);
                        }
                    });
                    finish();
                    return;
                }


                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        myAppDatabase.noteDao().insertNote(new Note(content, 0));

                    }
                });
            }

        });

    }
}
