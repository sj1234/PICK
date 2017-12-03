package com.example.sjeong.pick;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


// * 가입금액, 월납입액 중 한가지 이상을 입력하여야 합니다.\n* 가입금액을 입력하지 않으면 0원으로 계산을 합니다.
// * 월납입액을 입력하지 않으면 정기예금으로 계산을 합니다.

public class GoalAddFragment extends Fragment implements View.OnClickListener {

    private TextView start_month, rate, month_text, fail_text;
    private EditText goal_name, join_sum, monthly_sum;
    private Boolean com_sim=true;
    private SeekBar rate_seekBar, month, fail_month_num;
    private Button goal_add;
    private RadioButton com, sim;
    private String data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_goal_add, container, false);

        // 뒤로가기
        ImageButton back_to_goal = (ImageButton)view.findViewById(R.id.back_to_goal);
        back_to_goal.setOnClickListener(this);

        // 목표이름
        goal_name = (EditText)view.findViewById(R.id.goal_name);

        // 시작 날짜
        long now = System.currentTimeMillis();  // 현재시간
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        start_month = (TextView)view.findViewById(R.id.start_month);
        start_month.setText(CurDateFormat.format(new Date(now)));
        LinearLayout start = (LinearLayout)view.findViewById(R.id.start);
        start.setOnClickListener(this);

        // 가입개월
        month_text =  (TextView)view.findViewById(R.id.month_text);
        month = (SeekBar)view.findViewById(R.id.month);
        month.setMax(120);
        month.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                month_text.setText(progress+"개월");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 금리
        rate = (TextView)view.findViewById(R.id.rate);
        rate_seekBar = (SeekBar)view.findViewById(R.id.rate_seekBar);
        rate_seekBar.setMax(70);
        rate_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String rate_string = String.format("%.2f", ((float)progress)*0.1);
                rate.setText(rate_string+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 단리복리
        com = (RadioButton)view.findViewById(R.id.com);
        com.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    sim.setChecked(false);
                }
            }
        });
        sim = (RadioButton)view.findViewById(R.id.sim);
        sim.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    com.setChecked(false);
                }
            }
        });
        com.isChecked();

        // 가입금액
        join_sum = (EditText)view.findViewById(R.id.join_sum);

        // 월납입액
        monthly_sum = (EditText)view.findViewById(R.id.monthly_sum);

        // 실패한 달
        fail_text =  (TextView)view.findViewById(R.id.fail_text);
        fail_month_num = (SeekBar)view.findViewById(R.id.fail_month_num);
        fail_month_num.setMax(120);
        fail_month_num.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fail_text.setText(progress+"개월");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        // 목표추가
        goal_add = (Button)view.findViewById(R.id.goal_add);
        goal_add.setTag( "add");
        goal_add.setOnClickListener(this);

        data = getArguments().getString("data");
        if(!data.isEmpty()){
            Log.i("get goal data", data);
            goal_add.setText("목표 수정하기");
            goal_add.setTag("update");
            SharedPreferences preferences = getActivity().getSharedPreferences("person", MODE_PRIVATE);
            String id = preferences.getString("id", "");

            SetGoalHandler handler = new SetGoalHandler(id, data, "", "", "", "", "", "");
            SetGoalDB test = new SetGoalDB("Get", id, data, handler);
            test.execute();
        }
        else{
            LinearLayout fail = (LinearLayout)view.findViewById(R.id.fail);
            fail_month_num.setVisibility(View.GONE);
            fail.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_to_goal: // 뒤로 가기
                GoalAddFragment fragment = (GoalAddFragment)getActivity().getSupportFragmentManager().findFragmentByTag("goal_add_fragment");
                GoalFragment goal = (GoalFragment)getActivity().getSupportFragmentManager().findFragmentByTag("goal_fragment");
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.remove(fragment);
                ft.show(goal);
                ft.commit();
                break;
            case R.id.goal_add: // 목표추가
                // 이름, 시작달, 목표달, 금리, 단복리, 가입금액, 월납입
                String name = goal_name.getText().toString(), start= start_month.getText().toString(), end = month.getProgress()+"", rate_persent = rate.getText().toString().replace("%",""),
                        join = join_sum.getText().toString(), monthly = monthly_sum.getText().toString(), comsim, fail = fail_month_num.getProgress()+"";
                if(com_sim)
                    comsim="b'0'";
                else
                    comsim="b'1'";

                if(name.isEmpty() || start.isEmpty() || end.isEmpty() || Integer.parseInt(end)<=0 || rate_persent.equals("0.00") || (join.isEmpty() && monthly.isEmpty()))
                    Toast.makeText(getActivity(), "모든 내용을 입력해 주세요. :-)", Toast.LENGTH_SHORT).show();
                else{
                    if(!v.getTag().toString().equals("add")){
                        // 실패달의 조건
                        String[] split = start.split("-");
                        long now = System.currentTimeMillis();  // 현재시간
                        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String now_date = CurDateFormat.format(new Date(now));
                        String[] data_split = now_date.split("-");
                        int month_num = (Integer.parseInt(data_split[0])-Integer.parseInt(split[0]))*12 + (Integer.parseInt(data_split[1])-Integer.parseInt(split[1]));
                        if((Integer.parseInt(data_split[0])-Integer.parseInt(split[0]))<0)
                            month_num--;

                        if(month_num<Integer.parseInt(fail)){
                            Toast.makeText(getActivity(), "실패한 달이 시작부터 현재까지의 달보다 많습니다.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    SharedPreferences preferences = getActivity().getSharedPreferences("person", MODE_PRIVATE);
                    String id = preferences.getString("id", "");

                    if(join.isEmpty())  join = "0";
                    if(monthly.isEmpty())  monthly = "0";

                    if(v.getTag().toString().equals("add")){
                        SetGoalHandler handler = new SetGoalHandler(id, name, start, end, rate_persent, comsim, join, monthly);
                        SetGoalDB test = new SetGoalDB("Confirm", id, name, start, end, rate_persent, comsim, join, monthly, handler);
                        test.execute();
                    }
                    else if(data.equals(name)){
                        SetGoalHandler handler = new SetGoalHandler(id, name, start, end, rate_persent, comsim, join, monthly);
                        SetGoalDB test = new SetGoalDB("Update", data, id, name, start, end, rate_persent, comsim, join, monthly, fail, handler);
                        test.execute();
                    }
                    else{
                        SetGoalHandler handler = new SetGoalHandler(id, name, start, end, rate_persent, comsim, join, monthly);
                        SetGoalDB test = new SetGoalDB("Confirm_update", data, id,name, start, end, rate_persent, comsim, join, monthly, fail, handler);
                        test.execute();
                    }
                }

                break;
            case R.id.start:// 날짜선택
                long now = System.currentTimeMillis();  // 현재시간
                String[] date = start_month.getText().toString().split("-");

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        start_month.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth );
                    }
                }, Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2]));
                dialog.show();

                break;
        }
    }

    class SetGoalHandler extends Handler {

        private String origin_name, id, name,  start,  end,  rate,  comsim,  join,  monthly, fail;
        private Goal goal;

        public SetGoalHandler(String id, String name, String start, String end, String rate, String comsim, String join, String monthly){
            this.id = id;
            this.name = name;
            this.start = start;
            this.end = end;
            this.rate = rate;
            this.comsim = comsim;
            this.join = join;
            this.monthly = monthly;
        }

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1: // confirm 인 경우
                case 4 : // update 확인절차 이후 update
                    if(msg.obj.toString().equals("YES")){
                        Toast.makeText(getActivity(), "존재하는 목표이름입니다. 다른 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(msg.what==1){
                            SetGoalDB test = new SetGoalDB("Insert", id, name, start, end, rate, comsim, join, monthly, this);
                            test.execute();
                        }
                        else{
                            SetGoalDB test = new SetGoalDB("Update", origin_name, id, name, start, end, rate, comsim, join, monthly, fail, this);
                            test.execute();
                        }
                    }
                    break;
                case 2: // insert 인 경우
                case 5: // update 인 경우
                    if(msg.obj.toString().equals("OK")){
                        Toast.makeText(getActivity(), "완료 :-)", Toast.LENGTH_SHORT).show();

                        // fragment 종료
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        GoalFragment goal = (GoalFragment)getActivity().getSupportFragmentManager().findFragmentByTag("goal_fragment");
                        GoalFragment new_goal = new GoalFragment();

                        ft.remove(goal);
                        ft.replace(R.id.fragment_goal, new_goal, "goal_fragment");
                        ft.commit();
                    }
                    else{
                        Toast.makeText(getActivity(), "오류가 났습니다.\n오류반복시 건의하여주십시오.", Toast.LENGTH_SHORT).show();
                        Log.i("set goal fail", msg.obj.toString());
                    }
                    break;
                case 3: // update 위한 정보 가져오는 경우
                    if(msg.obj.toString().equals("No Data")){
                        Toast.makeText(getActivity(), "오류가 났습니다.\n오류반복시 건의하여주십시오.", Toast.LENGTH_SHORT).show();
                        Log.i("set goal fail", msg.obj.toString());

                        // fragment 종료
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        GoalAddFragment fragment = (GoalAddFragment) getActivity().getSupportFragmentManager().findFragmentByTag("goal_add_fragment");
                        GoalFragment goal = (GoalFragment)getActivity().getSupportFragmentManager().findFragmentByTag("goal_fragment");
                        ft.remove(fragment);
                        ft.show(goal);
                        ft.commit();
                    }
                    else{
                        try {
                            origin_name = name;

                            String data = msg.obj.toString();
                            JSONObject jObject= new JSONObject(data);

                            goal_name.setText(name);
                            start=jObject.getString("START_MONTH").toString();
                            start_month.setText(start);
                            end = jObject.getString("GOAL_MONTH").toString();
                            month.setProgress(Integer.parseInt(end));
                            rate = jObject.getString("CONT_RATE").toString().replace("%","");
                            rate_seekBar.setProgress(Integer.parseInt(jObject.getString("CONT_RATE").toString().replace(".",""))/10);
                            comsim = jObject.getString("START_MONTH").toString();
                            if(comsim.equals("0")) {
                                com_sim = true;
                                com.isChecked();
                            }
                            else{
                                com_sim = false;
                                sim.isChecked();
                            }
                            join=jObject.getString("START_SUM").toString();
                            join_sum.setText(join);
                            monthly=jObject.getString("MONTHLY_SUM").toString();
                            monthly_sum.setText(monthly);
                            fail=jObject.getString("FAIL_MONTH").toString();
                            fail_month_num.setProgress(Integer.parseInt(fail));

                            goal = new Goal(id, name, end, join, monthly, rate, comsim, start, fail);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }
}

class SetGoalDB extends AsyncTask<Void, Integer, Void> {

    private String data, string, type;
    private GoalAddFragment.SetGoalHandler handler;
    private URL url;

    public SetGoalDB(String type, String id, String name, GoalAddFragment.SetGoalHandler handler){
        try {
            url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/getGoal2.php");
            string = "u_id="+id+"&u_name="+name;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.type = type;
        this.handler = handler;
    }

    public SetGoalDB(String type, String id, String name, String start, String end, String rate, String comsim, String join, String monthly, GoalAddFragment.SetGoalHandler handler){
        this.type = type;
        try {
            if(type.contains("Confirm")) {
                url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/ConfirmGoal.php");
                string = "u_query=SELECT USR_ID FROM GOAL WHERE USR_ID='" + id + "' AND GOAL_NAME='" + name + "'";
            }
            else {
                url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/SetDepContent.php");
                string = "u_query=INSERT INTO GOAL VALUES ('" + id + "','" + name + "','" + end + "','" + join + "','" + monthly + "','" + rate + "'," + comsim + ",'" + start + "','0')";
            }
        } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        this.handler = handler;
    }

    public SetGoalDB(String type, String origin, String id, String name, String start, String end, String rate, String comsim, String join, String monthly, String fail, GoalAddFragment.SetGoalHandler handler){
        this.type = type;
        try {
            if(type.contains("Confirm")) {
                url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/ConfirmGoal.php");
                string = "u_query=SELECT USR_ID FROM GOAL WHERE USR_ID='" + id + "' AND GOAL_NAME='" + origin + "'";
            }
            else{
                url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/SetDepContent.php");
                string = "u_query=UPDATE GOAL SET GOAL_NAME='" + name + "', GOAL_MONTH='" + end + "', START_SUM='" + join + "', MONTHLY_SUM='" + monthly + "', " +
                        "CONT_RATE='" + rate + "', COM_SIM=" + comsim + ", START_MONTH='" + start + "', FAIL_MONTH='"+fail+"' WHERE USR_ID='"+id+"' AND GOAL_NAME='" +origin+ "'";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {

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
                Log.i("Result", type+", "+data);
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
        if(type.equals("Confirm"))
            msg.what = 1;
        else if(type.equals("Get"))
            msg.what = 3;
        else if(type.equals("Confirm_update"))
            msg.what = 4;
        else if(type.equals("Update"))
            msg.what = 5;
        else
            msg.what = 2;
        msg.obj = data;
        handler.sendMessage(msg);
    }
}
