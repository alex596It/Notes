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
private final ArrayList<Note> arrayList = new ArrayList<>();
private NotesDBHelper dbHelper;
private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerViewNotes);
        dbHelper = new NotesDBHelper(this);
        database = dbHelper.getWritableDatabase();
        getData();
        NotesAdapter notesAdapter = new NotesAdapter(arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notesAdapter);
        notesAdapter.setOnNoteClickListner(new NotesAdapter.OnNoteClickListner() {
            @Override
            public void onNoteClick(int position) {
                Toast.makeText(MainActivity.this,"Номер позиции:"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNoteLongClick(int position) {
                int id = arrayList.get(position).getId();
                String where = NotesContract.NotesEntries._ID + " = ?";
                String[] whereArgs = new String[]{Integer.toString(id)} ;
                database.delete(NotesContract.NotesEntries.TABLE_NAME,where,whereArgs);
                getData();
                notesAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onClickAddNote(View view) {
        Intent intent = new Intent(this,AddNoteActivity.class);
        startActivity(intent);
    }
    private void getData(){
        arrayList.clear();
        Cursor cursor = database.query(NotesContract.NotesEntries.TABLE_NAME,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(NotesContract.NotesEntries._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntries.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.NotesEntries.COLUMN_DESCRIPTION));
           int day_of_week = cursor.getInt(cursor.getColumnIndexOrThrow(NotesContract.NotesEntries.COLUMN_DAY_OF_WEEK));
            int priority = cursor.getInt(cursor.getColumnIndexOrThrow(NotesContract.NotesEntries.COLUMN_PRIORITY));
            Note note = new Note(id,title,description,priority,day_of_week);
            arrayList.add(note);
        }
        cursor.close();
    }
}