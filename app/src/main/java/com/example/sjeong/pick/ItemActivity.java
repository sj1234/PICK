package com.example.sjeong.pick;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
        //toolbar.setBackground(new ColorDrawable(Color.parseColor("#00ff0000")));
        toolbar.getBackground().setAlpha(0);
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
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        Drawable full = ContextCompat.getDrawable(this, R.drawable.full_star);
        Drawable empty = ContextCompat.getDrawable(this, R.drawable.empty_star);
        Bitmap bitmap1 = ((BitmapDrawable)full).getBitmap();

        switch(id){
            case android.R.id.home:
                finish();
                break;
            case R.id.star:
                Log.i("Test", "Click star button");
                Bitmap bitmap3 = ((BitmapDrawable)(item.getIcon())).getBitmap();
                if(bitmap3.equals(bitmap1))
                    item.setIcon(empty);
                else
                    item.setIcon(full);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
