package com.example.sjeong.pick.Saving;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.sjeong.pick.R;
import com.example.sjeong.pick.RequestHttpURLConnection;

/**
 * Created by mijin on 2017-11-20.
 */

public class ProductDetailActivity extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Intent intent = getIntent();
        int prod_code = intent.getIntExtra("prod_code",0);

        String url = "http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/getProductDetail.php?";
        ContentValues values = new ContentValues();
        values.put("prod_code",prod_code);

        NetworkTask2 networkTask2 = new NetworkTask2(url,values);
        networkTask2.execute();


        textView = (TextView) findViewById(R.id.textView);


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


            //String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            String result = requestHttpURLConnection.request(url, values);
            // 해당 URL로 부터 결과물을 얻어온다.


            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            textView.setText(result);


        }
    }
}
