package com.example.russell.taskmanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class EditNoteFragment extends AddNoteFragment {
    Note editingNote;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        noteEditText = view.findViewById(R.id.noteEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        dateEditText.setOnFocusChangeListener(showDatePickerDialog);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int index;
            index = bundle.getInt("noteIndex");
            editingNote = activity.strList.get(index);
            noteEditText.setText(editingNote.getText());
            dateEditText.setText(editingNote.getDate());
        }
        noteEditText.requestFocus();
        final InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            //↓ Чтобы убрать варнинг "may produce NullPointerException"
            imm.showSoftInput(noteEditText, InputMethodManager.SHOW_IMPLICIT);
        }

        fragmentFab = view.findViewById(R.id.fragment_fab);

        fragmentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editingNote.setText(noteEditText.getText().toString());
                editingNote.setDate(dateEditText.getText().toString());
                if (imm != null && activity.getCurrentFocus() != null) {
                    //↓ Чтобы убрать варнинг "may produce NullPointerException"
                    imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                    return;
                }
                activity.dataBaseSQLite.update(editingNote);
                activity.adapter.notifyDataSetChanged();
                activity.getSupportFragmentManager().popBackStack();
            }
        });
    }
}
