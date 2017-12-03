package com.example.sjeong.pick.Saving;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sjeong.pick.R;
import com.example.sjeong.pick.RequestHttpURLConnection;
import com.labo.kaji.fragmentanimations.MoveAnimation;

/**
 * Created by mijin on 2017-12-01.
 */

public class SearchSimpleSavingFragment extends Fragment {
    private EditText searchtext;
    private Button base_detail;
    private int animation=0, time=0;

    String json;

    View view;

    private ProgressDialog progressDialog;
    public static int TIME_OUT = 1001;
    String url;
    ContentValues values;
    NetworkTask40 networkTask40;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_simple_saving,null);

        base_detail = (Button)view.findViewById(R.id.base_detail);
        base_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                SearchSimpleSavingFragment fragment = (SearchSimpleSavingFragment)getActivity().getSupportFragmentManager().findFragmentByTag("SearchSimpleSaving");
                SearchDetailSavingFragment detail = (SearchDetailSavingFragment)getActivity().getSupportFragmentManager().findFragmentByTag("SearchDetailSaving");
                if(detail==null) {
                    detail = new SearchDetailSavingFragment();
                    fragment.setAnimation("UP");
                    detail.setAnimation("UP");
                    ft.add(R.id.fragment_search, detail, "SearchDetailSaving");
                }
                else {
                    fragment.setAnimation("UP");
                    detail.setAnimation("UP");
                    ft.show(detail);
                }
                ft.hide(fragment);
                ft.commit();
            }
        });

        ImageButton back_to_login = (ImageButton) view.findViewById(R.id.back_to_login);
        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        searchtext = (EditText)view.findViewById(R.id.searchText);
        ImageButton searchbutton = (ImageButton)view.findViewById(R.id.searchbutton);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 기본검색
                String text = searchtext.getText().toString();
                Log.i("Test", text);

                if(text.isEmpty())
                    Toast.makeText(getActivity(), "상품명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                else{
                   //네트워크
                    url = " http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/getProductByName.php?";
                    values = new ContentValues();
                    values.put("keyword",searchtext.getText().toString());

                    networkTask40 = new NetworkTask40(url,values);
                    networkTask40.execute();
                }

            }
        });

        return view;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return MoveAnimation.create(animation, enter, time);
    }

    public void setAnimation(String type){ // 애니메이션 설정
        time = 1000;
        if(type.equals("UP"))
            animation = MoveAnimation.UP;
        else
            animation = MoveAnimation.DOWN;
    }

    public class NetworkTask40 extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        Handler mHandler = new Handler() {

            public void handleMessage(Message msg) {
                if (msg.what == TIME_OUT) { // 타임아웃이 발생하면
                    progressDialog.dismiss(); // ProgressDialog를 종료
                }
            }

        };

        public NetworkTask40(String url, ContentValues values) {

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

            json = result;
            progressDialog = ProgressDialog.show(getContext(), "상품 검색 중", "잠시만 기다려주세요.");
            mHandler.sendEmptyMessageDelayed(TIME_OUT, 2000);


            Intent intent = new Intent(view.getContext(), ProductActivity.class);
            intent.putExtra("json", result);
            intent.putExtra("prime_cond", Integer.parseInt("111111111111", 2) - Integer.parseInt("110000000000", 2));
            startActivity(intent);


        }
    }
}
