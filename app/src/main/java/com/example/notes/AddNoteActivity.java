package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {
private EditText editTextTitle;
private EditText editTextDescription;
private Spinner spinnerDaysOfWeek;
private RadioGroup radioGroupPriority;
private NotesDBHelper dbHelper;
private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        dbHelper = new NotesDBHelper(this);
        database = dbHelper.getWritableDatabase();
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerDaysOfWeek = findViewById(R.id.spinnerDayOfWeek);
        radioGroupPriority = findViewById(R.id.RadioGroupPriority);
    }

    public void onClickSaveNote(View view) {
    String title = editTextTitle.getText().toString().trim();
    String desc = editTextDescription.getText().toString().trim();
    int dayOfWeek = spinnerDaysOfWeek.getSelectedItemPosition();
    int radioButtonId = radioGroupPriority.getCheckedRadioButtonId();
    RadioButton radioButton = findViewById(radioButtonId);
    int priority = Integer.parseInt(radioButton.getText().toString());
    if (isField(title,desc)) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NotesContract.NotesEntries.COLUMN_TITLE, title);
        contentValues.put(NotesContract.NotesEntries.COLUMN_DESCRIPTION, desc);
        contentValues.put(NotesContract.NotesEntries.COLUMN_DAY_OF_WEEK, dayOfWeek+1);
        contentValues.put(NotesContract.NotesEntries.COLUMN_PRIORITY, priority);
        database.insert(NotesContract.NotesEntries.TABLE_NAME, null, contentValues);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }else {
        Toast.makeText(this,"Все поля должны быть заполнены", Toast.LENGTH_SHORT).show();
    }
    }
    private boolean isField(String title,String description){
        return !title.isEmpty() && !description.isEmpty();
    }
}