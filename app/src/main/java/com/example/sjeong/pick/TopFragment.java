package com.example.sjeong.pick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ListView;

import com.labo.kaji.fragmentanimations.MoveAnimation;

import java.util.ArrayList;

public class TopFragment extends Fragment {

    private String animation = "NONE";

    public TopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top,null);
        ListView listView = (ListView)view.findViewById(R.id.listTop);

        ArrayList<Top10> top10s = new ArrayList<Top10>();

        for(int i=1; i<4; i++){
            Top10 top10 = new Top10();

            top10.setTop_name("예금 "+i);
            top10.setTop_thisrank(i);
            top10.setTop_prerank(4-i);

            top10s.add(top10);
        }

        TopAdapter listAdapter = new TopAdapter(getActivity(), R.layout.list_topadapter, top10s, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemname = v.getTag().toString();

                Intent intent = new Intent(getActivity(), ItemActivity.class);
                intent.putExtra("name",itemname);
                startActivity(intent);
            }
        });

        listView.setAdapter(listAdapter);

        return view;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        switch(animation){
            case "NONE":
                break;
            case "UP":
                animation = "DOWN";
                return MoveAnimation.create(MoveAnimation.UP, enter, 500);
            case "DOWN":
                return MoveAnimation.create(MoveAnimation.DOWN, enter, 500);
        }
        return null;
    }

    public void setAnimation(String animation){
        this.animation = animation;
    }

}
