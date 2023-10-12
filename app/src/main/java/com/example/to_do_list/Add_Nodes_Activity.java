package com.example.to_do_list;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.to_do_list.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Add_Nodes_Activity extends AppCompatActivity {

    EditText editText_title, editText_note;
    ImageView imageView_save;
    Notes note;
    boolean isOldNote = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nodes);

        imageView_save = findViewById(R.id.imageView_save);
        editText_title = findViewById(R.id.editText_title);
        editText_note = findViewById(R.id.editText_note);


        note = new Notes();
        try {
            note = (Notes) getIntent().getSerializableExtra("old_note");
            editText_title.setText(note.getTitle());
            editText_note.setText(note.getNotes());
            isOldNote = true;

        }catch (Exception e){
            e.printStackTrace();
        }

        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editText_title.getText().toString();
                String notes = editText_note.getText().toString();

                if (notes.isEmpty()) {
                    Toast.makeText(Add_Nodes_Activity.this, "Please add a note", Toast.LENGTH_SHORT).show();
                    return;
                }
                    SimpleDateFormat format = new SimpleDateFormat("EEE, d, MMM yyyy HH:mm: a");
                    Date date = new Date();


                    if(!isOldNote){
                        note = new Notes();
                    }

                   note.setTitle(title);
                   note.setNotes(notes);
                   note.setDate(format.format(date));
//to pass data like this you have to make note class serializable
                Intent intent = new Intent();
                intent.putExtra("notes", note);
                setResult(Activity.RESULT_OK, intent);
                finish();


            }
        });

    }
}