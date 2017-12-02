package com.example.sjeong.pick.Saving;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sjeong.pick.R;
import com.labo.kaji.fragmentanimations.MoveAnimation;

/**
 * Created by mijin on 2017-12-01.
 */

public class SearchSimpleSavingFragment extends Fragment {
    private EditText searchtext;
    private Button base_detail;
    private int animation=0, time=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_simple_saving,null);

        base_detail = (Button)view.findViewById(R.id.base_detail);
        base_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                SearchSimpleSavingFragment fragment = (SearchSimpleSavingFragment)getActivity().getSupportFragmentManager().findFragmentByTag("SearchSimpleSaving");
                SearchDetailSavingFragment detail = (SearchDetailSavingFragment)getActivity().getSupportFragmentManager().findFragmentByTag("SearchDetailSaving");
                if(detail==null) {
                    detail = new SearchDetailSavingFragment();
                    fragment.setAnimation("UP");
                    detail.setAnimation("UP");
                    ft.add(R.id.fragment_search, detail, "SearchDetailSaving");
                }
                else {
                    fragment.setAnimation("UP");
                    detail.setAnimation("UP");
                    ft.show(detail);
                }
                ft.hide(fragment);
                ft.commit();
            }
        });

        searchtext = (EditText)view.findViewById(R.id.searchText);
        ImageButton searchbutton = (ImageButton)view.findViewById(R.id.searchbutton);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 기본검색
                String text = searchtext.getText().toString();
                Log.i("Test", text);

                if(text.isEmpty())
                    Toast.makeText(getActivity(), "상품명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                else{
                   //네트워크
                }

            }
        });

        return view;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return MoveAnimation.create(animation, enter, time);
    }

    public void setAnimation(String type){ // 애니메이션 설정
        time = 1000;
        if(type.equals("UP"))
            animation = MoveAnimation.UP;
        else
            animation = MoveAnimation.DOWN;
    }

}
