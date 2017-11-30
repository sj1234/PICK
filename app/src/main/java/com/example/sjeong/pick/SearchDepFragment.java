package com.example.sjeong.pick;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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
    private String info="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_dep,null);

        base_detail = (Button)view.findViewById(R.id.base_detail);
        base_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = base_detail.getText().toString();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                if(text.equals("상품명 검색")) {
                    Log.i("Test", "detail On");
                    base_detail.setText("상세 검색");
                    SearchDepDetailFragment fragment = new SearchDepDetailFragment();
                    ft.replace(R.id.fragment_down, fragment);
                    ft.commit();
                }
                else{
                    Log.i("Test", "detail Off");
                    base_detail.setText("상품명 검색");
                    LogoFragment logo = new LogoFragment();
                    ft.replace(R.id.fragment_down, logo);
                    ft.commit();
                }
            }
        });

        searchtext = (EditText)view.findViewById(R.id.searchText);
        ImageButton searchbutton = (ImageButton)view.findViewById(R.id.searchbutton);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchHandler handler = new SearchHandler();

                // 상세검색
                if(base_detail.getText().toString().equals("상세 검색")){
                    Log.i("Test", "detail only");

                    // 상세 검색 정보 가져오기
                    SearchDepDetailFragment fragment = (SearchDepDetailFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_down);
                    info = fragment.saveInfo();
                    String[] info_spl = info.split("/");
                    String sql;

                    if(info_spl[0].isEmpty() || info_spl[1].equals("0,36") || info_spl[2].equals("0.0%") || info_spl[3].isEmpty() || info_spl[4].equals("0,0,0") || info_spl[5].equals("0,0") || info_spl[6].equals("0원")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("입력하지 않은 조건은 전체로 검색합니다.");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }

                    // All/0,36/0.0%/All/1,1,1/1,1/0원/0,0  // 상세 검색어를 아에 입력하지 않은 경우
                    if((info_spl[0].isEmpty() || info_spl[0].equals("All")) && info_spl[1].equals("0") && info_spl[2].equals("0.0%") && (info_spl[3].isEmpty() || info_spl[3].equals("All")) &&
                            (info_spl[4].equals("0,0,0") && info_spl[4].equals("1,1,1")) && (info_spl[5].equals("0,0") || info_spl[5].equals("1,1")) && info_spl[6].equals("0원")){
                        sql  = "SELECT A.PROD_CODE AS CODE, MIN(A.CONT_RATE) AS MIN, MAX(MAX_RATE) AS MAX, B.PROD_NAME AS NAME, B.BANK AS BANK FROM DEP_RATE A INNER JOIN DEPOSITE B ON A.PROD_CODE = B.PROD_CODE GROUP BY A.PROD_CODE";
                    }
                    else{ // 상세 검색어를 한개이상 검색한 경우

                        String bank="", join="", target="", sim_com="", min_money="";

                        // 은행
                        if(!info_spl[0].equals("All") && !info_spl[0].isEmpty() ){
                            String[] bank_list = info_spl[0].split(",");
                            for(String list : bank_list){
                                bank += "'"+list+"',";
                            }
                            bank = "BANK IN ("+bank.substring(0, bank.length()-1)+")";
                        }

                        // 가입방법
                        if(!info_spl[3].equals("All")){
                            join = "JOIN_WAY '";

                            if(info_spl[3].contains("인터넷")) join+="1";
                            else join+="0";

                            if(info_spl[3].contains("모바일")) join+="1";
                            else join+="0";

                            if(info_spl[3].contains("영업점")) join+="1";
                            else join+="0";

                            if(info_spl[3].contains("콜센터")) join+="1";
                            else join+="0";
                        }
                        if(join.equals("JOIN_WAY '0000") || join.isEmpty()) join="";
                        else join +="'";

                        // 가입대상
                        if(!info_spl[4].equals("1,1,1") && !info_spl[4].equals("0,0,0")){
                            String[] join_target_list = info_spl[4].split(",");
                            target = "JOIN_TARGET '";
                            for(String list : join_target_list)
                                target += list;
                            target +="'";
                        }

                        // 복리 단리
                        if(!info_spl[5].equals("1,1") && !info_spl[5].equals("0,0")){
                            sim_com="COM_SIM='";
                            String[] com_sim_list = info_spl[5].split(",");
                            if(com_sim_list[0]=="1") sim_com+="0'";
                            else sim_com+="1'";
                        }

                        // 가입금액
                        if(!info_spl[6].equals("0원")){
                            min_money = "JOIN_LIMIT >= '"+info_spl[6].replace("000원","")+"'";
                        }

                        // 금리, 기간
                        sql  = "SELECT A.PROD_CODE AS CODE, A.MIN AS MIN, A.MAX AS MAX, B.PROD_NAME AS NAME, B.BANK AS BANK FROM (SELECT PROD_CODE, MIN( CONT_RATE ) AS MIN, MAX( MAX_RATE ) AS MAX FROM DEP_RATE";

                        if(!info_spl[1].equals("0") || !info_spl[2].equals("0.0%")){
                            sql += " WHERE ";
                            if(!info_spl[1].equals("0")){
                                sql += "CONT_TERM_END>='"+info_spl[1]+"'";

                                if(!info_spl[2].equals("0.0%")){
                                    String rate = info_spl[2].replace("%", "");
                                    sql += " AND MAX_RATE>='"+rate+"'";
                                }
                            }
                            else if(!info_spl[2].equals("0.0%")){
                                String rate = info_spl[2].replace("%", "");
                                sql += "MAX_RATE>='"+rate+"'";
                            }
                        }

                        sql +=" GROUP BY PROD_CODE) A INNER JOIN ( SELECT PROD_CODE, PROD_NAME, BANK FROM DEPOSITE";

                        if(!bank.isEmpty()){
                            if(sql.contains("DEPOSITE WHERE"))
                                sql += " AND " + bank;
                            else
                                sql += " WHERE " + bank;
                        }
                        if(!join.isEmpty()) {
                            if(sql.contains("DEPOSITE WHERE"))
                                sql += " AND " + join;
                            else
                                sql += " WHERE " + join;
                        }
                        if(!target.isEmpty()){
                            if(sql.contains("DEPOSITE WHERE"))
                                sql += " AND " + target;
                            else
                                sql += " WHERE " + target;
                        }
                        if(!sim_com.isEmpty()) {
                            if(sql.contains("DEPOSITE WHERE"))
                                sql += " AND " + sim_com;
                            else
                                sql += " WHERE " + sim_com;
                        }
                        if(!min_money.isEmpty()){
                            if(sql.contains("DEPOSITE WHERE"))
                                sql += " AND " + min_money;
                            else
                                sql += " WHERE " + min_money;
                        }

                        sql += ") B ON A.PROD_CODE = B.PROD_CODE";
                    }

                    // 우대조건
                    if(!info_spl[7].equals("0,0")){
                        String prime = "SELECT PROD_CODE FROM DEP_COND WHERE PRIME_COND'";
                        String[] prime_list = info_spl[7].split(",");
                        prime += prime_list[0]+""+prime_list[1]+"'";

                        sql += "/" + prime;
                        SearchDB test = new SearchDB("3", sql, handler);
                        test.execute();
                    }
                    else{
                        SearchDB test = new SearchDB("2", sql, handler);
                        test.execute();
                    }
                }
                else{ // 기본검색
                    String text = searchtext.getText().toString();
                    Log.i("Test", text);

                    if(text.isEmpty()){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("상품명을 검색해주세요.");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                    else{
                        String sql = "SELECT A.PROD_CODE AS CODE, MIN(A.CONT_RATE) AS MIN, MAX(MAX_RATE) AS MAX, B.PROD_NAME AS NAME, B.BANK AS BANK FROM DEP_RATE A INNER JOIN (SELECT PROD_CODE, PROD_NAME, BANK FROM DEPOSITE WHERE PROD_NAME LIKE '%"+ text+"%') B ON A.PROD_CODE = B.PROD_CODE GROUP BY A.PROD_CODE";
                        SearchDB test = new SearchDB("1", sql, handler);
                        test.execute();
                    }
                }
            }
        });

        return view;
    }

    class SearchHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);

            ItemSearchFragment fragment;
            Bundle bundle;
            FragmentTransaction ft;

            switch(msg.what){
                case 6: //데이터 받아옴
                    Log.i("SearchHandler", "case 6");
                    fragment = new ItemSearchFragment();

                    bundle = new Bundle(2); // 파라미터는 전달할 데이터 개수
                    bundle.putString("type", "1,2");
                    bundle.putString("data", msg.obj.toString());
                    fragment.setArguments(bundle);

                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_down, fragment);
                    ft.commit();
                    break;
                case 7:
                    Log.i("SearchHandler", "case 7");
                    String sql = msg.obj.toString();
                    SearchDB test = new SearchDB("4", sql, this);
                    test.execute();
                    break;
                case 8:
                    Log.i("SearchHandler", "case 8");
                    fragment = new ItemSearchFragment();

                    bundle = new Bundle(2); // 파라미터는 전달할 데이터 개수
                    bundle.putString("type", "3");
                    bundle.putString("data", msg.obj.toString());
                    fragment.setArguments(bundle);

                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_down, fragment);
                    ft.commit();
                    break;
            }
        }
    }
}

// 검색
class SearchDB extends AsyncTask<Void, Integer, Void> {

    private String data, string, string_3, type;
    private SearchDepFragment.SearchHandler handler;

    public SearchDB(String type, String string, SearchDepFragment.SearchHandler handler) { // 상품명을 바탕으로
        this.type = type;
        this.string = string;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url;

            if (type.equals("3")) {
                String[] list = string.split("/");
                string = "u_query=" + list[0];
                string_3 = list[1];
                url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/SearchDep2.php");
            } else if (type.equals("4")) {
                String[] list = string.split("/");
                string = "u_query=" + list[1];
                string_3 = list[0];
                url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/SearchDep3.php");
            } else {
                string = "u_query=" + string;
                url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/SearchDep2.php");
            }

            Log.i("SQL", type + ", " + string);
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

            if (type.equals("3")) {
                msg.what = 7;
                msg.obj = data + "/" + string_3;
            } else if (type.equals("4")) {
                msg.what = 8;
                msg.obj = string_3 + "/" + data;
            } else {
                msg.what = 6;
                msg.obj = data;
            }
            handler.sendMessage(msg);
        }
    }
}
