package com.example.sjeong.pick;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by mijin on 2017-08-14.
 */

public class CalculatorActivity extends AppCompatActivity {
    DepositFragment dfragment;
    InstallmentFragment ifragment;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        //dfragment = new DepositFragment();
        //ifragment = new InstallmentFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.container2, new DepositFragment()).commit();//dfragment).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs2);

        tabs.addTab(tabs.newTab().setText("예금"));
        tabs.addTab(tabs.newTab().setText("적금"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selected = null;

                int position = tab.getPosition();

                switch(position){
                    case 0 : selected = new DepositFragment(); //dfragment;
                        break;
                    case 1 : selected = new InstallmentFragment();//ifragment;
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container2, selected).commit();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}

