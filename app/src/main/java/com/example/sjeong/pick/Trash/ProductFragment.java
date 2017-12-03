package com.example.sjeong.pick.Trash;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sjeong.pick.My.ProductView;
import com.example.sjeong.pick.R;
import com.example.sjeong.pick.RequestHttpURLConnection;
import com.example.sjeong.pick.Saving.Item2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mijin on 2017-08-09.
 */

public class ProductFragment extends Fragment {

    Context context;
    ProductAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_product, container, false);

        context = getActivity().getApplicationContext();
        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        adapter = new ProductAdapter();

        String url = "http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/getUserProduct.php?";
        ContentValues values = new ContentValues();
        values.put("usr_id","pick");

        NetworkTask2 networkTask2 = new NetworkTask2(url,values);
        networkTask2.execute();

        listView.setAdapter(adapter);

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
            //((TextView)pView.findViewById(R.id.content)).setText(item.getContent());
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

            try {
                JSONArray jsonArray = new JSONArray(result);
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    adapter.addItem(new Item2(jsonObject.getInt("prod_code"),0,jsonObject.getString("prod_name"), jsonObject.getString("rate_range"), jsonObject.getString("prod_detail")));
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
