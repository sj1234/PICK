package com.example.sjeong.pick;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.sjeong.pick.Calculator.CalculatorActivity;
import com.example.sjeong.pick.My.MyActivity;
import com.example.sjeong.pick.Saving.SavingSearchActivity;
import com.example.sjeong.pick.Setting.SettingActivity;
import com.lsjwzh.widget.recyclerviewpager.LoopRecyclerViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.sjeong.pick.R.id.menu_logout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 상단바 색 변경
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#d4e9fa"));

        setContentView(R.layout.activity_main);

        // RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        String url = "http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/hot.php?";
        NetworkTask21 networkTask21 = new NetworkTask21(url,new ContentValues());
        networkTask21.execute();


        // 로그아웃 버튼
        ImageButton menu_logout = (ImageButton) findViewById(R.id.menu_logout);
        menu_logout.setOnClickListener(this);
        // 설정 버튼
        ImageButton menu_setting = (ImageButton) findViewById(R.id.menu_setting);
        menu_setting.setOnClickListener(this);
        // 예금
        LinearLayout menu_dep = (LinearLayout)findViewById(R.id.menu_dep);
        menu_dep.setOnClickListener(this);
        // 적금
        LinearLayout menu_sav = (LinearLayout)findViewById(R.id.menu_sav);
        menu_sav.setOnClickListener(this);
        // 계산기
        LinearLayout menu_cac = (LinearLayout)findViewById(R.id.menu_cac);
        menu_cac.setOnClickListener(this);
        // 목표
        LinearLayout menu_goal = (LinearLayout)findViewById(R.id.menu_goal);
        menu_goal.setOnClickListener(this);
        // 관심
        LinearLayout menu_int = (LinearLayout)findViewById(R.id.menu_int);
        menu_int.setOnClickListener(this);
        // faq
        LinearLayout menu_faq = (LinearLayout)findViewById(R.id.menu_faq);
        menu_faq.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case menu_logout: // 로그아웃
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("LOGOUT")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                // 자동로그인 정보 및 로그인 ID 정보 지우기
                                SharedPreferences preferences = getSharedPreferences("person", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.remove("auto_login");
                                editor.remove("id");
                                editor.commit();

                                // main 화면으로 이동
                                Intent main = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(main);
                                finish();
                            }
                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case R.id.menu_setting: // 설정
                Intent setting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(setting);
                break;
            case R.id.menu_dep: // 예금
                Intent dep = new Intent(MainActivity.this, SearchDepActivity.class);
                startActivity(dep);
                break;
            case R.id.menu_sav: // 적금
                Intent sav = new Intent(MainActivity.this, SavingSearchActivity.class);
                startActivity(sav);
                break;
            case R.id.menu_cac: // 계산기
                Intent cac = new Intent(MainActivity.this, CalculatorActivity.class);
                startActivity(cac);
                break;
            case R.id.menu_goal: // 목표
                Intent goal = new Intent(MainActivity.this, GoalActivity.class);
                startActivity(goal);
                break;
            case R.id.menu_int: // 관심
                Intent interest = new Intent(MainActivity.this, MyActivity.class);
                startActivity(interest);
                break;
            case R.id.menu_faq: // faq
                Intent faq = new Intent(MainActivity.this, FAQActivity.class);
                startActivity(faq);
                break;
        }
    }


    public class NetworkTask21 extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask21(String url, ContentValues values) {

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


            ArrayList<Hot> Hots = new ArrayList<Hot>();
            int ranking;
            try {
                if(result!=null) {
                    JSONArray jarr = new JSONArray(result);
                    for(int i=0;i<jarr.length();i++){

                        JSONObject json = (JSONObject) jarr.get(i);
                        if(i==0) ranking = R.drawable.gold;
                        else if(i==1) ranking = R.drawable.silver;
                        else ranking = R.drawable.metal;

                        Hots.add(new Hot(json.getString("prod_name"),json.getDouble("min"),json.getDouble("max"),R.drawable.ic_menu_01,ranking));

                    }

                    final LoopRecyclerViewPager viewPager = (LoopRecyclerViewPager) findViewById(R.id.viewPager);
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL,false);

                    MyAdapter myAdapter = new MyAdapter(Hots);
                    viewPager.setLayoutManager(layoutManager);
                    viewPager.setTriggerOffset(0.15f);
                    viewPager.setAdapter(myAdapter);
                    viewPager.setFlingFactor(0.25f);


                    viewPager.setHasFixedSize(true);
                    viewPager.setLongClickable(true);

                    viewPager.setOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {

                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + viewPager.getFirstVisiblePosition());
                            int childCount = viewPager.getChildCount();
                            int width = viewPager.getChildAt(0).getWidth();
                            int padding = (viewPager.getWidth() - width) / 2;

                            for (int j = 0; j < childCount; j++) {
                                View v = recyclerView.getChildAt(j);

                                float rate = 0;
                                if (v.getLeft() <= padding) {
                                    if (v.getLeft() >= padding - v.getWidth()) {
                                        rate = (padding - v.getLeft()) * 1f / v.getWidth();
                                    } else {
                                        rate = 1;
                                    }
                                    v.setScaleY(1 - rate * 0.1f);
                                } else {

                                    if (v.getLeft() <= recyclerView.getWidth() - padding) {
                                        rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                                    }
                                    v.setScaleY(0.9f + rate * 0.1f);
                                }
                            }
                        }
                    });

                    viewPager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            if (viewPager.getChildCount() < 3) {
                                if (viewPager.getChildAt(1) != null) {
                                    View v1 = viewPager.getChildAt(1);
                                    v1.setScaleY(0.9f);
                                }
                            } else {
                                if (viewPager.getChildAt(0) != null) {
                                    View v0 = viewPager.getChildAt(0);
                                    v0.setScaleY(0.9f);
                                }
                                if (viewPager.getChildAt(2) != null) {
                                    View v2 = viewPager.getChildAt(2);
                                    v2.setScaleY(0.9f);
                                }
                            }

                        }
                    });


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
