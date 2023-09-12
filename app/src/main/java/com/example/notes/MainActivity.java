package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
private RecyclerView recyclerView;
public static final ArrayList<Note> arrayList = new ArrayList<>();
private NotesDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerViewNotes);
        dbHelper = new NotesDBHelper(this);
        SQLiteDatabase  database = dbHelper.getWritableDatabase();
        if (arrayList.isEmpty()) {
            arrayList.add(new Note("Парихмахер", "Сделать причёску", 2, "Вторник"));
            arrayList.add(new Note("Реснички", "Сделать реснички", 3, "Четверг"));
        }
        for (Note note : arrayList){
            ContentValues contentValues = new ContentValues();
            contentValues.put(NotesContract.NotesEntries.COLUMN_TITLE, note.getTitle());
            contentValues.put(NotesContract.NotesEntries.COLUMN_DESCRIPTION, note.getDescription());
            contentValues.put(NotesContract.NotesEntries.COLUMN_DAY_OF_WEEK, note.getDayOfWeek());
            contentValues.put(NotesContract.NotesEntries.COLUMN_PRIORITY, note.getPriority());
            database.insert(NotesContract.NotesEntries.TABLE_NAME,null, contentValues);
        }
        ArrayList<Note> notesFromDB = new ArrayList<>();
        Cursor cursor = database.query(NotesContract.NotesEntries.TABLE_NAME,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntries.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntries.COLUMN_DESCRIPTION));
            String day_of_week = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntries.COLUMN_DAY_OF_WEEK));
            int priority = cursor.getInt(cursor.getColumnIndexOrThrow(NotesContract.NotesEntries.COLUMN_PRIORITY));
            Note note = new Note(title,description,priority,day_of_week);
            notesFromDB.add(note);
        }
        cursor.close();
        NotesAdapter notesAdapter = new NotesAdapter(notesFromDB);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notesAdapter);
        notesAdapter.setOnNoteClickListner(new NotesAdapter.OnNoteClickListner() {
            @Override
            public void onNoteClick(int position) {
                Toast.makeText(MainActivity.this,"Номер позиции:"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNoteLongClick(int position) {
                arrayList.remove(position);
                notesAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onClickAddNote(View view) {
        Intent intent = new Intent(this,AddNoteActivity.class);
        startActivity(intent);
    }
}