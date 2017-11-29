package com.example.sjeong.pick;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyActivity extends AppCompatActivity {
    String url;
    ContentValues values;
    NetworkTask2 networkTask2;
    Context context;
    ProductAdapter adapter;
    int id;
    String usr_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_product);

        SharedPreferences prefs = getSharedPreferences("PrefName",MODE_PRIVATE);
        //usr_id = prefs.getString("id",null);
        usr_id="pick";

        context = getApplicationContext();
        ListView listView = (ListView) findViewById(R.id.listView);

        adapter = new ProductAdapter();

        load();

        listView.setAdapter(adapter);

    }

    public void load(){
        url = "http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/getUserProduct.php?";
        values = new ContentValues();
        values.put("usr_id",usr_id);

        networkTask2 = new NetworkTask2(url,values);
        networkTask2.execute();
    }
    class ProductAdapter extends BaseAdapter {
        ArrayList<Item2> items = new ArrayList<Item2>();

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
            ProductView pView = new ProductView(context);
            Item2 item = items.get(i);

            id = item.getId();
            SwipeLayout swipeLayout =  (SwipeLayout) pView.findViewById(R.id.swipeLayout);

//set show mode.
            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

//add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, pView.findViewById(R.id.bottom_wrapper));

            swipeLayout.addSwipeListener(new SimpleSwipeListener(){
                @Override
                public void onOpen(SwipeLayout layout) {
                    super.onOpen(layout);
                }
            });

            Button button = (Button) pView.findViewById(R.id.delete);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    url = "http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/deleteUserProduct.php?";
                    values = new ContentValues();
                    values.put("usr_id",usr_id);
                    values.put("id",id);

                    networkTask2 = new NetworkTask2(url, values);
                    networkTask2.execute();

                    load();
                }
            });
            ((TextView)pView.findViewById(R.id.title)).setText(item.getTitle());
            ((TextView)pView.findViewById(R.id.profit)).setText(item.getProfit());
            ((TextView)pView.findViewById(R.id.content)).setText(item.getContent());
            return pView;
        }


        public void addItem(Item2 item){
            items.add(item);
        }

        public void clear(){
            items.clear();
        }
    }


    public class NetworkTask2 extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask2(String url, ContentValues values) {

            this.url = url;
            this.values = values;

        }

        @Override
        protected String doInBackground(Void... params) {


            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);
            // 해당 URL로 부터 결과물을 얻어온다.


            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                if(result!=null) {
                    adapter.clear();
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        adapter.addItem(new Item2(jsonObject.getInt("prod_code"), jsonObject.getString("prod_name"), jsonObject.getString("rate_range"), jsonObject.getString("prod_detail")));
                    }
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}
    /*
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
}*/