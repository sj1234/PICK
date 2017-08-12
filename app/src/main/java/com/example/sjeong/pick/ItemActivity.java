package com.example.sjeong.pick;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ItemActivity extends AppCompatActivity {

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        TextView name_textview = (TextView) findViewById(R.id.item_name);
        name_textview.setText(name);
    }
}
