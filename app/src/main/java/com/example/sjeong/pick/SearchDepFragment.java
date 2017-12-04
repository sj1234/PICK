package com.example.sjeong.pick;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.labo.kaji.fragmentanimations.MoveAnimation;

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

public class SearchDepFragment extends Fragment {
    private EditText searchtext;
    private Button base_detail;
    private int animation=0, time=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_dep,null);

        base_detail = (Button)view.findViewById(R.id.base_detail);
        base_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                SearchDepFragment fragment = (SearchDepFragment)getActivity().getSupportFragmentManager().findFragmentByTag("SearchDep");
                SearchDepDetailFragment detail = (SearchDepDetailFragment)getActivity().getSupportFragmentManager().findFragmentByTag("SearchDepDetail");
                if(detail==null) {
                    detail = new SearchDepDetailFragment();
                    fragment.setAnimation("UP");
                    detail.setAnimation("UP");
                    ft.add(R.id.fragment_search, detail, "SearchDepDetail");
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

        searchtext = (EditText)view.findViewById(R.id.searchText);
        searchtext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction()==KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    // 기본검색
                    SearchHandler handler = new SearchHandler();
                    String text = searchtext.getText().toString();
                    Log.i("Test", text);

                    if(text.isEmpty())
                        Toast.makeText(getActivity(), "상품명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    else{
                        String sql = "SELECT A.PROD_CODE AS CODE, MIN(A.CONT_RATE) AS MIN, MAX(MAX_RATE) AS MAX, B.PROD_NAME AS NAME, B.BANK AS BANK FROM DEP_RATE A INNER JOIN (SELECT PROD_CODE, PROD_NAME, BANK FROM DEPOSITE WHERE PROD_NAME LIKE '%"+ text+"%') B ON A.PROD_CODE = B.PROD_CODE GROUP BY A.PROD_CODE";
                        SearchDB test = new SearchDB(sql, handler);
                        test.execute();
                    }
                    return true;
                }
                return false;
            }
        });

        ImageButton searchbutton = (ImageButton)view.findViewById(R.id.searchbutton);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchHandler handler = new SearchHandler();

                // 기본검색
                String text = searchtext.getText().toString();
                Log.i("Test", text);

                if(text.isEmpty())
                    Toast.makeText(getActivity(), "상품명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                else{
                    String sql = "SELECT A.PROD_CODE AS CODE, MIN(A.CONT_RATE) AS MIN, MAX(MAX_RATE) AS MAX, B.PROD_NAME AS NAME, B.BANK AS BANK FROM DEP_RATE A INNER JOIN (SELECT PROD_CODE, PROD_NAME, BANK FROM DEPOSITE WHERE PROD_NAME LIKE '%"+ text+"%') B ON A.PROD_CODE = B.PROD_CODE GROUP BY A.PROD_CODE";
                    SearchDB test = new SearchDB( sql, handler);
                    test.execute();
                }

            }
        });

        ImageButton back_to_login = (ImageButton)view.findViewById(R.id.back_to_login);
        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
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

    class SearchHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);

            Intent intent = new Intent(getActivity(), ItemSearchActivity.class);
            intent.putExtra("type", "1,2");
            intent.putExtra("data", msg.obj.toString());
            startActivity(intent);
        }
    }
}

// 검색
class SearchDB extends AsyncTask<Void, Integer, Void> {

    private String data, string;
    private SearchDepFragment.SearchHandler handler;

    public SearchDB(String string, SearchDepFragment.SearchHandler handler) { // 상품명을 바탕으로
        this.string = "u_query=" + string;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/SearchDep2.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {

                conn.setConnectTimeout(100000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoInput(true);
                conn.setDoOutput(true);
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
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                Log.i("Result", data);

                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.i("Connnection", conn.getResponseCode() + "\n" + conn.getErrorStream() + "");
                    return null;
                } else {
                    Log.i("Connnection", conn.getResponseCode() + "");
                }
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

        if (data != null) {
            Message msg = new Message();
            msg.obj = data;
            handler.sendMessage(msg);
        }
    }
}
