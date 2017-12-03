package com.example.sjeong.pick.Saving;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sjeong.pick.Calculator.CalculatorActivity;
import com.example.sjeong.pick.GoalActivity;
import com.example.sjeong.pick.ItemDetail;
import com.example.sjeong.pick.ItemDetailAdapter;
import com.example.sjeong.pick.R;
import com.example.sjeong.pick.RequestHttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mijin on 2017-11-20.
 */

public class ProductDetailActivity extends AppCompatActivity {

    private TextView name_textview, item_summary;
    private Button item_homepage;
    String url;
    ContentValues values;
    NetworkTask2 networkTask2;
    String usr_id;
    int prod_code;
    MenuItem item;
    ImageButton star;
    boolean flag1 = false;

    private ProgressDialog progressDialog;
    public static int TIME_OUT = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        progressDialog = ProgressDialog.show(ProductDetailActivity.this, "상품 검색 중", "잠시만 기다려주세요.");
        mHandler.sendEmptyMessageDelayed(TIME_OUT, 2000);

        SharedPreferences preferences = this.getSharedPreferences("person", MODE_PRIVATE);
        usr_id = preferences.getString("id", null);

        Intent intent = getIntent();
        prod_code = intent.getIntExtra("prod_code",0);

        url = "http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/getProductDetail.php?";
        values = new ContentValues();
        values.put("prod_code",prod_code);

