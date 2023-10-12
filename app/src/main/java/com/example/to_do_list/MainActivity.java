package com.example.to_do_list;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.to_do_list.Adaptors.NotesAdaptor;
import com.example.to_do_list.Database.DBHelper;
import com.example.to_do_list.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    RecyclerView recyclerView;
    NotesAdaptor notesAdaptor;
    List<Notes> notes = new ArrayList<>();
    DBHelper database;
    FloatingActionButton fab_add;
    SearchView searchview_home;
    Notes selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_home);
        fab_add = findViewById(R.id.fab_add);
        searchview_home = findViewById(R.id.searchview_home);

        database = DBHelper.getInstance(this);
        notes = database.mainDao().getAll();




        updateRecycler(notes);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Add_Nodes_Activity.class);
                startActivityForResult(intent, 101);
            }
        });

        searchview_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

    }

    private void filter(String newText){
        List<Notes> filterList = new ArrayList<>();
        for(Notes singlenotes: notes){
            if(singlenotes.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                    singlenotes.getNotes().toLowerCase().contains(newText.toLowerCase())){
            filterList.add(singlenotes);
            }
        }
        notesAdaptor.filterlist(filterList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Notes new_notes = (Notes) data.getSerializableExtra("notes");
                database.mainDao().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDao().getAll());
                notesAdaptor.notifyDataSetChanged();
            }
        } else if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                Notes new_notes = (Notes) data.getSerializableExtra("notes");
                database.mainDao().update(new_notes.getId(), new_notes.getTitle(), new_notes.getNotes());
                notes.clear();
                notes.addAll(database.mainDao().getAll());
                notesAdaptor.notifyDataSetChanged();
            }
        }

    }

    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesAdaptor = new NotesAdaptor(MainActivity.this, notes, notesClickListner);
        recyclerView.setAdapter(notesAdaptor);
    }

    private final NotesClickListner notesClickListner = new NotesClickListner() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, Add_Nodes_Activity.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNote = new Notes();
            selectedNote = notes;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.pin) {
            if (selectedNote.isPinned()) {
                database.mainDao().pin(selectedNote.getId(), false);
                Toast.makeText(this, "Unpinned", Toast.LENGTH_SHORT).show();
            } else {
                database.mainDao().pin(selectedNote.getId(), true);
                Toast.makeText(this, "Pinned!", Toast.LENGTH_SHORT).show();
            }

            notes.clear();
            notes.addAll(database.mainDao().getAll());
            notesAdaptor.notifyDataSetChanged();
            return true;
        }
        else if(itemId == R.id.delete){
            database.mainDao().delete(selectedNote);
            notes.remove(selectedNote);
            notesAdaptor.notifyDataSetChanged();
            Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show();
            return true;

        }
        else
            return false;


    }
}