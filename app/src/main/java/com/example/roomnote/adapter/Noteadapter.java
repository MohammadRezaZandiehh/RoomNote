package com.example.roomnote.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomnote.EditActivity;
import com.example.roomnote.R;
import com.example.roomnote.database.MyAppDatabase;
import com.example.roomnote.model.Note;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Noteadapter extends RecyclerView.Adapter<Noteadapter.NoteViewHolder> {
    Context context;
    List<Note> noteList;
    Executor executor;
    MyAppDatabase myAppDatabase;

    public Noteadapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
        executor = Executors.newSingleThreadExecutor();
        myAppDatabase = MyAppDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_note_item, parent, false);

        NoteViewHolder noteViewHolder = new NoteViewHolder(view);
        return noteViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.textViewNoteTitle.setText(note.title);
        if (note.isDone == 1) {
            holder.imageViewNoteDoneStatus.setImageResource(R.drawable.ic_notedone);
            holder.textViewNoteTitle.setPaintFlags(holder.textViewNoteTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.imageViewNoteDoneStatus.setImageResource(R.drawable.ic_done);
            holder.textViewNoteTitle.setPaintFlags(0);
        }
    }

    //stackOverFlow:
    //tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    @Override
    public int getItemCount() {
        return noteList.size();
    }


    class NoteViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewNoteDoneStatus;
        TextView textViewNoteTitle;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewNoteDoneStatus = itemView.findViewById(R.id.imageViewNoteDoneStatus);
            textViewNoteTitle = itemView.findViewById(R.id.textViewNoteTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditActivity.class);
                    intent.putExtra("noteId", noteList.get(getAdapterPosition()).id);
                    context.startActivity(intent);
                }
            });
            imageViewNoteDoneStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Note note = noteList.get(getAdapterPosition());
                    if (note.isDone == 0) {
                        note.isDone = 1;
                    } else {
                        note.isDone = 0;
                    }
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            myAppDatabase.noteDao().updateNote(note);
                            noteList = myAppDatabase.noteDao().getAllNotes();
                        }
                    });
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }


    public void adapterUpdate(List<Note> noteList) {
        this.noteList = noteList;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }
}
//tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
