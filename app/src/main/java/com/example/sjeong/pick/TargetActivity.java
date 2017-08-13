package com.example.sjeong.pick;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mijin on 2017-08-13.
 */

public class TargetActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);


        ListView listView = (ListView) findViewById(R.id.listView2);

        TargetAdapter adapter = new TargetAdapter();
        adapter.addItem(new Item3("IBK든든",100,20000,20));
        adapter.addItem(new Item3("국민든든",200,40000,20));

        listView.setAdapter(adapter);


        FloatingActionButton floatingBtn = (FloatingActionButton) findViewById(R.id.floating_btn);
            floatingBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TargetSettingActivity.class);
                startActivity(intent);
            }
        });
    }

    class TargetAdapter extends BaseAdapter {
        ArrayList<Item3> items = new ArrayList<Item3>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TargetView targetView = new TargetView(getApplicationContext());
            Item3 item = items.get(i);
            ((TextView) targetView.findViewById(R.id.productTitle)).setText(item.getTargetTitle());
            ((TextView) targetView.findViewById(R.id.productTarget)).setText(item.toString());
            //((TextView)targetView.findViewById(R.id.progress)).setText(((ProgressBar)targetView.findViewById(R.id.progressBar)).getProgress());

            return targetView;
        }

        public void addItem(Item3 item) {
            items.add(item);
        }
    }
}
