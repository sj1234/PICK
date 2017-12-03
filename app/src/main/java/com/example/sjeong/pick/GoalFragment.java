package com.example.sjeong.pick;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class GoalFragment extends Fragment {

    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("person", MODE_PRIVATE);
        String id = preferences.getString("id", "");

        // 뒤로 가기
        ImageButton back_to_main = (ImageButton)view.findViewById(R.id.back_to_main);
        back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        // 목표 이름설정
        TextView title = (TextView)view.findViewById(R.id.goal_title);
        title.setText("'"+id+"'님의 목표");

        ListView listView = (ListView)view.findViewById(R.id.list_goal);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState){
                if (scrollState==SCROLL_STATE_IDLE)
                    fab.show();
                else
                    fab.hide();
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
        });

        fab = (FloatingActionButton)view.findViewById(R.id.goal_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                GoalAddFragment goaladd = new GoalAddFragment();

                Bundle bundle = new Bundle(1);
                bundle.putString("data", "");
                goaladd.setArguments(bundle);
                ft.hide(getActivity().getSupportFragmentManager().findFragmentByTag("goal_fragment"));
                ft.add(R.id.fragment_goal, goaladd, "goal_add_fragment");
                ft.commit();
            }
        });

        // DB에서 정보 가져오기
        GetGoalHandler handler = new GetGoalHandler(listView, getActivity());
        GetGoalDB test = new GetGoalDB(id, handler);
        test.execute();

        return view;
    }

    class GetGoalHandler extends Handler {

        private ListView view;
        private Context context;

        public GetGoalHandler(ListView view, Context context){
            this.view = view;
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);

            SharedPreferences preferences = getActivity().getSharedPreferences("person", MODE_PRIVATE);
            String id = preferences.getString("id", "");

            switch(msg.what){
                case 10: // 성공
                    ArrayList<Goal> goals = new ArrayList<Goal>();
                    String text = msg.obj.toString();
                    View.OnClickListener listener = null;

                    // listAdapter에 정보 저장, Goal 데이터가 있는 경우
                    if(!text.contains("No Data")){
                        text = "["+text.substring(0, text.length()-1)+"]";

                        JSONArray jarray = null;
                        try {
                            jarray = new JSONArray(text);
                            for(int i=0; i<jarray.length(); i++){
                                JSONObject jObject = jarray.getJSONObject(i);
                                goals.add(new Goal(id, jObject.get("GOAL_NAME").toString(), jObject.get("GOAL_MONTH").toString(), jObject.get("START_SUM").toString(), jObject.get("MONTHLY_SUM").toString(),
                                        jObject.get("CONT_RATE").toString(), jObject.get("COM_SIM").toString(), jObject.get("START_MONTH").toString(), jObject.get("FAIL_MONTH").toString()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("Goal Error", e.toString());
                        }

                        listener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String data = v.getTag().toString();

                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                GoalAddFragment goaladd = new GoalAddFragment();
                                Bundle bundle = new Bundle(1);
                                bundle.putString("data", data);
                                goaladd.setArguments(bundle);

                                ft.hide(getActivity().getSupportFragmentManager().findFragmentByTag("goal_fragment"));
                                ft.add(R.id.fragment_goal, goaladd, "goal_add_fragment");
                                ft.commit();
                            }
                        };
                    }
                    else{ // 데이터가 없는경우
                        long now = System.currentTimeMillis();  // 현재시간
                        Date date = new Date(now);
                        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        goals.add(new Goal(id, "목표를 만들어주세요!", "12", "0", "10000", "2.0","0", CurDateFormat.format(date), "0"));
                    }

                    // ListView 에 목표내용 출력
                    GoalAdapter goalAdapter = new GoalAdapter(context, goals, this, listener);
                    view.setAdapter(goalAdapter);
                    break;
                case 11: // 삭제요청
                    GetGoalDB test = new GetGoalDB(id, this);
                    test.execute();
                    break;
            }
        }
    }
}

class GetGoalDB extends AsyncTask<Void, Integer, Void> {

    private String data, string, url_string;
    private GoalFragment.GetGoalHandler handler;

    public GetGoalDB(String string, GoalFragment.GetGoalHandler handler){
        this.string = "u_id="+string;
        this.handler = handler;
        this.url_string = "getGoal.php";
    }

    public GetGoalDB(String id, String name, GoalFragment.GetGoalHandler handler){
        this.string = "u_query=DELETE FROM GOAL WHERE USR_ID='"+id+"' AND GOAL_NAME='"+name+"'";
        this.handler = handler;
        this.url_string = "SetDepContent.php";
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/"+url_string);

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
       if(url_string.equals("getGoal.php")) msg.what = 10;
        else msg.what = 11;
        msg.obj = data;
        handler.sendMessage(msg);
    }
}

class GoalAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Goal> arraylist;
    private View.OnClickListener listener;
    private GoalFragment.GetGoalHandler handler;

    public GoalAdapter(Context context, ArrayList<Goal> arraylist, GoalFragment.GetGoalHandler handler,View.OnClickListener listener){
        this.context = context;
        this.arraylist = arraylist;
        this.listener = listener;
        this.handler = handler;
    }

    //리스트 객체 내의 item의 갯수를 반환해주는 함수. 리스트 객체의 size를 반환해주면된다
    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // ListView의 항목들을 출력하는 함수 position : 해당되는 항목의 Adapter에서의 위치값  convertView : 재사용할 항목의 View   arent : 항목의 View들을 포함하고 있는 ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_goaladapter, parent, false);
            convertView.setTag(position+"");
        }

        SwipeLayout swipeLayout =  (SwipeLayout) convertView.findViewById(R.id.swipeGoal);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        LinearLayout bottom_wrapper = (LinearLayout)convertView.findViewById(R.id.bottom_wrapper);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, bottom_wrapper);

        ImageButton button = (ImageButton) convertView.findViewById(R.id.delete);
        button.setTag(position+"");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 삭제
                int position = Integer.parseInt(v.getTag().toString());
                deletegoal listener = new deletegoal(arraylist.get(position).getId().toString(), arraylist.get(position).getName().toString(), handler);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("DELETE")
                        .setMessage("해당 목표를 삭제하시겠습니까?")
                        .setPositiveButton("확인", listener).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                });
                builder.show();
            }
        });

        TextView date = (TextView)convertView.findViewById(R.id.goal_date);
        TextView name = (TextView)convertView.findViewById(R.id.goal_name);
        TextView persent = (TextView)convertView.findViewById(R.id.goal_persent);
        TextView sum = (TextView)convertView.findViewById(R.id.goal_sum);
        ProgressBar pb = (ProgressBar)convertView.findViewById(R.id.goal_pb);

        String start_month = arraylist.get(position).getStart().toString();
        String end_month = arraylist.get(position).getMonth().toString();

        // 종료 일자 계산 및 data 출력
        String[] split = start_month.split("-");
        int month = Integer.parseInt(split[1])+Integer.parseInt(end_month) + Integer.parseInt(arraylist.get(position).getFail().toString());
        int year = Integer.parseInt(split[0]) + (month/12);
        month = month%12;
        if(month==0){ month=12; year--; }
        date.setText(split[0]+". "+split[1]+". "+split[2]+" - "+year+". "+month+". "+split[2]);

        // 목표 이름 출력
        name.setText(arraylist.get(position).getName().toString());

        // persent 계산 및 출력
        long now = System.currentTimeMillis();  // 현재시간
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String now_date = CurDateFormat.format(new Date(now));

        int month_int, persent_int;
        String[] data_split = now_date.split("-");
        int year_gap = Integer.parseInt(data_split[0])-Integer.parseInt(split[0]);
        int month_gap = Integer.parseInt(data_split[1])-Integer.parseInt(split[1]) ;
        int day_gap = Integer.parseInt(data_split[2])-Integer.parseInt(split[2]);
        if(year_gap==0){
            if(month_gap==0)
                persent_int=0;
            else{
                persent_int=month_gap;
                if(day_gap<0)
                    persent_int-=1;
            }
        }
        else{
            if(month_gap<0){
                persent_int=((year_gap-1)*12)+(12-month)+Integer.parseInt(data_split[1]);
                if(day_gap<0)
                    persent_int-=1;
            }
            else{
                persent_int=(year_gap*12) + month_gap;
                if(day_gap<0)
                    persent_int-=1;
            }
        }
        persent_int -= Integer.parseInt(arraylist.get(position).getFail().toString());
        month_int = persent_int;
        persent_int = (persent_int*100)/Integer.parseInt(end_month);
        persent.setText(persent_int+"%");

        TextView gap = (TextView)convertView.findViewById(R.id.goal_gap);
        TextView gap2 = (TextView)convertView.findViewById(R.id.goal_gap2);
        float a= (float)persent_int/10;
        gap.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, a));
        gap2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 10f-a));

        // progressbar 설정
        pb.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#fe6186")));
        pb.setProgress(persent_int);

        // sum 예상금액 설정
        double start =  Double.parseDouble(arraylist.get(position).getStart_sum());
        double monthly =  Double.parseDouble(arraylist.get(position).getMonthly());
        double rate_float = Double.parseDouble(arraylist.get(position).getRate())*(0.01);
        double before_tax__sum, after_tax__sum;

        if(arraylist.get(position).getCom_sim().equals("0")) { //단리인 경우
            if(monthly==0){ // 예금인경우
                before_tax__sum = start*month_int*(rate_float)/12.0;
                after_tax__sum = start + (before_tax__sum*0.846);
                before_tax__sum += start;
            }
            else{
                before_tax__sum = (monthly*(month_int+(month_int+1))*0.5*(rate_float/12))*0.846;
                after_tax__sum = start + monthly*month_int + (before_tax__sum*0.846);
                before_tax__sum += (monthly*month_int) + start;
            }
        }
        else{
            if(monthly==0) { // 예금인경우
                before_tax__sum = start;
                for(int i=0; i<month_int; i++){
                    before_tax__sum *= (1.0+(rate_float/12));
                }
                after_tax__sum = start + ((before_tax__sum - start)*0.846);
            }
            else{
                before_tax__sum = 0.0;
                for(int i=0; i<month_int; i++){
                    before_tax__sum = (before_tax__sum*(rate_float/12)) + (monthly*(rate_float/12));
                }
                after_tax__sum = start + monthly*month_int + (before_tax__sum*0.846);
                before_tax__sum += (monthly*month_int) + start;
            }
        }
        sum.setText("세전 : 약 "+String.format("%,d", (int)before_tax__sum)+"원 / 세후 : 약 "+String.format("%,d", (int)after_tax__sum)+"원");

        // listener
        if(listener != null) {
            LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.goal_list_adapter);
            layout.setTag(arraylist.get(position).getName().toString());
            layout.setOnClickListener(listener);
        }

        return convertView;
    }
}

class deletegoal implements DialogInterface.OnClickListener {

    private String id, name;
    private GoalFragment.GetGoalHandler handler;

    public deletegoal(String id, String name, GoalFragment.GetGoalHandler handler){
        this.id = id;
        this.name = name;
        this.handler = handler;
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        GetGoalDB test = new GetGoalDB(id, name, handler);
        test.execute();
    }
}