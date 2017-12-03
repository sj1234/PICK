package com.example.sjeong.pick;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {

    private Context context;
    private String data, url;
    private TextView name_textview;
    private Button item_homepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 상단바
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && view != null) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        setContentView(R.layout.activity_item);
        context = getApplicationContext();

        // 상품정보
        Intent intent = getIntent();
        data = intent.getStringExtra("data");

        // DB에서 상품 자세한 정보 받아오기
        GetItemHandler handler = new GetItemHandler(this);
        Log.i("GetItemDB test", data);
        GetItemDB test = new GetItemDB(data, handler);
        test.execute();

        item_homepage= (Button)findViewById(R.id.item_homepage);
        name_textview = (TextView) findViewById(R.id.item_name);

        ImageButton back_to_search = (ImageButton)findViewById(R.id.back_to_search);
        back_to_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageButton star = (ImageButton)findViewById(R.id.star);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton image = (ImageButton)v;
                Log.i("Test", "Click star button");
                Bitmap bitmap1 = ((BitmapDrawable)ContextCompat.getDrawable(context, R.drawable.full_star)).getBitmap();
                Bitmap bitmap3 = ((BitmapDrawable)(image.getDrawable())).getBitmap();

                // id 정보가져오기
                SharedPreferences preferences = context.getSharedPreferences("person", MODE_PRIVATE);
                String user_id = preferences.getString("id", "");

                // DB연동
                SetInterestHandler handler = new SetInterestHandler(getBaseContext(), v);
                SetInterestDB test;
                if(bitmap3.equals(bitmap1))
                    test = new SetInterestDB("delete", user_id, data, handler);
                else
                    test = new SetInterestDB("add", user_id, data, handler);
                test.execute();
            }
        });

        LinearLayout item_cal = (LinearLayout)view.findViewById(R.id.item_cal);
        item_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // id 정보가져오기
        SharedPreferences preferences = getBaseContext().getSharedPreferences("person", MODE_PRIVATE);
        String user_id = preferences.getString("id", "");

        // 관심상품인지 정보
        ConfirmInterestHandler interestHandler = new ConfirmInterestHandler(this, star);
        ConfirmInterestDB interestDB = new ConfirmInterestDB(user_id, data, interestHandler);
        interestDB.execute();

    }

    public void tableMaker(TableLayout table, JSONArray arr) {

        Log.i("table maker", "table maker");

        try {

            TableRow tr_title = new TableRow(this);
            tr_title.setGravity(Gravity.CENTER);

            TextView term_title = new TextView(this);
            term_title.setBackgroundColor(Color.rgb(233, 222, 124));
            term_title.setGravity(Gravity.CENTER);
            term_title.setText("가입기간");
            tr_title.addView(term_title);

            JSONObject arrObject = (JSONObject) arr.get(0);
            String cond = arrObject.get("RATE_COND").toString();
            String[] splites = cond.split("/");
            for (int j = 0; j < splites.length; j++) {
                if (!splites[j].equals("")) {
                    TextView cond_title = new TextView(this);
                    cond_title.setGravity(Gravity.CENTER);
                    switch(j){
                        case 0:
                            if(!splites[j].equals("선택")) {
                                cond_title.setText("이자");
                                tr_title.addView(cond_title);
                            }
                            break;
                        case 1:
                            cond_title.setText("가입");
                            tr_title.addView(cond_title);
                            break;
                        case 2:
                            cond_title.setText("총납입");
                            tr_title.addView(cond_title);
                            break;
                        case 3:
                            cond_title.setText("회전");
                            tr_title.addView(cond_title);
                            break;
                        case 4:
                            cond_title.setText("예치");
                            tr_title.addView(cond_title);
                            break;
                    }
                }
            }

            TextView rate_title = new TextView(this);
            rate_title.setGravity(Gravity.CENTER);
            rate_title.setText("금리");
            tr_title.addView(rate_title);

            table.addView(tr_title);

            for(int i=0; i<arr.length() ; i++) {
                arrObject = (JSONObject) arr.get(i);

                TableRow tr = new TableRow(this);
                tr.setGravity(Gravity.CENTER);

                TextView term = new TextView(this);
                term.setGravity(Gravity.CENTER);
                term.setText(arrObject.get("CONT_TERM").toString() + "~" + arrObject.get("CONT_TERM_END").toString());
                tr.addView(term);

                cond = arrObject.get("RATE_COND").toString();
                splites = cond.split("/");
                for (int j = 0; j < splites.length; j++) {
                    if (!splites[j].equals("")) {
                        TextView colum_cond = new TextView(this);
                        colum_cond.setGravity(Gravity.CENTER);

                        if (j == 0) {
                            switch (splites[j]) {
                                case "0":
                                    colum_cond.setText("만기일");
                                    tr.addView(colum_cond);
                                    break;
                                case "1":
                                    colum_cond.setText("연이자");
                                    tr.addView(colum_cond);
                                    break;
                                case "2":
                                    colum_cond.setText("6개월");
                                    tr.addView(colum_cond);
                                    break;
                                case "3":
                                    colum_cond.setText("3개월");
                                    tr.addView(colum_cond);
                                    break;
                                case "4":
                                    colum_cond.setText("2개월");
                                    tr.addView(colum_cond);
                                    break;
                                case "5":
                                    colum_cond.setText("월이자");
                                    tr.addView(colum_cond);
                                    break;
                                case "6":
                                    colum_cond.setText("선지급");
                                    tr.addView(colum_cond);
                                    break;
                                case "7":
                                    colum_cond.setText("원리금");
                                    tr.addView(colum_cond);
                                    break;

                            }
                        }
                        else if(j==2){
                            String[] sum = splites[j].replace("원이상", "").replace("원이하","").split(" ");
                            if(sum.length==1){
                                int sum_int = Integer.parseInt(sum[0]);

                                int base = 1;
                                while(true){
                                    if(sum_int%base==0)
                                        base *= 10;
                                    else {
                                        base /= 10;
                                        break;
                                    }
                                }

                                String a="원";
                                switch(base){
                                    case 1000 : a="천원"; sum_int/=base; break;
                                    case 10000 : a="만원"; sum_int/=base; break;
                                    case 100000 : a="십만원"; sum_int/=base; break;
                                    case 1000000 : a="백만원"; sum_int/=base; break;
                                    case 10000000 : a="천만원"; sum_int/=base; break;
                                    case 100000000 : a="억원"; sum_int/=base; break;
                                }
                                
                                if(splites[j].contains("원이상"))
                                    colum_cond.setText(sum_int+""+a+"이상");
                                else
                                    colum_cond.setText(sum_int+""+a+"이하");
                            }
                            else{
                                int sum_min = Integer.parseInt(sum[0]);
                                int sum_max = Integer.parseInt(sum[1]);

                                int base_min = 1;
                                int base_max = 1;
                                while(true){
                                    if(sum_min%base_min==0)
                                        base_min *= 10;
                                    if(sum_max%base_max==0)
                                        base_max *= 10;

                                    if((sum_max%base_max!=0) && (sum_min%base_min!=0)){
                                        base_min /= 10;
                                        base_max /= 10;
                                        break;
                                    }
                                }

                                String a_min="원", a_max = "원";
                                switch(base_min){
                                    case 1000 : a_min="천원"; sum_min/=base_min; break;
                                    case 10000 : a_min="만원"; sum_min/=base_min; break;
                                    case 100000 : a_min="십만원"; sum_min/=base_min; break;
                                    case 1000000 : a_min="백만원"; sum_min/=base_min; break;
                                    case 10000000 : a_min="천만원"; sum_min/=base_min; break;
                                    case 100000000 : a_min="억원"; sum_min/=base_min; break;
                                }

                                switch(base_max){
                                    case 1000 : a_max="천원"; sum_max/=base_max; break;
                                    case 10000 : a_max="만원"; sum_max/=base_max; break;
                                    case 100000 : a_max="십만원"; sum_max/=base_max; break;
                                    case 1000000 : a_max="백만원"; sum_max/=base_max; break;
                                    case 10000000 : a_max="천만원"; sum_max/=base_max; break;
                                    case 100000000 : a_max="억원"; sum_max/=base_max; break;
                                }

                                colum_cond.setText(sum_min+""+a_min+"~"+sum_max+""+a_max);
                            }
                            tr.addView(colum_cond);
                        }
                        else {
                            colum_cond.setText(splites[j]);
                            tr.addView(colum_cond);
                        }
                    }
                }

                TextView rate = new TextView(this);
                rate.setGravity(Gravity.CENTER);
                if(arrObject.get("CONT_RATE").toString().equals(arrObject.get("MAX_RATE").toString()))
                    rate.setText(arrObject.get("CONT_RATE").toString() + "%");
                else
                    rate.setText(arrObject.get("CONT_RATE").toString() + "%~\n " + arrObject.get("MAX_RATE").toString() + "%");
                tr.addView(rate);

                table.addView(tr);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class GetItemHandler extends Handler {

        private Context context;

        public GetItemHandler(Context context){
            this.context = context;
        }
         @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case 8: // 잘된경우
                    Log.i("Good", "Good result");

                    JSONObject jObject = null;
                    ArrayList<ItemDetail> itemdetails = new ArrayList<ItemDetail>();
                    try {
                        jObject = new JSONObject(msg.obj.toString());

                        name_textview.setText(jObject.get("NAME").toString());
                        itemdetails.add(new ItemDetail("은행", jObject.get("BANK").toString()));

                        String bank = jObject.get("BANK").toString();
                        url = "";
                        switch(bank){
                            case "NH농협은행": url= "https://smartmarket.nonghyup.com/"; break;
                            case "기업은행": url= "https://mybank.ibk.co.kr/uib/jsp/index.jsp"; break;
                            case "국민은행": url= "https://obank.kbstar.com/quics?page=C016528"; break;
                            case "우리은행": url= "https://spot.wooribank.com/pot/Dream?withyou=po"; break;
                            case "KEB하나은행": url= "https://www.kebhana.com/cont/mall/index.jsp"; break;
                            case "KDB산업은행": url= "https://wbiz.kdb.co.kr/wb/simpleJsp.do"; break;
                            case "경남은행": url= "https://www.knbank.co.kr/ib20/mnu/FPM000000000001"; break;
                            case "광주은행": url= "http://www.kjbank.com/banking/index.jsp"; break;
                            case "대구은행": url= "https://www.dgb.co.kr/com_ebz_fpm_sub_main.jsp"; break;
                            case "부산은행": url= "https://www.busanbank.co.kr/ib20/mnu/FPM00001"; break;
                            case "수협은행": url= "https://mybank.ibk.co.kr/uib/jsp/index.jsp"; break;
                            case "스탠다드차타드은행": url= "http://www.standardchartered.co.kr/np/kr/ProductMall.jsp?ptfrm=HIN.KOR.INTROPC.topmenu1"; break;
                            case "씨티은행": url= "https://www.citibank.co.kr/MndMdtrMain0100.act?MENU_TYPE=pre&MENU_C_SQNO=M0_000020"; break;
                            case "우체국예금": url= "https://www.epostbank.go.kr/"; break;
                            case "전북은행": url= "https://www.jbbank.co.kr/"; break;
                            case "제주은행": url= "https://www.e-jejubank.com/HomeFinanceMall.do"; break;
                            case "케이뱅크": url= "https://www.kbanknow.com/ib20/mnu/FPMMAN000000#n"; break;
                        }

                        item_homepage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                            }
                        });

                        itemdetails.add(new ItemDetail("가입방법", jObject.get("J_WAY").toString().replace("<br/>", ", ")));
                        itemdetails.add(new ItemDetail("가입한도", jObject.get("J_LIMIT").toString()+"원 ~ "+jObject.get("M_LIMIT").toString()+"원"));
                        if(jObject.get("CS").toString().equals("0"))
                            itemdetails.add(new ItemDetail("단리복리", "단리"));
                        else
                            itemdetails.add(new ItemDetail("단리복리", "복리"));
                        itemdetails.add(new ItemDetail("만기이율", jObject.get("AFTER").toString().replace("<br/>", "\n")));
                        itemdetails.add(new ItemDetail("우대조건", jObject.get("PRIME").toString().replace("<br/>", "\n")));
                        itemdetails.add(new ItemDetail("기타", jObject.get("ETC").toString().replace("<br/>", "\n")));

                        if(jObject.get("RATE")!=null){
                            JSONArray arr = (JSONArray) jObject.get("RATE");
                            TableLayout rate_table = (TableLayout)findViewById(R.id.rate_table);
                            tableMaker(rate_table, arr);
                        }

                        ListView listView = (ListView)findViewById(R.id.list_item_detail);
                        ItemDetailAdapter listAdapter = new ItemDetailAdapter(context, itemdetails);
                        listView.setAdapter(listAdapter);

                        // 리스트뷰 화면에 꽉 차는 높이가 되도록
                        ViewGroup.LayoutParams params = listView.getLayoutParams();
                        int height = 0;
                        for (int size = 0; size < listAdapter.getCount(); size++) {
                            View listItem = listAdapter.getView(size, null, listView);
                            listItem.measure(0,0);
                            height += listItem.getMeasuredHeight();
                        }
                        params.height = height + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
                        Log.i("height", height+"");
                        listView.setLayoutParams(params);
                        listView.requestLayout();

                        // 스크롤 맨위로 보내기
                        ScrollView item_scrollview = (ScrollView)findViewById(R.id.item_scrollview);
                        //item_scrollview.fullScroll(ScrollView.FOCUS_UP);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 9: // 에러 문제 (발생하는 경우 코드상에는 없음)
                    break;
            }
         }
    }

    class SetInterestHandler extends Handler {

        private Context context;
        private ImageButton item;

        public SetInterestHandler(Context context, View item){
            this.context = context;
            this.item = (ImageButton)item;
        }
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.obj.toString().equals("OK")){
                Log.i("Test", "Click star button");
                Bitmap bitmap1 = ((BitmapDrawable)ContextCompat.getDrawable(context, R.drawable.full_star)).getBitmap();
                Bitmap bitmap3 = ((BitmapDrawable)(item.getDrawable())).getBitmap();
                Drawable full = ContextCompat.getDrawable(context, R.drawable.full_star);
                Drawable empty = ContextCompat.getDrawable(context, R.drawable.empty_star);

                if(bitmap3.equals(bitmap1))
                    item.setImageDrawable(empty);
                else
                    item.setImageDrawable(full);
            }
            else{
                // 에러가 났음을 토스 메세지로!
                Toast.makeText(context, "오류가 났습니다.\n오류반복시 건의하여주십시오.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class ConfirmInterestHandler extends Handler {

        private Context context;
        private ImageButton item;

        public ConfirmInterestHandler(Context context, ImageButton item){
            this.context = context;
            this.item = item;
        }
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.obj.toString().equals("YES")){
                Drawable full = ContextCompat.getDrawable(context, R.drawable.full_star);
                item.setImageDrawable(full);
            }
            else if(!msg.obj.toString().equals("NO")){
                Log.i("Interest Star", "Interest Star Error");
            }
        }
    }
}

