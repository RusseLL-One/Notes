package com.example.russell.taskmanager;

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

import java.util.List;

public class ArrayAdapterWButton extends ArrayAdapter<Note> {

    static class ViewHolder {
        TextView listItemText;
        FloatingActionButton listItemFab;
    }

    ArrayAdapterWButton(@NonNull Context context, @NonNull List<Note> objects) {
        super(context, R.layout.list_item, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Note note = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.listItemText = convertView.findViewById(R.id.list_item_text);
            holder.listItemFab = convertView.findViewById(R.id.list_item_fab);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (note != null) {
            holder.listItemText.setText(note.getText());
        }
        holder.listItemFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(position);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle noteIndexBundle = new Bundle();
                noteIndexBundle.putInt("noteIndex", position);
                EditNoteFragment editNoteFragment = new EditNoteFragment();
                editNoteFragment.setArguments(noteIndexBundle);
                ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.note_fragment, editNoteFragment).addToBackStack(null).commit();
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
                        Note note = getItem(position);
                        if (note != null) {
                            ((MainActivity) getContext()).dataBaseSQLite.delete(note.getId());
                            remove(note);
                        }
                    }
                }).create();
        alertDialog.show();
    }
}
