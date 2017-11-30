package com.example.sjeong.pick;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Created by mijin on 2017-12-01.
 */

public class SearchSimpleSavingFragment extends Fragment {

    EditText editText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_dep,container,false);


        Button btn = (Button) view.findViewById(R.id.base_detail);
        editText = (EditText) view.findViewById(R.id.searchText);
        ImageButton btn2 = (ImageButton) view.findViewById(R.id.searchbutton);


        return view;
    }
}
