package com.example.sjeong.pick;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;


public class SearchDepDetailFragment extends Fragment implements View.OnClickListener{
    private TextView bank2, join_way2;
    private String[] detail_info = new String[8];
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_dep_detail,null);

        // 은행선택
        TextView bank1 = (TextView)view.findViewById(R.id.bank1);
        bank2 = (TextView)view.findViewById(R.id.bank2);
        TextView bank3 = (TextView)view.findViewById(R.id.bank3);
        bank1.setOnClickListener(this);
        bank2.setOnClickListener(this);
        bank3.setOnClickListener(this);

        // 가입대상선택
        TextView join_1 = (TextView)view.findViewById(R.id.join_1);
        TextView join_2 = (TextView)view.findViewById(R.id.join_2);
        TextView join_3 = (TextView)view.findViewById(R.id.join_3);
        join_1.setOnClickListener(this);
        join_2.setOnClickListener(this);
        join_3.setOnClickListener(this);

        // 가입방법선택
        TextView join_way1 = (TextView)view.findViewById(R.id.join_way1);
        join_way2 = (TextView)view.findViewById(R.id.join_way2);
        TextView join_way3 = (TextView)view.findViewById(R.id.join_way3);
        join_way1.setOnClickListener(this);
        join_way2.setOnClickListener(this);
        join_way3.setOnClickListener(this);

        // 금리선택
        final TextView rate_text = (TextView)view.findViewById(R.id.rate_text);
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

        // 최소 금액
        final TextView min_join = (TextView)view.findViewById(R.id.min_join);
        SeekBar min_seekBar = (SeekBar)view.findViewById(R.id.min_seekBar);
        min_seekBar.setMax(300);
        min_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {// seekbar 값 변화할 때
                String rate_string = String.format("%d", progress);
                min_join.setText(rate_string+"원");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {// touch 시작시
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { // touch 종료
                detail_info[6] = min_join.getText().toString();
            }
        });

        // 기간
        final SeekBar term_seekbar = (SeekBar) view.findViewById(R.id.term_seekbar);
        final TextView term_text = (TextView) view.findViewById(R.id.term_text);

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

        // 복리단리
        TextView sim =  (TextView)view.findViewById(R.id.sim);
        sim.setOnClickListener(this);
        TextView com =  (TextView)view.findViewById(R.id.com);
        com.setOnClickListener(this);

        // 우대조건 첫고객, 모바일 및 인터넷 가입
        TextView first_customer = (TextView)view.findViewById(R.id.first_customer);
        first_customer.setOnClickListener(this);
        TextView mobile_internet = (TextView)view.findViewById(R.id.mobile_internet);
        mobile_internet.setOnClickListener(this);

        // 초기화버튼
        Button reset = (Button)view.findViewById(R.id.reset);
        reset.setOnClickListener(this);
        DetailOn();

        //String info = getArguments().getString("info"); // 넘어온 값
        return view;
    }

    // 상세검색정보 저장!
    public String saveInfo(){
        String detail_info_string="";

        for(int i=0; i<detail_info.length; i++){
            detail_info_string+=detail_info[i];
            if(i!=detail_info.length-1)
                detail_info_string+="/";
        }
        Log.i("save info", detail_info_string);
        return detail_info_string;
    }

    private View bank_view, join_way_view;

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        bank_view = getActivity().getLayoutInflater().inflate(R.layout.bank_select,null);
        final ArrayList<CheckBox> bank_list = new  ArrayList<CheckBox>();
        // 은행 리스트 -> 순서 농협, 기업, 국민, 우리, 하나, 산업, 경남, 광주, 대구, 부산, 수협, 스탠다드, 씨티, 우체국, 전북, 제주, 케이
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

        join_way_view = getActivity().getLayoutInflater().inflate(R.layout.join_way_select,null);
        final ArrayList<CheckBox> join_list = new  ArrayList<CheckBox>();
        join_list.add((CheckBox) join_way_view.findViewById(R.id.Inter));
        join_list.add((CheckBox) join_way_view.findViewById(R.id.Mobile));
        join_list.add((CheckBox) join_way_view.findViewById(R.id.Visit));
        join_list.add((CheckBox) join_way_view.findViewById(R.id.Call));

        switch(v.getId()){
            case R.id.bank1:
            case R.id.bank2:
            case R.id.bank3:

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
                            bank2.setText("전체");
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
                                    bank2.setText(checked_bank[0]+" 외 "+checked_bank_num+"개");
                                else
                                    bank2.setText(checked_bank[0]);
                            }
                            else{
                                detail_info[0] = "";
                                bank2.setText("은행을 선택해 주십시오.");
                            }
                        }
                        dialog.dismiss();
                    }
                });

                // All 선택시 클릭 리스너 처리
                CheckBox All_bank = (CheckBox) bank_view.findViewById(R.id.ALL);
                All_bank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                            if ( isChecked ) {
                                                                for(CheckBox bank : bank_list){
                                                                    bank.setChecked(true);
                                                                }
                                                            }
                                                            else{
                                                                for(CheckBox bank : bank_list){
                                                                    bank.setChecked(false);
                                                                }
                                                            }
                                                        }
                                                    }
                );

                if(!bank2.getText().toString().isEmpty()){
                    if(bank2.getText().toString()=="전체") {
                        All_bank.setChecked(true);
                    }
                    else{
                        for(CheckBox bank : bank_list){
                            if(bank2.getText().toString().contains(bank.getText().toString()))
                                bank.setChecked(true);
                        }
                    }
                }
                builder.show();
                break;
            // 가입조건 개인
            case R.id.join_1:
                TextView join_1 = (TextView)view.findViewById(R.id.join_1);
                String[] splite = detail_info[4].split(",");
                if(join_1.getCurrentTextColor()== ContextCompat.getColor(getActivity(), R.color.UncheckTextView)) {
                    join_1.setTextColor(ContextCompat.getColor(getActivity(), R.color.CheckTextView));
                    detail_info[4] = "1,"+splite[1]+","+splite[2];
                }
                else {
                    join_1.setTextColor(ContextCompat.getColor(getActivity(), R.color.UncheckTextView));
                    detail_info[4] = "0,"+splite[1]+","+splite[2];
                }
                break;
            // 가입조건 단체
            case R.id.join_2:
                TextView join_2 = (TextView)view.findViewById(R.id.join_2);
                String[] splite_2 = detail_info[4].split(",");
                if(join_2.getCurrentTextColor()== ContextCompat.getColor(getActivity(), R.color.UncheckTextView)) {
                    join_2.setTextColor(ContextCompat.getColor(getActivity(), R.color.CheckTextView));
                    detail_info[4] = splite_2[0]+",1,"+splite_2[2];
                }
                else {
                    join_2.setTextColor(ContextCompat.getColor(getActivity(), R.color.UncheckTextView));
                    detail_info[4] = splite_2[0]+",0,"+splite_2[2];
                }
                break;
            // 가입조건 법인, 사업자
            case R.id.join_3:
                TextView join_3 = (TextView)view.findViewById(R.id.join_3);
                String[] splite_3 = detail_info[4].split(",");
                if(join_3.getCurrentTextColor()== ContextCompat.getColor(getActivity(), R.color.UncheckTextView)) {
                    join_3.setTextColor(ContextCompat.getColor(getActivity(), R.color.CheckTextView));
                    detail_info[4] = splite_3[0]+","+splite_3[1]+",1";
                }
                else {
                    join_3.setTextColor(ContextCompat.getColor(getActivity(), R.color.UncheckTextView));
                    detail_info[4] = splite_3[0]+","+splite_3[1]+",0";
                }
                break;
            case R.id.join_way1:
            case R.id.join_way2:
            case R.id.join_way3:

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
                            join_way2.setText("전체");
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
                                    join_way2.setText(checked_way[0]+" 외 "+checked_way_num+"개");
                                else
                                    join_way2.setText(checked_way[0]);
                            }
                        }
                        dialog.dismiss();
                    }
                });

                // All 선택시 클릭 리스너 처리
                CheckBox All_join = (CheckBox) join_way_view.findViewById(R.id.ALL);
                All_join.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                            if ( isChecked ) {
                                                                for(CheckBox join : join_list){
                                                                    join.setChecked(true);
                                                                }
                                                            }
                                                            else{
                                                                for(CheckBox join : join_list){
                                                                    join.setChecked(false);
                                                                }
                                                            }
                                                        }
                                                    }
                );

                if(!join_way2.getText().toString().isEmpty()){
                    if(join_way2.getText().toString()=="전체") {
                        All_join.setChecked(true);
                    }
                    else{
                        for(CheckBox join : join_list){
                            if(join_way2.getText().toString().contains(join.getText().toString()))
                                join.setChecked(true);
                        }
                    }
                }

                builder.show();
                break;
            // 단리
            case R.id.com:
                TextView com = (TextView)view.findViewById(R.id.com);
                if(com.getCurrentTextColor()== ContextCompat.getColor(getActivity(), R.color.UncheckTextView)){
                    com.setTextColor(ContextCompat.getColor(getActivity(), R.color.CheckTextView));
                    detail_info[5]=detail_info[5].replace("0,", "1,");
                }
                else{
                    com.setTextColor(ContextCompat.getColor(getActivity(), R.color.UncheckTextView));
                    detail_info[5]=detail_info[5].replace("1,", "0,");
                }
                break;
            // 복리
            case R.id.sim:
                TextView sim = (TextView)view.findViewById(R.id.sim);
                if(sim.getCurrentTextColor()== ContextCompat.getColor(getActivity(), R.color.UncheckTextView)) {
                    sim.setTextColor(ContextCompat.getColor(getActivity(), R.color.CheckTextView));
                    detail_info[5]=detail_info[5].replace(",0", ",1");
                }
                else{
                    sim.setTextColor(ContextCompat.getColor(getActivity(), R.color.UncheckTextView));
                    detail_info[5]=detail_info[5].replace(",1", ",0");
                }
                break;
            // 우대조건 첫고객
            case R.id.first_customer:
                TextView first_customer = (TextView)view.findViewById(R.id.first_customer);
                if(first_customer.getCurrentTextColor()== ContextCompat.getColor(getActivity(), R.color.UncheckTextView)) {
                    first_customer.setTextColor(ContextCompat.getColor(getActivity(), R.color.CheckTextView));
                    detail_info[7]=detail_info[7].replace("0,", "1,");
                }
                else {
                    first_customer.setTextColor(ContextCompat.getColor(getActivity(), R.color.UncheckTextView));
                    detail_info[7]=detail_info[7].replace("1,", "0,");
                }
                break;
            // 우대조건 모바일, 인터넷 가입
            case R.id.mobile_internet:
                TextView mobile_internet = (TextView)view.findViewById(R.id.mobile_internet);
                if(mobile_internet.getCurrentTextColor()== ContextCompat.getColor(getActivity(), R.color.UncheckTextView)) {
                    mobile_internet.setTextColor(ContextCompat.getColor(getActivity(), R.color.CheckTextView));
                    detail_info[7]=detail_info[7].replace(",0", ",1");
                }
                else{
                    mobile_internet.setTextColor(ContextCompat.getColor(getActivity(), R.color.UncheckTextView));
                    detail_info[7]=detail_info[7].replace(",1", ",0");
                }
                break;
            case R.id.reset:  //초기화
                break;
        }
    }

    public void DetailOn(){
        FormatInfo();
    }

    public void FormatInfo(){

        TextView join_1 = (TextView)view.findViewById(R.id.join_1);
        TextView join_2 = (TextView)view.findViewById(R.id.join_2);
        TextView join_3 = (TextView)view.findViewById(R.id.join_3);
        TextView com = (TextView)view.findViewById(R.id.com);
        TextView sim = (TextView)view.findViewById(R.id.sim);
        TextView first_customer = (TextView)view.findViewById(R.id.first_customer);
        TextView mobile_internet = (TextView)view.findViewById(R.id.mobile_internet);

        bank_view = getActivity().getLayoutInflater().inflate(R.layout.bank_select,null);
        join_way_view = getActivity().getLayoutInflater().inflate(R.layout.join_way_select,null);
        detail_info = new String[8];

        // 은행
        detail_info[0]="All";
        bank2.setText("전체");

        // 기간
        detail_info[1]="0";
        SeekBar term_seekbar = (SeekBar) view.findViewById(R.id.term_seekbar);
        TextView term_min = (TextView) view.findViewById(R.id.term_min);
        TextView term_max = (TextView) view.findViewById(R.id.term_max);
        term_seekbar.setMax(36);
        term_min.setText("0");
        term_max.setText("36");

        // 금리
        detail_info[2]="0.0%";
        TextView rate_text = (TextView)view.findViewById(R.id.rate_text);
        SeekBar rate = (SeekBar)view.findViewById(R.id.rate_seekBar);
        rate_text.setText("0.0%");
        rate.setProgress(0);

        // 가입방법
        detail_info[3]="All";
        join_way2.setText("전체");

        // 가입대상
        detail_info[4]="1,1,1";
        join_1.setTextColor(ContextCompat.getColor(getActivity(), R.color.CheckTextView));
        join_2.setTextColor(ContextCompat.getColor(getActivity(), R.color.CheckTextView));
        join_3.setTextColor(ContextCompat.getColor(getActivity(), R.color.CheckTextView));

        // 복리단리
        detail_info[5]="1,1";
        sim.setTextColor(ContextCompat.getColor(getActivity(), R.color.CheckTextView));
        com.setTextColor(ContextCompat.getColor(getActivity(), R.color.CheckTextView));

        // 최소가입
        detail_info[6]="0원";
        TextView min_join = (TextView)view.findViewById(R.id.min_join);
        SeekBar min_seekBar = (SeekBar)view.findViewById(R.id.min_seekBar);
        min_seekBar.setProgress(0);
        min_join.setText("0원");

        // 우대조건
        detail_info[7]="0,0";
        first_customer.setTextColor(ContextCompat.getColor(getActivity(), R.color.UncheckTextView));
        mobile_internet.setTextColor(ContextCompat.getColor(getActivity(), R.color.UncheckTextView));

    }
}