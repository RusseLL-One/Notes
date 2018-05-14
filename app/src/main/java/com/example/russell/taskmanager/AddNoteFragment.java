package com.example.russell.taskmanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class AddNoteFragment extends Fragment {
    MainActivity activity;
    EditText noteEditText;
    EditText dateEditText;
    FloatingActionButton fragmentFab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        if (activity != null)
            activity.findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);
        return inflater.inflate(R.layout.note_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        noteEditText = view.findViewById(R.id.noteEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        dateEditText.setOnFocusChangeListener(showDatePickerDialog);
        if (activity == null) {
            return;
        }
        noteEditText.requestFocus();
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            //↓ Чтобы убрать варнинг "may produce NullPointerException"
            imm.showSoftInput(noteEditText, InputMethodManager.SHOW_IMPLICIT);
        }

        fragmentFab = view.findViewById(R.id.fragment_fab);
        fragmentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note newNote = new Note();
                newNote.setText(noteEditText.getText().toString());
                newNote.setDate(dateEditText.getText().toString());
                activity.dataBaseSQLite.write(newNote);
                activity.strList.add(newNote);
                if (imm != null && activity.getCurrentFocus() != null) {
                    //↓ Чтобы убрать варнинг "may produce NullPointerException"
                    imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                    return;
                }
                activity.adapter.notifyDataSetChanged();
                activity.getSupportFragmentManager().popBackStack();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    View.OnFocusChangeListener showDatePickerDialog = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month = Calendar.getInstance().get(Calendar.MONTH);
                int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                DatePickerDialog tpd = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateEditText.setText(dayOfMonth + "." + (month + 1) + "." + year);
                    }
                }, year, month, day);
                tpd.show();
            }
        }
    };

    @Override
    public void onDestroyView() {
        if (activity != null)
            activity.findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
        super.onDestroyView();
    }
}
