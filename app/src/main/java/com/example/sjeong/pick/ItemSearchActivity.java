package com.example.sjeong.pick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_search);

        Intent intent = getIntent();
        String text = intent.getStringExtra("data"); // 전달한 key 값
        String type = intent.getStringExtra("type");

        String prime = "";
        if(type.equals("3")){ // 우대조건이 있는 경우
            String[] splite = text.split("/");
            text = "["+splite[0].substring(0, splite[0].length()-1)+"]";
            prime = splite[1];
        }
        else{
            text = "["+text.substring(0, text.length()-1)+"]";
        }

        // item listView
        ListView listView = (ListView)findViewById(R.id.listitem);
        ArrayList<Item> items = new ArrayList<Item>();

        // data 파싱
        try {
            JSONArray jarray = new JSONArray(text);

            for(int i=0; i<jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);
                Item item = new Item();
                item.setName(jObject.get("NAME").toString());
                item.setBank( jObject.get("BANK").toString());
                item.setCode(jObject.get("CODE").toString());
                item.setCont_rate(jObject.get("MIN").toString());
                item.setMax_rate( jObject.get("MAX").toString());

                Log.i("Test", "Fragment items : "+ jObject.toString());
                items.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ItemAdapter listAdapter = new ItemAdapter(this, items, prime, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = v.getTag().toString();

                Intent intent = new Intent(ItemSearchActivity.this, ItemActivity.class);
                intent.putExtra("data",data);
                startActivity(intent);
            }
        });

        listView.setAdapter(listAdapter);
    }
}
