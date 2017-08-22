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
        top10s = topSample();

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

    public ArrayList<Top10> topSample(){
        ArrayList<Top10> top10s = new ArrayList<Top10>();

        Top10 top10 = new Top10();
        top10.setTop_name("i-ONE 놀이터 예금");
        top10.setTop_thisrank(1);
        top10.setTop_prerank(6);
        top10s.add(top10);

        Top10 top101 = new Top10();
        top101.setTop_name("i-ONE 1,000 예금");
        top101.setTop_thisrank(2);
        top101.setTop_prerank(8);
        top10s.add(top101);

        Top10 top102 = new Top10();
        top102.setTop_name("IBK 직장인적금");
        top102.setTop_thisrank(3);
        top102.setTop_prerank(4);
        top10s.add(top102);

        Top10 top103 = new Top10();
        top103.setTop_name("i-ONE 놀이터 적금");
        top103.setTop_thisrank(4);
        top103.setTop_prerank(1);
        top10s.add(top103);

        Top10 top104 = new Top10();
        top104.setTop_name("i-ONE 300 적금");
        top104.setTop_thisrank(5);
        top104.setTop_prerank(5);
        top10s.add(top104);

        Top10 top105 = new Top10();
        top105.setTop_name("i-미래통장");
        top105.setTop_thisrank(6);
        top105.setTop_prerank(10);
        top10s.add(top105);

        Top10 top106 = new Top10();
        top106.setTop_name("1석7조통장");
        top106.setTop_thisrank(7);
        top106.setTop_prerank(-1);
        top10s.add(top106);

        Top10 top107 = new Top10();
        top107.setTop_name("IBK썸통장");
        top107.setTop_thisrank(8);
        top107.setTop_prerank(3);
        top10s.add(top107);

        Top10 top108 = new Top10();
        top108.setTop_name("IBK군인적금");
        top108.setTop_thisrank(9);
        top108.setTop_prerank(2);
        top10s.add(top108);

        Top10 top109 = new Top10();
        top109.setTop_name("참! 좋은기부적금");
        top109.setTop_thisrank(10);
        top109.setTop_prerank(-1);
        top10s.add(top109);

        return top10s;
    }

}