        networkTask2 = new NetworkTask2(url,values);
        networkTask2.execute();

        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && view != null) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        ImageButton back_to_search = (ImageButton) findViewById(R.id.back_to_search);
        back_to_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        star = (ImageButton) findViewById(R.id.star);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                url = "http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/deleteOrAddInterest.php?";
                values = new ContentValues();
                values.put("usr_id",usr_id);
                values.put("prod_code",prod_code);

                if(flag1==true) values.put("flag","delete");
                else values.put("flag","add");

                networkTask2 = new NetworkTask2(url, values);
                networkTask2.execute();
            }
        });

        url = "http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/getInterest.php?";
        values = new ContentValues();
        values.put("usr_id",usr_id);
        values.put("prod_code",prod_code);

        networkTask2 = new NetworkTask2(url, values);
        networkTask2.execute();

        item_homepage= (Button)findViewById(R.id.item_homepage);
        name_textview = (TextView) findViewById(R.id.item_name);
        item_summary = (TextView) findViewById(R.id.item_summary);


        LinearLayout goCalculator = (LinearLayout) findViewById(R.id.goCalculator);
        goCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, CalculatorActivity.class);
                startActivity(intent);
                finish();
            }
        });

        LinearLayout goGoal = (LinearLayout) findViewById(R.id.goGoal);
        goGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, GoalActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == TIME_OUT) { // 타임아웃이 발생하면
                progressDialog.dismiss(); // ProgressDialog를 종료
            }
        }

    };



    public void tableMaker(TableLayout table, JSONArray arr) {

        Log.i("table maker", "table maker");

        try {
            Drawable background = ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_light_black);
            TableRow tr_title = new TableRow(this);
            tr_title.setGravity(Gravity.CENTER);

            TextView cont_term = new TextView(this);
            cont_term.setBackground(background);
            cont_term.setGravity(Gravity.CENTER);
            cont_term.setText("가입기간");
            tr_title.addView(cont_term);

            TextView policy_no = new TextView(this);
            policy_no.setGravity(Gravity.CENTER);
            policy_no.setText("분류");
            tr_title.addView(policy_no);

            TextView policy_id = new TextView(this);
            policy_id.setGravity(Gravity.CENTER);
            policy_id.setText("조건");
            tr_title.addView(policy_id);

            TextView intr = new TextView(this);
            intr.setGravity(Gravity.CENTER);
            intr.setText("금리");
            tr_title.addView(intr);

            table.addView(tr_title);


            for(int i=0; i<arr.length() ; i++) {

                JSONObject json = (JSONObject) arr.get(i);

                TableRow tr = new TableRow(this);
                tr.setGravity(Gravity.CENTER);

                TextView term = new TextView(this);
                term.setGravity(Gravity.CENTER);
                term.setText(String.valueOf(json.getInt("cont_term")));
                tr.addView(term);

                TextView no = new TextView(this);
                no.setGravity(Gravity.CENTER);
                no.setText(policy_no(json.getInt("policy_no")));
                tr.addView(no);

                TextView id = new TextView(this);
                id.setGravity(Gravity.CENTER);
                id.setText(policy_id(json.getInt("policy_no"), json.getInt("policy_id")));
                tr.addView(id);

                TextView intrr = new TextView(this);
                intrr.setGravity(Gravity.CENTER);
                intrr.setText(String.valueOf(json.getDouble("intr")));
                tr.addView(intrr);

                table.addView(tr);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String policy_no(int policy_no){
        switch (policy_no){
            case 1 : return "납입방법";
            case 2 : return "이자지급방법";
            case 3 : return "가입방법";
            default: return "";
        }
    }

    public String policy_id(int policy_no, int policy_id){
        switch (policy_no){
            case 1 :
                switch (policy_id){
                    case 0 : return "자유적립식";
                    case 1 : return "정액적립식";
                    default: return "";
                }
            case 2 :
                switch (policy_id){
                    case 0: return "만기일시지급식";
                    case 1: return "연이자지급식";
                    case 2: return "6개월이자지급식";
                    case 3: return "3개월이자지급식";
                    case 4: return "2개월이자지급식";
                    case 5: return "월이자지급식";
                    case 6: return "선지급식";
                    case 7: return "원리금 균등";
                    default: return "";
                }
            case 3 :
                switch (policy_id){
                    case 0 : return "영업점";
                    case 1 : return "모바일뱅킹";
                    case 2 : return "인터넷뱅킹";
                    case 3 : return "콜센터";
                    default: return "";
                }
            default: return "";
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


            //String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            String result = requestHttpURLConnection.request(url, values);
            // 해당 URL로 부터 결과물을 얻어온다.


            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String ex_intr_desc, can_intr_desc, prime_desc, prod_warning, prod_desc;
            String bank;
            int prime_cond, join_target, join_way; //max_cont_term
            String sprime_cond, sjoin_target, sjoin_way;
            String prod_name;
            Double min_intr, max_prime_intr;
            int prod_type, com_sim, ori_pay_method, rat_pay_method, min_join_limit, max_save_limit, min_month_limit, max_month_limit;
            String sprod_type, scom_sim, sori_pay_method, srat_pay_method, month_limit;
            boolean flag;
            try {
                if (result != null) {
                    if(result.trim().equals("success")){
                        star.setImageResource(R.drawable.full_star);
                        flag1 = true;
                    }else if(result.trim().equals("successno")){
                        star.setImageResource(R.drawable.empty_star);
                        flag1 = false;
                    }else if(result.trim().equals("success2")){
                        if(flag1==true) {
                            star.setImageResource(R.drawable.empty_star);
                            flag1 = false;
                        }else{
                            star.setImageResource(R.drawable.full_star);
                            flag1 = true;
                        }
                    }else {
                        JSONObject json = new JSONObject(result);

                        JSONObject sd = (JSONObject) json.get("sd");
                        ex_intr_desc = sd.getString("ex_intr_desc");
                        can_intr_desc = sd.getString("can_intr_desc");
                        prime_desc = sd.getString("prime_desc");
                        prod_warning = sd.getString("prod_warning");
                        prod_desc = sd.getString("prod_desc");

                        JSONObject sms = (JSONObject) json.get("sms");
                        bank = sms.getString("bank");
                        prime_cond = sms.getInt("prime_cond");
                        sprime_cond = "";
                        flag = false;
                        if ((prime_cond & (int) Math.pow(2, 11)) == (int) Math.pow(2, 11)) {
                            sprime_cond += "비과세";
                            flag = true;
                        }
                        if ((prime_cond & (int) Math.pow(2, 10)) == (int) Math.pow(2, 10)) {
                            if (flag) sprime_cond += ", ";
                            sprime_cond += "변동금리";
                            flag = true;
                        }
                        if ((prime_cond & (int) Math.pow(2, 9)) == (int) Math.pow(2, 9)) {
                            if (flag) sprime_cond += ", ";
                            sprime_cond += "보유/잔액";
                            flag = true;
                        }
                        if ((prime_cond & (int) Math.pow(2, 8)) == (int) Math.pow(2, 8)) {
                            if (flag) sprime_cond += ", ";
                            sprime_cond += "실적/결제/대금";
                            flag = true;
                        }
                        if ((prime_cond & (int) Math.pow(2, 7)) == (int) Math.pow(2, 7)) {
                            if (flag) sprime_cond += ", ";
                            sprime_cond += "신규/가입/첫거래";
                            flag = true;
                        }
                        if ((prime_cond & (int) Math.pow(2, 6)) == (int) Math.pow(2, 6)) {
                            if (flag) sprime_cond += ", ";
                            sprime_cond += "모바일/온라인/비대면";
                            flag = true;
                        }
                        if ((prime_cond & (int) Math.pow(2, 5)) == (int) Math.pow(2, 5)) {
                            if (flag) sprime_cond += ", ";
                            sprime_cond += "이체관련";
                            flag = true;
                        }
                        if ((prime_cond & (int) Math.pow(2, 4)) == (int) Math.pow(2, 4)) {
                            if (flag) sprime_cond += ", ";
                            sprime_cond += "동시가입";
                            flag = true;
                        }
                        if ((prime_cond & (int) Math.pow(2, 3)) == (int) Math.pow(2, 3)) {
                            if (flag) sprime_cond += ", ";
                            sprime_cond += "이벤트/미션";
                            flag = true;
                        }
                        if ((prime_cond & (int) Math.pow(2, 2)) == (int) Math.pow(2, 2)) {
                            if (flag) sprime_cond += ", ";
                            sprime_cond += "군인/특정직급자";
                            flag = true;
                        }
                        if ((prime_cond & (int) Math.pow(2, 1)) == (int) Math.pow(2, 1)) {
                            if (flag) sprime_cond += ", ";
                            sprime_cond += "사회적배려/다문화";
                            flag = true;
                        }
                        if ((prime_cond & (int) Math.pow(2, 0)) == (int) Math.pow(2, 0)) {
                            if (flag) sprime_cond += ", ";
                            sprime_cond += "나이/연령";
                            flag = true;
                        }

                        join_target = sms.getInt("join_target");
                        sjoin_target = "";
                        flag = false;
                        if ((join_target & (int) Math.pow(2, 2)) == (int) Math.pow(2, 2)) {
                            sjoin_target += "개인";
                            flag = true;
                        }
                        if ((join_target & (int) Math.pow(2, 1)) == (int) Math.pow(2, 1)) {
                            if (flag) sjoin_target += ", ";
                            sjoin_target += "단체";
                            flag = true;
                        }
                        if ((join_target & (int) Math.pow(2, 0)) == (int) Math.pow(2, 0)) {
                            if (flag) sjoin_target += ", ";
                            sjoin_target += "사업자/법인";
                            flag = true;
                        }

                        join_way = sms.getInt("join_way");
                        sjoin_way = "";
                        flag = false;
                        if ((join_way & (int) Math.pow(2, 3)) == (int) Math.pow(2, 3)) {
                            sjoin_way += "영업점";
                            flag = true;
                        }
                        if ((join_way & (int) Math.pow(2, 2)) == (int) Math.pow(2, 2)) {
                            if (flag) sjoin_way += ", ";
                            sjoin_way += "모바일뱅킹";
                            flag = true;
                        }
                        if ((join_way & (int) Math.pow(2, 1)) == (int) Math.pow(2, 1)) {
                            if (flag) sjoin_way += ", ";
                            sjoin_way += "인터넷뱅킹";
                            flag = true;
                        }
                        if ((join_way & (int) Math.pow(2, 0)) == (int) Math.pow(2, 0)) {
                            if (flag) sjoin_way += ", ";
                            sjoin_way += "콜센터";
                            flag = true;
                        }

                        JSONObject sos = (JSONObject) json.get("sos");
                        prod_name = sos.getString("prod_name");
                        min_intr = sos.getDouble("min_intr");
                        max_prime_intr = sos.getDouble("max_prime_intr");
                        prod_type = sos.getInt("prod_type");
                        sprod_type = "";
                        flag = false;
                        if ((prod_type & (int) Math.pow(2, 1)) == (int) Math.pow(2, 1)) {
                            sprod_type += "자유적립식";
                            flag = true;
                        }
                        if ((prod_type & (int) Math.pow(2, 0)) == (int) Math.pow(2, 0)) {
                            if (flag) sprod_type += ", ";
                            sprod_type += "정액적립식";
                        }

                        com_sim = sos.getInt("com_sim");
                        scom_sim = "";
                        if ((com_sim & (int) Math.pow(2, 0)) == (int) Math.pow(2, 0)) {
                            scom_sim += "복리";
                        } else {
                            scom_sim += "단리";
                        }

                        ori_pay_method = sos.getInt("ori_pay_method");
                        sori_pay_method = "";
                        flag = false;
                        if ((ori_pay_method & (int) Math.pow(2, 1)) == (int) Math.pow(2, 1)) {
                            sori_pay_method += "만기일시지급식";
                            flag = true;
                        }
                        if ((ori_pay_method & (int) Math.pow(2, 0)) == (int) Math.pow(2, 0)) {
                            if (flag) sori_pay_method += ", ";
                            sori_pay_method += "연이자지급식";
                        }

                        rat_pay_method = sos.getInt("rat_pay_method");
                        srat_pay_method = "";
                        flag = false;
                        if ((rat_pay_method & (int) Math.pow(2, 1)) == (int) Math.pow(2, 1)) {
                            srat_pay_method += "만기일시지급식";
                            flag = true;
                        }
                        if ((rat_pay_method & (int) Math.pow(2, 0)) == (int) Math.pow(2, 0)) {
                            if (flag) srat_pay_method += ", ";
                            srat_pay_method += "연이자지급식";
                        }

                        if(!sos.get("min_join_limit").toString().equals("")) min_join_limit = sos.getInt("min_join_limit");
                        else min_join_limit=0;
                        if(!sos.get("max_save_limit").toString().equals("")) max_save_limit = sos.getInt("max_save_limit");
                        else max_save_limit=0;
                        if(!sos.get("min_month_limit").toString().equals("")) min_month_limit = sos.getInt("min_month_limit");
                        else min_month_limit=0;
                        if(!sos.get("max_month_limit").toString().equals("")) max_month_limit = sos.getInt("max_month_limit");
                        else max_month_limit=65535;


                        if (max_month_limit != 0 || max_month_limit != 65535)
                            month_limit = min_month_limit * 1000 + "원 ~" + max_month_limit * 1000 + "원";
                        else month_limit = min_month_limit * 1000 + "원 ~";

                        JSONArray si = (JSONArray) json.get("si");
                        if (si != null) {
                            TableLayout rate_table = (TableLayout) findViewById(R.id.rate_table);
                            tableMaker(rate_table, si);
                        }

                        ArrayList<ItemDetail> itemdetails = new ArrayList<ItemDetail>();

                        name_textview.setText(prod_name);
                        item_summary.setText(prod_desc);
                        itemdetails.add(new ItemDetail("은행", bank));

                        url = "";
                        switch (bank) {
                            case "NH농협은행":
                                url = "https://smartmarket.nonghyup.com/";
                                break;
                            case "기업은행":
                                url = "https://mybank.ibk.co.kr/uib/jsp/index.jsp";
                                break;
                            case "국민은행":
                                url = "https://obank.kbstar.com/quics?page=C016528";
                                break;
                            case "우리은행":
                                url = "https://spot.wooribank.com/pot/Dream?withyou=po";
                                break;
                            case "KEB하나은행":
                                url = "https://www.kebhana.com/cont/mall/index.jsp";
                                break;
                            case "KDB산업은행":
                                url = "https://wbiz.kdb.co.kr/wb/simpleJsp.do";
                                break;
                            case "경남은행":
                                url = "https://www.knbank.co.kr/ib20/mnu/FPM000000000001";
                                break;
                            case "광주은행":
                                url = "http://www.kjbank.com/banking/index.jsp";
                                break;
                            case "대구은행":
                                url = "https://www.dgb.co.kr/com_ebz_fpm_sub_main.jsp";
                                break;
                            case "부산은행":
                                url = "https://www.busanbank.co.kr/ib20/mnu/FPM00001";
                                break;
                            case "수협은행":
                                url = "https://mybank.ibk.co.kr/uib/jsp/index.jsp";
                                break;
                            case "스탠다드차타드은행":
                                url = "http://www.standardchartered.co.kr/np/kr/ProductMall.jsp?ptfrm=HIN.KOR.INTROPC.topmenu1";
                                break;
                            case "씨티은행":
                                url = "https://www.citibank.co.kr/MndMdtrMain0100.act?MENU_TYPE=pre&MENU_C_SQNO=M0_000020";
                                break;
                            case "우체국예금":
                                url = "https://www.epostbank.go.kr/";
                                break;
                            case "전북은행":
                                url = "https://www.jbbank.co.kr/";
                                break;
                            case "제주은행":
                                url = "https://www.e-jejubank.com/HomeFinanceMall.do";
                                break;
                            case "케이뱅크":
                                url = "https://www.kbanknow.com/ib20/mnu/FPMMAN000000#n";
                                break;
                        }

                        item_homepage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                            }
                        });


                        itemdetails.add(new ItemDetail("상품유형", sprod_type));
                        itemdetails.add(new ItemDetail("원금지급방식", sori_pay_method));
                        itemdetails.add(new ItemDetail("이자지급방식", srat_pay_method));
                        itemdetails.add(new ItemDetail("가입방법", sjoin_way));
                        if(min_join_limit!=0) itemdetails.add(new ItemDetail("가입최소한도", String.valueOf(min_join_limit) + "원"));
                        else itemdetails.add(new ItemDetail("가입최소한도", "제한없음"));
                        if(max_save_limit!=0) itemdetails.add(new ItemDetail("최대납입한도", String.valueOf(max_save_limit * 1000000) + "원"));
                        else itemdetails.add(new ItemDetail("최대납입한도", "제한없음"));
                        itemdetails.add(new ItemDetail("월납입한도", month_limit));
                        itemdetails.add(new ItemDetail("단리복리", scom_sim));
                        itemdetails.add(new ItemDetail("우대조건", sprime_cond));
                        itemdetails.add(new ItemDetail("만기이율", ex_intr_desc));
                        itemdetails.add(new ItemDetail("중도해지이율", can_intr_desc));
                        itemdetails.add(new ItemDetail("우대금리", prime_desc.replace("=======>"," - ")));
                        itemdetails.add(new ItemDetail("기타", prod_warning));

                        ListView listView = (ListView) findViewById(R.id.list_item_detail);
                        ItemDetailAdapter listAdapter = new ItemDetailAdapter(getApplicationContext(), itemdetails);
                        listView.setAdapter(listAdapter);

                        // 리스트뷰 화면에 꽉 차는 높이가 되도록
                        ViewGroup.LayoutParams params = listView.getLayoutParams();
                        int height = 0;
                        for (int size = 0; size < listAdapter.getCount(); size++) {
                            View listItem = listAdapter.getView(size, null, listView);
                            listItem.measure(0, 0);
                            height += listItem.getMeasuredHeight();
                        }
                        params.height = height + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
                        Log.i("height", height + "");
                        listView.setLayoutParams(params);
                        listView.requestLayout();
                    }
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
