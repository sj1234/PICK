package com.example.sjeong.pick;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.labo.kaji.fragmentanimations.MoveAnimation;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchDetailFragment extends Fragment {

    private String animation = "UP";
    public SearchDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_detail, container, false);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        switch(animation){
            case "UP":
                return MoveAnimation.create(MoveAnimation.UP, enter, 500);
            case "DOWN":
                animation = "UP";
                return MoveAnimation.create(MoveAnimation.DOWN, enter, 500);
        }
        return null;
    }

    public void setAnimation(String animation){
        this.animation = animation;
    }
}
