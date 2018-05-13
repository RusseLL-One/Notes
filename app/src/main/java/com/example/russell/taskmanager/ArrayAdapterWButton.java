package com.example.russell.taskmanager;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ArrayAdapterWButton extends ArrayAdapter<Note> {

    public ArrayAdapterWButton(@NonNull Context context, @NonNull List<Note> objects) {
        super(context, R.layout.list_item, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Note note = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle noteIndexBundle = new Bundle();
                    noteIndexBundle.putInt("noteIndex", position);
                    EditNoteFragment editNoteFragment = new EditNoteFragment();
                    editNoteFragment.setArguments(noteIndexBundle);
                    ((MainActivity)getContext()).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.note_fragment , editNoteFragment).addToBackStack(null).commit();
                }
            });
        }
        ((TextView) convertView.findViewById(R.id.list_item_text))
                .setText(note.getText());
        (convertView.findViewById(R.id.list_item_fab))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDeleteDialog(position);
                    }
                });
        return convertView;
    }

    private void showDeleteDialog(final int position) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Удаление элемента")
                .setMessage("Вы действительно хотите удалить эту запись?")
                .setNegativeButton("Нет", null)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)getContext()).dataBaseSQLite.delete(getItem(position).getId());
                        remove(getItem(position));
                        System.out.println();
                    }
                }).create();
        alertDialog.show();
    }
}
