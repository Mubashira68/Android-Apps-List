package com.example.to_do_list;

import androidx.cardview.widget.CardView;

import com.example.to_do_list.Models.Notes;

public interface NotesClickListner {
    void onClick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);
}
