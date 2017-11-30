package com.example.sjeong.pick;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class GoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        // toolbar 설정
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        GoalFragment goal = new GoalFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_goal, goal, "goal_fragment");
        ft.commit();
    }

    // 메뉴 클릭 listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_goal);

                if(fragment.getTag().toString().equals("goal_fragment")) {
                    finish();
                }
                else{
                    GoalFragment goal = (GoalFragment)getSupportFragmentManager().findFragmentByTag("goal_fragment");
                    ft.remove(fragment);
                    ft.show(goal);
                    ft.commit();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
