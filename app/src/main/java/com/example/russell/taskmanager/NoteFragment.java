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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class NoteFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.note_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final EditText editText = view.findViewById(R.id.editText);
        final MainActivity activity = (MainActivity)getActivity();
        if (activity == null) {
            return;
        }
        editText.requestFocus();
        final InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
       // if(editText.requestFocus()) {
           //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //}

        FloatingActionButton fragmentFab = view.findViewById(R.id.fragment_fab);
        fragmentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.strList.add(editText.getText().toString());
                assert imm != null;
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                activity.getSupportFragmentManager().popBackStack();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