// code 통해 아이템 가져오기
class GetItemDB extends AsyncTask<Void, Integer, Void> {

    private String data, string;
    private ItemActivity.GetItemHandler handler;

    public GetItemDB(String code, ItemActivity.GetItemHandler handler){
        string = "u_code="+code+"";
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/GetItemDetail2.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(conn !=null){
                conn.setConnectTimeout(10000);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                OutputStream output = conn.getOutputStream();
                output.write(string.getBytes("UTF-8"));
                output.flush();
                output.close();

                InputStream input = null;
                BufferedReader in = null;

                input = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(input), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null ) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                Log.i("Result", data);
            }
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        Message msg = new Message();
        msg.what=8;
        msg.obj=data;
        handler.sendMessage(msg);
    }
}

// 관심상품 설정, 삭제
class SetInterestDB extends AsyncTask<Void, Integer, Void> {

    private String data, string;
    private ItemActivity.SetInterestHandler handler;

    public SetInterestDB(String type, String user_id, String code, ItemActivity.SetInterestHandler handler){
        if(type.equals("delete"))// 관심목록 삭제
            string = "u_query=DELETE FROM INTEREST WHERE USR_ID='"+user_id+"' AND PROD_CODE='"+code+"'";
        else// 관심목록 추가
            string = "u_query=INSERT INTO INTEREST VALUES ('"+user_id+"', '"+code+"')";
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Void... params) {

        Log.i("SetInterest DB", string);
        try {
            URL url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/SetDepContent.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(conn !=null){
                conn.setConnectTimeout(10000);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                OutputStream output = conn.getOutputStream();
                output.write(string.getBytes("UTF-8"));
                output.flush();
                output.close();

                InputStream input = null;
                BufferedReader in = null;

                input = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(input), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null ) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                Log.i("Result", data);
            }
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Message msg = new Message();
        msg.obj=data;
        handler.sendMessage(msg);
    }
}

// 관심상품 확인
class ConfirmInterestDB extends AsyncTask<Void, Integer, Void> {

    private String data, string;
    private ItemActivity.ConfirmInterestHandler handler;

    public ConfirmInterestDB(String user_id, String code, ItemActivity.ConfirmInterestHandler handler){
        string = "u_query=SELECT USR_ID FROM INTEREST WHERE USR_ID='"+user_id+"' AND PROD_CODE='"+code+"'";
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Void... params) {

        Log.i("SetInterest DB", string);
        try {
            URL url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/ConfirmInterest.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(conn !=null){
                conn.setConnectTimeout(10000);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                OutputStream output = conn.getOutputStream();
                output.write(string.getBytes("UTF-8"));
                output.flush();
                output.close();

                InputStream input = null;
                BufferedReader in = null;

                input = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(input), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null ) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                Log.i("Result", data);
            }
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Message msg = new Message();
        msg.obj=data;
        handler.sendMessage(msg);
    }
}
