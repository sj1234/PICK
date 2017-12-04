package com.example.sjeong.pick;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.labo.kaji.fragmentanimations.MoveAnimation;

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

import static android.content.Context.MODE_PRIVATE;


public class SearchDepDetailFragment extends Fragment implements View.OnClickListener{

    private TextView bank_text, join_way_text, term_text, rate_text, min_join;
    private CheckBox join_1, join_2, join_3, com, sim, first_customer, mobile_internet, fix_rate, float_rate;
    private String[] detail_info = new String[9];
    private View view, bank_view, join_way_view;
    private int animation = MoveAnimation.UP;
    private ArrayList<CheckBox> bank_list = new  ArrayList<CheckBox>();
    private ArrayList<CheckBox> join_list = new  ArrayList<CheckBox>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_dep_detail,null);
        bank_view = getActivity().getLayoutInflater().inflate(R.layout.bank_select,null);
        join_way_view = getActivity().getLayoutInflater().inflate(R.layout.join_way_select,null);

        // 은행선택
        LinearLayout bank = (LinearLayout)view.findViewById(R.id.bank);
        bank_text = (TextView)view.findViewById(R.id.bank_text);
        bank.setOnClickListener(this);

        // 기간
        SeekBar term_seekbar = (SeekBar) view.findViewById(R.id.term_seekbar);
        term_text = (TextView) view.findViewById(R.id.term_text);
        term_seekbar.setMax(36);
        term_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {// seekbar 값 변화할 때
                term_text.setText(progress+"개월 이상");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {// touch 시작시
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { // touch 종료
                detail_info[1] = term_text.getText().toString().replace("개월 이상", "");
            }
        });

        // 금리선택
        rate_text = (TextView)view.findViewById(R.id.rate_text);
        SeekBar rate = (SeekBar)view.findViewById(R.id.rate_seekBar);
        rate.setMax(100);
        rate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {// seekbar 값 변화할 때
                String rate_string = String.format("%.2f", ((float)progress)*0.1);
                rate_text.setText(rate_string+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {// touch 시작시
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { // touch 종료
                detail_info[2] = rate_text.getText().toString();
            }
        });

        // 가입방법선택
        LinearLayout join_way = (LinearLayout)view.findViewById(R.id.join_way);
        join_way_text = (TextView)view.findViewById(R.id.join_way_text);
        join_way.setOnClickListener(this);

        // 가입대상선택
        join_1 = (CheckBox)view.findViewById(R.id.join_1);
        join_2 = (CheckBox)view.findViewById(R.id.join_2);
        join_3 = (CheckBox)view.findViewById(R.id.join_3);

        // 복리단리
        sim =  (CheckBox)view.findViewById(R.id.sim);
        com =  (CheckBox)view.findViewById(R.id.com);

        // 변동금리
        fix_rate = (CheckBox)view.findViewById(R.id.fix_rate);
        float_rate = (CheckBox)view.findViewById(R.id.float_rate);

        // 최소 금액
        min_join = (TextView)view.findViewById(R.id.min_join);
        SeekBar min_seekBar = (SeekBar)view.findViewById(R.id.min_seekBar);
        min_seekBar.setMax(299);
        min_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {// seekbar 값 변화할 때
                String rate_string = String.format("%d", progress+1);
                min_join.setText(rate_string+"000원");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {// touch 시작시
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { // touch 종료
                detail_info[6] = min_join.getText().toString();
            }
        });

        // 우대조건 첫고객, 모바일 및 인터넷 가입
        first_customer = (CheckBox)view.findViewById(R.id.first_customer);
        mobile_internet = (CheckBox)view.findViewById(R.id.mobile_internet);

        // 초기화버튼
        Button reset = (Button)view.findViewById(R.id.reset);
        reset.setOnClickListener(this);

        // 검색버튼
        ImageButton search_detail = (ImageButton)view.findViewById(R.id.search_detail);
        search_detail.setOnClickListener(this);

        // 상품명으로 돌아가기
        Button search_base = (Button)view.findViewById(R.id.search_base);
        search_base.setOnClickListener(this);

        SetInit();
        return view;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return MoveAnimation.create(animation, enter, 1000);
    }

    public void setAnimation(String type){ // 애니메이션 설정
        if(type.equals("UP"))
            animation = MoveAnimation.UP;
        else
            animation = MoveAnimation.DOWN;
    }

    @Override
    public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        switch(v.getId()){
            case R.id.bank:
                resetbankView();
                builder.setTitle("은행선택");
                builder.setView(bank_view);
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckBox All = (CheckBox) bank_view.findViewById(R.id.ALL);

                        if(All.isChecked()) {
                            bank_text.setText("전체");
                            detail_info[0] = "All";
                        }
                        else{
                            String checked="";
                            for(CheckBox bank : bank_list){
                                if(bank.isChecked()){
                                    checked+=bank.getText()+",";
                                }
                            }

                            if(!checked.isEmpty()){
                                detail_info[0] = checked.substring(0, checked.length()-1);
                                String[] checked_bank = checked.split(",");
                                int checked_bank_num = checked_bank.length-1;

                                if(checked_bank_num>0)
                                    bank_text.setText(checked_bank[0]+" 외 "+checked_bank_num+"개");
                                else
                                    bank_text.setText(checked_bank[0]);
                            }
                            else{
                                detail_info[0] = "";
                                bank_text.setText("은행을 선택해 주십시오.");
                            }
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case R.id.join_way:
                resetjoinView();
                builder.setTitle("가입방법선택");
                builder.setView(join_way_view);
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CheckBox All = (CheckBox) join_way_view.findViewById(R.id.ALL);

                        if(All.isChecked()){
                            join_way_text.setText("전체");
                            detail_info[3] = "All";
                        }
                        else{
                            String checked="";

                            for(CheckBox join : join_list){
                                if(join.isChecked())
                                    checked+=join.getText()+",";
                            }

                            if(!checked.isEmpty()){
                                detail_info[3] = checked.substring(0, checked.length()-1);

                                String[] checked_way = checked.split(",");
                                int checked_way_num = checked_way.length-1;

                                if(checked_way_num>0)
                                    join_way_text.setText(checked_way[0]+" 외 "+checked_way_num+"개");
                                else
                                    join_way_text.setText(checked_way[0]);
                            }
                            else {
                                detail_info[3] = "";
                                join_way_text.setText("가입방법을 선택해주십시오.");
                            }
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case R.id.search_base: // 상품명 검색으로 돌아가기
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                SearchDepFragment fragment = (SearchDepFragment)getActivity().getSupportFragmentManager().findFragmentByTag("SearchDep");
                SearchDepDetailFragment detail = (SearchDepDetailFragment)getActivity().getSupportFragmentManager().findFragmentByTag("SearchDepDetail");

                fragment.setAnimation("DOWN");
                detail.setAnimation("DOWN");

                ft.hide(detail);
                ft.show(fragment);
                ft.commit();
                break;
            case R.id.search_detail: // 검색
                saveInfo();
                break;
            case R.id.reset:  //초기화
                FormatInfo();
                break;
        }
    }

    public void resetjoinView(){

        join_way_view = getActivity().getLayoutInflater().inflate(R.layout.join_way_select,null);
        join_list.clear();
        join_list.add((CheckBox) join_way_view.findViewById(R.id.Inter));
        join_list.add((CheckBox) join_way_view.findViewById(R.id.Mobile));
        join_list.add((CheckBox) join_way_view.findViewById(R.id.Visit));
        join_list.add((CheckBox) join_way_view.findViewById(R.id.Call));

        // All 선택시 클릭 리스너 처리
        CheckBox All = (CheckBox) join_way_view.findViewById(R.id.ALL);
        All.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked ) {
                    for(CheckBox join : join_list)
                        join.setChecked(true);
                }
                else{
                    for(CheckBox join : join_list)
                        join.setChecked(false);
                }
            }
        });

        if(detail_info[3].equals("All")){
            All.setChecked(true);
        }

        for(CheckBox checkBox : join_list){
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CheckBox All = (CheckBox) join_way_view.findViewById(R.id.ALL);
                    if(!isChecked && All.isChecked()){
                        All.setChecked(false);
                        for(CheckBox checkBox : join_list){
                            if(!checkBox.getText().toString().equals(buttonView.getText().toString()) && !checkBox.getText().toString().equals("전체"))
                                checkBox.setChecked(true);
                        }
                    }
                }
            });

            if(detail_info[3].contains(checkBox.getText().toString()))
                checkBox.setChecked(true);
        }
    }

    public void resetbankView(){
        bank_view = getActivity().getLayoutInflater().inflate(R.layout.bank_select,null);
        // 은행 리스트 -> 순서 농협, 기업, 국민, 우리, 하나, 산업, 경남, 광주, 대구, 부산, 수협, 스탠다드, 씨티, 우체국, 전북, 제주, 케이
        bank_list.clear();
        bank_list.add((CheckBox) bank_view.findViewById(R.id.NH));
        bank_list.add((CheckBox) bank_view.findViewById(R.id.IBK));
        bank_list.add((CheckBox) bank_view.findViewById(R.id.KB));
        bank_list.add((CheckBox) bank_view.findViewById(R.id.WooRi));
        bank_list.add((CheckBox) bank_view.findViewById(R.id.HaNa));
        bank_list.add((CheckBox) bank_view.findViewById(R.id.KDB));
        bank_list.add((CheckBox) bank_view.findViewById(R.id.KN));
        bank_list.add((CheckBox) bank_view.findViewById(R.id.GJ));
        bank_list.add((CheckBox) bank_view.findViewById(R.id.DG));
        bank_list.add((CheckBox) bank_view.findViewById(R.id.BS));
        bank_list.add((CheckBox) bank_view.findViewById(R.id.SH));
        bank_list.add((CheckBox) bank_view.findViewById(R.id.STANDARD));
        bank_list.add((CheckBox) bank_view.findViewById(R.id.CITY));
        bank_list.add((CheckBox) bank_view.findViewById(R.id.POST));
        bank_list.add((CheckBox) bank_view.findViewById(R.id.JB));
        bank_list.add((CheckBox) bank_view.findViewById(R.id.JJ));
        bank_list.add((CheckBox) bank_view.findViewById(R.id.K));

        // All 선택시 클릭 리스너 처리
        CheckBox All_bank = (CheckBox) bank_view.findViewById(R.id.ALL);
        All_bank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked ) {
                    for(CheckBox bank : bank_list)
                        bank.setChecked(true);
                }
                else{
                    for(CheckBox bank : bank_list)
                        bank.setChecked(false);
                }
            }
        });
        if(detail_info[0].equals("All")){
            All_bank.setChecked(true);
        }

        for(CheckBox checkBox : bank_list){
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CheckBox All_bank = (CheckBox) bank_view.findViewById(R.id.ALL);
                    if(!isChecked && All_bank.isChecked()){
                        All_bank.setChecked(false);
                        for(CheckBox checkBox : bank_list){
                            if(!checkBox.getText().toString().equals(buttonView.getText().toString()) && !checkBox.getText().toString().equals("전체"))
                                checkBox.setChecked(true);
                        }
                    }
                }
            });

            if(detail_info[0].contains(checkBox.getText().toString()))
                checkBox.setChecked(true);
        }
    }

    // 상세검색정보 저장!
    public void saveInfo(){
        String sql;
        SearchDetailHandler handler = new SearchDetailHandler();

        // 4 가입대상, 5 복리단리, 7 우대조건, 8 변동금리
        if(join_1.isChecked()) detail_info[4] = "1,";
        else detail_info[4] = "0,";
        if(join_2.isChecked()) detail_info[4] += "1,";
        else detail_info[4] += "0,";
        if(join_3.isChecked()) detail_info[4] += "1";
        else detail_info[4] += "0";

        if(com.isChecked()) detail_info[5] = "1,";
        else detail_info[5] = "0,";
        if(sim.isChecked()) detail_info[5] += "1";
        else detail_info[5] += "0";

        if(first_customer.isChecked()) detail_info[7] = "1,";
        else detail_info[7] = "0,";
        if(mobile_internet.isChecked()) detail_info[7] += "1";
        else detail_info[7] += "0";

        if(fix_rate.isChecked()) detail_info[8] = "1,";
        else detail_info[8] = "0,";
        if(float_rate.isChecked()) detail_info[8] += "1";
        else detail_info[8] += "0";

        if(detail_info[0].isEmpty() || detail_info[1].equals("0,36") || detail_info[2].equals("0.0%") || detail_info[3].isEmpty() || detail_info[4].equals("0,0,0")
                || detail_info[5].equals("0,0") || detail_info[6].equals("1000원") || detail_info[8].equals("0,0"))
            Toast.makeText(getActivity(), "입력하지 않은 조건은 전체로 검색합니다.", Toast.LENGTH_SHORT).show();

        // All/0/0.00%/All/1,1,1/1,1/1000원/0,0/1,1  // 상세 검색어를 아에 입력하지 않은 경우
        if((detail_info[0].isEmpty() || detail_info[0].equals("All")) && detail_info[1].equals("0") && detail_info[2].equals("0.00%") && (detail_info[3].isEmpty() || detail_info[3].equals("All")) &&
                (detail_info[4].equals("0,0,0") && detail_info[4].equals("1,1,1")) && (detail_info[5].equals("0,0") || detail_info[5].equals("1,1")) && detail_info[6].equals("1000원") && detail_info[8].equals("1,1")){
            sql  = "SELECT A.PROD_CODE AS CODE, MIN(A.CONT_RATE) AS MIN, MAX(MAX_RATE) AS MAX, B.PROD_NAME AS NAME, B.BANK AS BANK FROM DEP_RATE A INNER JOIN DEPOSITE B ON A.PROD_CODE = B.PROD_CODE GROUP BY A.PROD_CODE";
        }
        else{ // 상세 검색어를 한개이상 검색한 경우

            String bank="", join="", target="", sim_com="", min_money="", float_rate="";

            // 은행
            if(!detail_info[0].equals("All") && !detail_info[0].isEmpty() ){
                String[] bank_list = detail_info[0].split(",");
                for(String list : bank_list)
                    bank += "'"+list+"',";
                bank = "BANK IN ("+bank.substring(0, bank.length()-1)+")";
            }

            // 가입방법
            if(!detail_info[3].equals("All")){
                join = "JOIN_WAY '";

                if(detail_info[3].contains("인터넷")) join+="1";
                else join+="0";
                if(detail_info[3].contains("모바일")) join+="1";
                else join+="0";
                if(detail_info[3].contains("영업점")) join+="1";
                else join+="0";
                if(detail_info[3].contains("콜센터")) join+="1";
                else join+="0";
            }
            if(join.equals("JOIN_WAY '0000") || join.isEmpty()) join="";
            else join +="'";

            // 가입대상
            if(!detail_info[4].equals("1,1,1") && !detail_info[4].equals("0,0,0")){
                String[] join_target_list = detail_info[4].split(",");
                target = "JOIN_TARGET '";
                for(String list : join_target_list)
                    target += list;
                target +="'";
            }

            // 복리 단리
            if(!detail_info[5].equals("1,1") && !detail_info[5].equals("0,0")){
                sim_com="COM_SIM='";
                String[] com_sim_list = detail_info[5].split(",");
                if(com_sim_list[0]=="1") sim_com+="0'";
                else sim_com+="1'";
            }

            // 가입금액
            if(!detail_info[6].equals("1000원")){
                min_money = "JOIN_LIMIT >= '"+detail_info[6].replace("000원","")+"'";
            }

            // 변동금리
            if(!detail_info[8].equals("1,1") && !detail_info[8].equals("0,0")){
                float_rate = "FLOAT_RATE='";
                String[] float_rate_list = detail_info[8].split(",");
                if(float_rate_list[0]=="1") float_rate+="0'";
                else float_rate+="1'";
            }

            // 금리, 기간
            sql  = "SELECT A.PROD_CODE AS CODE, A.MIN AS MIN, A.MAX AS MAX, B.PROD_NAME AS NAME, B.BANK AS BANK FROM (SELECT PROD_CODE, MIN( CONT_RATE ) AS MIN, MAX( MAX_RATE ) AS MAX FROM DEP_RATE";

            if(!detail_info[1].equals("0") || !detail_info[2].equals("0.00%")){
                sql += " WHERE ";
                if(!detail_info[1].equals("0")){
                    sql += "(CONT_TERM<='"+detail_info[1]+"' AND CONT_TERM_END>='"+detail_info[1]+"')";

                    if(!detail_info[2].equals("0.00%")){
                        String rate = detail_info[2].replace("%", "");
                        sql += " AND MAX_RATE>='"+rate+"'";
                    }
                }
                else if(!detail_info[2].equals("0.00%")){
                    String rate = detail_info[2].replace("%", "");
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
            if(!float_rate.isEmpty()){
                if(sql.contains("DEPOSITE WHERE"))
                    sql += " AND " + float_rate;
                else
                    sql += " WHERE " + float_rate;
            }

            sql += ") B ON A.PROD_CODE = B.PROD_CODE";
        }

        // 우대조건
        if(!detail_info[7].equals("0,0")){
            String prime = "SELECT PROD_CODE FROM DEP_COND WHERE PRIME_COND'";
            String[] prime_list = detail_info[7].split(",");
            prime += prime_list[0]+""+prime_list[1]+"'";

            sql += "/" + prime;
            SearchDetailDB test = new SearchDetailDB("2", sql, handler);
            test.execute();
        }
        else{
            SearchDetailDB test = new SearchDetailDB("1", sql, handler);
            test.execute();
        }

    }

    class SearchDetailHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case 6: //데이터 받아옴
                    Log.i("SearchHandler", "case 6");

                    Intent intent = new Intent(getActivity(), ItemSearchActivity.class);
                    intent.putExtra("type", "1,2");
                    intent.putExtra("data", msg.obj.toString());
                    startActivity(intent);
                    break;
                case 7:
                    Log.i("SearchHandler", "case 7");
                    String sql = msg.obj.toString();
                    SearchDetailDB test = new SearchDetailDB("3", sql, this);
                    test.execute();
                    break;
                case 8:
                    Log.i("SearchHandler", "case 8");
                    Intent intent_8 = new Intent(getActivity(), ItemSearchActivity.class);
                    intent_8.putExtra("type", "3");
                    intent_8.putExtra("data", msg.obj.toString());
                    startActivity(intent_8);
                    break;
                case 9: // 초기 검색정보 받아옴
                    try {
                        JSONObject jObject = new JSONObject(msg.obj.toString());

                        String bank = jObject.getString("BANK").toString();
                        if(bank!=null && !bank.isEmpty()){
                            detail_info[0]=bank;
                            bank_text.setText(bank);
                            //체크박스 설정하기
                            for(CheckBox check : bank_list){
                                if(check.getText().toString().contains(bank)){
                                    check.setChecked(true);
                                    break;
                                }
                            }
                        }
                        else{
                            detail_info[0]="All";
                            bank_text.setText("전체");
                            CheckBox All_bank = (CheckBox) bank_view.findViewById(R.id.ALL);
                            All_bank.setChecked(true);
                        }

                        // 기간
                        detail_info[1]="0";
                        SeekBar term_seekbar = (SeekBar) view.findViewById(R.id.term_seekbar);
                        term_text.setText("0개월 이상");
                        term_seekbar.setProgress(0);

                        // 금리
                        String rate_string = jObject.getString("CONT_RATE").toString();
                        SeekBar rate = (SeekBar)view.findViewById(R.id.rate_seekBar);
                        if(!rate_string.equals("0.00")){
                            detail_info[2]=rate_string+"%";
                            rate_text.setText(rate_string+"%");
                            rate.setProgress((int)(Double.parseDouble(rate_string)*10));
                        }
                        else{
                            detail_info[2]="0.00%";
                            rate_text.setText("0.00%");
                            rate.setProgress(0);
                        }

                        // 가입방법
                        detail_info[3]="All";
                        join_way_text.setText("전체");

                        // 가입대상
                        int target = Integer.parseInt(jObject.getString("JOIN_TARGET").toString());
                        if(target!=0){
                            if((target&4)==4) {
                                detail_info[4]="1,";
                                join_1.setChecked(true);
                            }
                            else{
                                detail_info[4]="0,";
                                join_1.setChecked(false);
                            }

                            if((target&2)==2) {
                                detail_info[4]="1,";
                                join_2.setChecked(true);
                            }
                            else{
                                detail_info[4]="0,";
                                join_2.setChecked(false);
                            }

                            if((target&1)==1) {
                                detail_info[4]="1";
                                join_3.setChecked(true);
                            }
                            else{
                                detail_info[4]="0";
                                join_3.setChecked(false);
                            }
                        }
                        else{
                            detail_info[4]="1,1,1";
                            join_1.setChecked(true);
                            join_2.setChecked(true);
                            join_3.setChecked(true);
                        }

                        // 복리단리
                        detail_info[5]="1,1";
                        sim.setChecked(true);
                        com.setChecked(true);

                        // 최소가입
                        detail_info[6]="1000원";
                        SeekBar min_seekBar = (SeekBar)view.findViewById(R.id.min_seekBar);
                        min_seekBar.setProgress(0);
                        min_join.setText("1000원");

                        // 우대조건
                        detail_info[7]="0,0";
                        first_customer.setChecked(false);
                        mobile_internet.setChecked(false);

                        // 변동금리
                        detail_info[8]="1,1";
                        fix_rate.setChecked(true);
                        float_rate.setChecked(true);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("Error", e.toString());
                    }
                    break;
            }
        }
    }

    // 사용자 정보에 따른 검색정보 설정
    public void SetInit(){

        SharedPreferences preferences = getActivity().getSharedPreferences("person", MODE_PRIVATE);
        String id = preferences.getString("id", "");

        SearchDetailHandler handler = new SearchDetailHandler();
        UserSearchDB test = new UserSearchDB(id, handler);
        test.execute();

    }
    // 초기화
    public void FormatInfo(){

        // 은행
        detail_info[0]="All";
        bank_text.setText("전체");
        CheckBox All_bank = (CheckBox) bank_view.findViewById(R.id.ALL);
        All_bank.setChecked(true);

        // 기간
        detail_info[1]="0";
        SeekBar term_seekbar = (SeekBar) view.findViewById(R.id.term_seekbar);
        term_text.setText("0개월 이상");
        term_seekbar.setProgress(0);

        // 금리
        detail_info[2]="0.00%";
        SeekBar rate = (SeekBar)view.findViewById(R.id.rate_seekBar);
        rate_text.setText("0.00%");
        rate.setProgress(0);

        // 가입방법
        detail_info[3]="All";
        join_way_text.setText("전체");

        // 가입대상
        detail_info[4]="1,1,1";
        join_1.setChecked(true);
        join_2.setChecked(true);
        join_3.setChecked(true);

        // 복리단리
        detail_info[5]="1,1";
        sim.setChecked(true);
        com.setChecked(true);

        // 최소가입
        detail_info[6]="1000원";
        SeekBar min_seekBar = (SeekBar)view.findViewById(R.id.min_seekBar);
        min_seekBar.setProgress(0);
        min_join.setText("1000원");

        // 우대조건
        detail_info[7]="0,0";
        first_customer.setChecked(false);
        mobile_internet.setChecked(false);

        // 변동금리
        detail_info[8]="1,1";
        fix_rate.setChecked(true);
        float_rate.setChecked(true);
    }
}

