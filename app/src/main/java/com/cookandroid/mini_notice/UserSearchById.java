package com.cookandroid.mini_notice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserSearchById extends Fragment {

    EditText idSearchByName;
    EditText idSearchByEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.id_search, container, false);

        idSearchByName = (EditText) rootView.findViewById(R.id.idSearchByName);
        idSearchByEmail = (EditText) rootView.findViewById(R.id.idSearchByEmail);

        return rootView;
    }

    public String[] getText() {
        String[] text = {idSearchByName.getText().toString().trim(),
                idSearchByEmail.getText().toString()};
        return text;
    }
}
