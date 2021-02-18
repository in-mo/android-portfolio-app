package com.cookandroid.mini_notice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserSearchByPass extends Fragment {

    EditText passSearchByID;
    EditText passSearchByName;
    EditText passSearchByEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.pass_search, container, false);

        passSearchByID = (EditText) rootView.findViewById(R.id.passSearchByID);
        passSearchByName = (EditText) rootView.findViewById(R.id.passSearchByName);
        passSearchByEmail = (EditText) rootView.findViewById(R.id.passSearchByEmail);

        return rootView;
    }

    public String[] getText() {
        String[] text = {passSearchByID.getText().toString().trim(),
                passSearchByName.getText().toString(),
                passSearchByEmail.getText().toString()};
        return text;
    }
}
