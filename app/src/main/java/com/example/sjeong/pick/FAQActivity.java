package com.example.sjeong.pick;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class FAQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        // toolbar 설정
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        // 이메일 보내기
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.goal_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "구현예정 :-)", Toast.LENGTH_SHORT).show();
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                String[] address = {"email@address.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                startActivity(email);
            }
        });

        ListView FAQ_list = (ListView) findViewById(R.id.FAQ_list);
        GetFAQHandler handler = new GetFAQHandler(FAQ_list, this);
        GetFAQDB test = new GetFAQDB(handler);
        test.execute();
    }

    // 메뉴 클릭 listener
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

class FAQ{
    private String question, answer;

    public FAQ(String question, String answer){
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {return question;}

    public String getAnswer() {return answer;}

}

class FAQAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private ArrayList<FAQ> arraylist;

    public FAQAdapter(Context context, ArrayList<FAQ> arraylist){
        this.context = context;
        this.arraylist = arraylist;
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
            convertView = inflater.inflate(R.layout.list_faqadapter, parent, false);
        }

        TextView question = (TextView)convertView.findViewById(R.id.question);
        question.setText(arraylist.get(position).getQuestion());
        TextView answer = (TextView)convertView.findViewById(R.id.answer);
        answer.setText(arraylist.get(position).getAnswer());

        LinearLayout answer_layout = (LinearLayout)convertView.findViewById(R.id.answer_layout);
        answer_layout.setTag(position+"answer_layout");
        answer_layout.setVisibility(View.GONE); // answer 안보이기

        // qustion 클릭시 answer 보이도록
        LinearLayout question_layout = (LinearLayout)convertView.findViewById(R.id.question_layout);
        question_layout.setTag(position);
        question_layout.setOnClickListener(this);

        return convertView;
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.question_layout){
            View total = (View)(v.getParent()).getParent();
            LinearLayout answer_layout =(LinearLayout)total.findViewWithTag(v.getTag().toString()+"answer_layout");
            if(answer_layout.getVisibility()==View.VISIBLE)
                answer_layout.setVisibility(View.GONE);
            else
                answer_layout.setVisibility(View.VISIBLE);
        }
    }
}

class GetFAQHandler extends Handler {

    private ListView view;
    private Context context;

    public GetFAQHandler(ListView view, Context context){
        this.view = view;
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg){
        super.handleMessage(msg);
        String json = msg.obj.toString();
        json = "["+json.substring(0, json.length()-1)+"]";
        ArrayList<FAQ> faqs = new ArrayList<FAQ>();

        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jObject = jsonArray.getJSONObject(i);
                faqs.add(new FAQ(jObject.get("QUESTION").toString(), jObject.get("ANSWER").toString()));
            }
            FAQAdapter faqAdapter = new FAQAdapter(context, faqs);
            view.setAdapter(faqAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

class GetFAQDB extends AsyncTask<Void, Integer, Void> {

    private String data, string;
    private GetFAQHandler handler;

    public GetFAQDB(GetFAQHandler handler){
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/getFAQ.php");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(conn !=null){
                conn.setConnectTimeout(10000);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

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
        msg.obj = data;
        handler.sendMessage(msg);
    }
}
