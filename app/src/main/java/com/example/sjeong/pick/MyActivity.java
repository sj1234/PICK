package com.example.sjeong.pick;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import static android.support.design.widget.TabLayout.OnTabSelectedListener;
import static android.support.design.widget.TabLayout.Tab;

public class MyActivity extends AppCompatActivity {
    SettingFragment sfragment;
    ProductFragment pfragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        sfragment = new SettingFragment();
        pfragment = new ProductFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.container, sfragment).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);

        tabs.addTab(tabs.newTab().setText("기본 정보 설정"));
        tabs.addTab(tabs.newTab().setText("관심 상품"));

        tabs.setOnTabSelectedListener(new OnTabSelectedListener(){
            @Override
            public void onTabSelected(Tab tab) {
                Fragment selected = null;

                int position = tab.getPosition();

                switch(position){
                    case 0 : selected = sfragment;
                        break;
                    case 1 : selected = pfragment;
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();

            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
    }
}