package com.example.sjeong.pick.Saving;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sjeong.pick.My.ProductView;
import com.example.sjeong.pick.R;
import com.example.sjeong.pick.RequestHttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mijin on 2017-11-11.
 */

public class ProductActivity extends AppCompatActivity {
    Context context;
    ProductAdapter adapter;
    int prime_cond;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_product);
        context = getApplicationContext();
        ListView listView = (ListView) findViewById(R.id.listView);

        adapter = new ProductAdapter();

        listView.setAdapter(adapter);

        Intent intent = getIntent();


        String json = intent.getStringExtra("json");
        prime_cond = intent.getIntExtra("prime_cond",0);
        Log.d("우대조건",prime_cond+"");

        if(json!=null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    adapter.addItem(new Item2(jsonObject.getInt("id"),jsonObject.getInt("prime_cond"),jsonObject.getString("prod_name"), jsonObject.getString("rate_range"), jsonObject.getString("prod_desc")));
                    Log.d("가져온우대",jsonObject.getInt("prime_cond")+"/"+jsonObject.getInt("id"));
                    Log.d("뭔데", jsonObject.getString("rate_range"));
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int prod_code = ((Item2)adapterView.getAdapter().getItem(i)).getId();

                Log.d("선택",prod_code+"");
                Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
                intent.putExtra("prod_code",prod_code);
                startActivity(intent);
            }
        });

        /*

        String url = "http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/getUserProduct.php?";
        ContentValues values = new ContentValues();
        values.put("usr_id","pick");


        NetworkTask2 networkTask2 = new NetworkTask2(url,values);
        networkTask2.execute();

        listView.setAdapter(adapter);
*/
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_product, container, false);


        return rootView;
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
            ((TextView)pView.findViewById(R.id.title)).setText(item.getTitle());
            ((TextView)pView.findViewById(R.id.profit)).setText(item.getProfit());
            Log.d("왜이래",item.getProfit());
           // ((TextView)pView.findViewById(R.id.content)).setText(item.getContent());

            if((item.getPrime_cond()&prime_cond)==prime_cond){
                pView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
            return pView;
        }


        public void addItem(Item2 item){
            items.add(item);
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




        }
    }
}
