package com.example.russell.taskmanager;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class EditNoteFragment extends Fragment {
    MainActivity activity;
    Note editingNote;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity)getActivity();
        if(activity != null)
            activity.findViewById(R.id.floatingActionButton).setVisibility(View.INVISIBLE);
        return inflater.inflate(R.layout.note_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final EditText editText = view.findViewById(R.id.editText);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int index;
            index = bundle.getInt("noteIndex");
            editingNote = activity.strList.get(index);
            editText.setText(editingNote.getText());
        }
        editText.requestFocus();
        final InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

        FloatingActionButton fragmentFab = view.findViewById(R.id.fragment_fab);
        fragmentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editingNote.setText(editText.getText().toString());
//                newNote.setDate(editText.getText().toString());

               // activity.strList.get()
                //activity.strList.add(editText.getText().toString());
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                activity.dataBaseSQLite.update(editingNote);
                activity.adapter.notifyDataSetChanged();
                activity.getSupportFragmentManager().popBackStack();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if(activity != null)
            activity.findViewById(R.id.floatingActionButton).setVisibility(View.VISIBLE);
        super.onDestroyView();
    }
}
