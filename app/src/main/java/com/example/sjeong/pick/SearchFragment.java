package com.example.sjeong.pick;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class SearchFragment extends Fragment {

    private EditText searchtext;
    private TextView detailbutton;
    private Boolean detailOn = false;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,null);

        searchtext = (EditText)view.findViewById(R.id.searchText);

        ImageButton searchbutton = (ImageButton)view.findViewById(R.id.searchbutton);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = searchtext.getText().toString();

                if(text.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("검색어를 입력해 주십시오");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                else{
                    Log.i("Test", text);

                    ItemSearchFragment fragment = new ItemSearchFragment();

                    Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
                    bundle.putString("searchtext", text); // key , value
                    fragment.setArguments(bundle);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    if(detailOn){
                        Fragment removefragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_down);
                        ft.remove(removefragment);
                        detailOn =false;
                    }
                    ft.replace(R.id.fragment_down, fragment);
                    ft.commit();
                }
            }
        });

        detailbutton = (TextView)view.findViewById(R.id.searchdetail);
        detailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                if(!detailOn){
                    Log.i("Test", "detail On");
                    detailOn = true;
                    detailbutton.setText("∧");

                    SearchDetailFragment fragment = new SearchDetailFragment();
                    fragment.setAnimation("DOWN");
                    ft.add(R.id.fragment_down, fragment);
                    ft.commit();
                }
                else{
                    Log.i("Test", "detail Off");
                    detailOn = false;
                    detailbutton.setText("∨");

                    Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_down);
                    ft.remove(fragment);
                    ft.commit();
                }
            }
        });

        return view;
    }

}