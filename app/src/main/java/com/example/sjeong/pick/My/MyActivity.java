package com.example.sjeong.pick.My;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.example.sjeong.pick.ItemActivity;
import com.example.sjeong.pick.R;
import com.example.sjeong.pick.RequestHttpURLConnection;
import com.example.sjeong.pick.Saving.Item2;
import com.example.sjeong.pick.Saving.ProductDetailActivity;

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
        setContentView(R.layout.activity_my);

        SharedPreferences prefs = getSharedPreferences("person",MODE_PRIVATE);
        usr_id = prefs.getString("id",null);

        TextView interest_title = (TextView) findViewById(R.id.interest_title);
        interest_title.setText("'"+usr_id+"'님의 관심상품");

        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && view != null) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        ImageButton back_to_main = (ImageButton) findViewById(R.id.back_to_main);
        back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        context = getApplicationContext();
        ListView listView = (ListView) findViewById(R.id.listView);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = v.getTag().toString();
                int code = Integer.parseInt(data);
                Intent intent;
                if(code>=1010 && code<=1695) {
                    intent = new Intent(MyActivity.this, ItemActivity.class);
                    intent.putExtra("data",data);
                }
                else {
                    intent = new Intent(MyActivity.this, ProductDetailActivity.class);
                    intent.putExtra("prod_code", code);
                }
                startActivity(intent);
            }
        };
        adapter = new ProductAdapter(listener);
        load();
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        load();
        adapter.notifyDataSetChanged();
    }

    public void load(){
        url = "http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/getUserProduct.php?";
        values = new ContentValues();
        values.put("usr_id",usr_id);

        networkTask2 = new NetworkTask2(url,values);
        networkTask2.execute();
    }


    class ProductAdapter extends BaseAdapter {

        private View.OnClickListener listener;
        ArrayList<Item2> items = new ArrayList<Item2>();

        public ProductAdapter(View.OnClickListener listener){
            this.listener = listener;
        }
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
            return id;
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

            ImageButton button = (ImageButton) pView.findViewById(R.id.delete);
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

            String[] splits= item.getProfit().split("~");
            ((TextView)pView.findViewById(R.id.title)).setText(item.getTitle());
            ((TextView)pView.findViewById(R.id.profit)).setText(item.getProfit());
            ((TextView)pView.findViewById(R.id.profit)).setText(splits[0]+"% ~");
            ((TextView)pView.findViewById(R.id.profit2)).setText(splits[1]+"%");
            //((TextView)pView.findViewById(R.id.content)).setText(item.getContent());

            ImageView bank_image = (ImageView)pView.findViewById(R.id.bank);
            Log.i("관심 로고", item.getBank().toString());
            switch(item.getBank().toString()){
                case "NH농협은행":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.nh));
                    break;
                case "기업은행":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ibk));
                    break;
                case "국민은행":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.kb)); break;
                case "우리은행":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.woori)); break;
                case "KEB하나은행":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.keb)); break;
                case "KDB산업은행":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.kdb)); break;
                case "경남은행":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.gn)); break;
                case "광주은행":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.gj)); break;
                case "대구은행":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dg)); break;
                case "부산은행":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bs)); break;
                case "수협은행":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.sh)); break;
                case "스탠다드차타드은행":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.sc)); break;
                case "씨티은행":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.citi)); break;
                case "우체국예금":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.post)); break;
                case "전북은행":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.jb)); break;
                case "제주은행":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.jj)); break;
                case "케이뱅크":
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.kbank)); break;
                case "신한은행" :
                    bank_image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.shinhan)); break;
            }

            if(listener!=null){
                LinearLayout layout = (LinearLayout)pView.findViewById(R.id.change);
                layout.setTag(item.getId()+"");
                layout.setOnClickListener(listener);
            }

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
                        adapter.addItem(new Item2(jsonObject.getInt("prod_code"),0,jsonObject.getString("prod_name"), jsonObject.getString("rate_range"), jsonObject.getString("bank")));
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