// 검색
class SearchDetailDB extends AsyncTask<Void, Integer, Void> {

    private String data, string, string_3, type;
    private SearchDepDetailFragment.SearchDetailHandler handler;

    public SearchDetailDB(String type, String string, SearchDepDetailFragment.SearchDetailHandler handler){ // 상품명을 바탕으로
        this.type = type;
        this.string = string;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url;

            if(type.equals("2")){
                String[] list = string.split("/");
                string = "u_query="+list[0];
                string_3 = list[1];
                url=new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/SearchDep2.php");
            }
            else if(type.equals("3")){
                String[] list = string.split("/");
                string = "u_query="+list[1];
                string_3 = list[0];
                url=new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/SearchDep3.php");
            }
            else {
                string = "u_query=" + string;
                url=new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/SearchDep2.php");
            }

            Log.i("SQL", type+", "+string);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(conn!=null) {

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

                if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
                    Log.i("Connnection", conn.getResponseCode()+"\n"+conn.getErrorStream() +"");
                    return null;
                }
                else {
                    Log.i("Connnection", conn.getResponseCode()+"");
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

        if(data!=null){
            Message msg = new Message();

            if(type.equals("2")){
                msg.what=7;
                msg.obj=data+"/"+string_3;
            }
            else if(type.equals("3")){
                msg.what=8;
                msg.obj=string_3+"/"+data;
            }
            else{
                msg.what=6;
                msg.obj=data;
            }
            handler.sendMessage(msg);
        }
    }
}

// 검색정보
class UserSearchDB extends AsyncTask<Void, Integer, Void> {

    private String data, string;
    private SearchDepDetailFragment.SearchDetailHandler handler;

    public UserSearchDB(String id, SearchDepDetailFragment.SearchDetailHandler handler){ // 상품명을 바탕으로
        this.string = "u_id="+id;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url=new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/GetSearchInfo.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if(conn!=null) {

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

                if(conn.getResponseCode() != HttpURLConnection.HTTP_OK){
                    Log.i("Connnection", conn.getResponseCode()+"\n"+conn.getErrorStream() +"");
                    return null;
                }
                else {
                    Log.i("Connnection", conn.getResponseCode()+"");
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
        if(data!=null){
            Message msg = new Message();
            msg.what=9;
            msg.obj=data;
            handler.sendMessage(msg);
        }
    }
}