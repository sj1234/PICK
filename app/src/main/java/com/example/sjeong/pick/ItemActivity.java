package com.example.sjeong.pick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ItemActivity extends AppCompatActivity {

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Toolbar toolbar = (Toolbar)findViewById(R.id.item_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        TextView name_textview = (TextView) findViewById(R.id.item_name);
        name_textview.setText(name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_faq, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                finish();
                break;
            case R.id.star:
                if(item.getIcon()==getDrawable(R.drawable.up))
                    item.setIcon(getDrawable(R.drawable.down));
                else
                    item.setIcon(getDrawable(R.drawable.up));
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
