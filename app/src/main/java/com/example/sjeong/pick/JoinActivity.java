package com.example.sjeong.pick;


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class JoinActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText id, pw, email, confirm_pw;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        context = this;

        id = (EditText)findViewById(R.id.id);
        pw = (EditText)findViewById(R.id.pw);
        confirm_pw = (EditText)findViewById(R.id.confirm_pw);
        email = (EditText)findViewById(R.id.email);

        id.addTextChangedListener(new TextWatcher() {

            @Override // 입력전
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override // 입력되는 텍스트 변화
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView confirm_ID_text = (TextView)findViewById(R.id.confirm_ID_text);
                confirm_ID_text.setText("* ID 중복확인을 해주세요.");
                confirm_ID_text.setTextColor(Color.RED);
            }

            @Override // 입력 후
            public void afterTextChanged(Editable s) {}
        });

        pw.addTextChangedListener(new TextWatcher() {

            TextView confirm_pw_text = (TextView)findViewById(R.id.confirm_pw_text);

            @Override // 입력전
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override // 입력되는 텍스트 변화
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirm_pw_text.setText("* 비밀번호가일치하지 않습니다.");
                confirm_pw_text.setTextColor(Color.RED);
            }

            @Override // 입력 후
            public void afterTextChanged(Editable s) {
                if(confirm_pw.getText().toString().equals(pw.getText().toString())){
                    confirm_pw_text.setText("* 비밀번호가 일치합니다.");
                    confirm_pw_text.setTextColor(Color.BLACK);
                }

            }
        });

        confirm_pw.addTextChangedListener(new TextWatcher() {
            TextView confirm_pw_text = (TextView)findViewById(R.id.confirm_pw_text);

            @Override // 입력전
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override // 입력되는 텍스트 변화
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirm_pw_text.setText("* 비밀번호가일치하지 않습니다.");
                confirm_pw_text.setTextColor(Color.RED);
            }

            @Override // 입력 후
            public void afterTextChanged(Editable s) {
                if(confirm_pw.getText().toString().equals(pw.getText().toString())){
                    confirm_pw_text.setText("* 비밀번호가 일치합니다.");
                    confirm_pw_text.setTextColor(Color.BLACK);
                }

            }
        });

        ImageButton back_to_login = (ImageButton)findViewById(R.id.back_to_login); // 로그인화면으로 돌아가기
        ImageButton confirm = (ImageButton)findViewById(R.id.confirm_ID); // ID 중복확인
        Button join = (Button)findViewById(R.id.join); // 가입하기

        back_to_login.setOnClickListener(this);
        confirm.setOnClickListener(this);
        join.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        // ID 중복확인 핸들러
        Handler confirm_handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                switch(msg.what){
                    case 2: // 중복 존재하지 않음
                        TextView confirm_ID_text = (TextView)findViewById(R.id.confirm_ID_text);
                        confirm_ID_text.setText("* 사용가능한 ID 입니다.");
                        confirm_ID_text.setTextColor(Color.BLACK);
                        break;
                    case 3: // 중복 존재
                        Toast.makeText(context, "존재하는 ID 입니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        // 가입 핸들러
        Handler join_handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                switch(msg.what){
                    case 4: // 가입완료
                        Toast.makeText(context, "환영합니다! 가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case 5: // 오류
                        Toast.makeText(context, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        switch(v.getId()){
            case R.id.back_to_login: // 로그인화면으로 돌아가기
                finish();
                break;
            case R.id.confirm_ID: // ID 중복확인
                if(id.getText().toString().isEmpty()) // ID입력 안 한경우
                    Toast.makeText(context, "ID를 입력하십시오.", Toast.LENGTH_SHORT).show();
                else{
                    ConfirmIDDB test = new ConfirmIDDB(id.getText().toString(), confirm_handler);
                    test.execute();
                }
                break;
            case R.id.join:  // 가입하기
                TextView confirm_ID_text = (TextView)findViewById(R.id.confirm_ID_text);
                TextView confirm_pw_text = (TextView)findViewById(R.id.confirm_pw_text);
                if(!id.getText().toString().isEmpty() && !pw.getText().toString().isEmpty() && !confirm_pw.getText().toString().isEmpty() && confirm_pw_text.getText().toString().equals("* 비밀번호가 일치합니다.") && !email.getText().toString().isEmpty() && confirm_ID_text.getText().toString().contains("사용가능한")){ // 필수항목 입력여부
                    JoinDB test = new JoinDB(id.getText().toString(), pw.getText().toString(), email.getText().toString(), join_handler);
                    test.execute();
                }
                else if(!confirm_ID_text.getText().toString().contains("사용가능한"))
                    Toast.makeText(context, "ID 중복여부를 확인해 주십시오.", Toast.LENGTH_SHORT).show();
                else if(!confirm_pw_text.getText().toString().equals("* 비밀번호가 일치합니다."))
                    Toast.makeText(context, "비밀번호 일치여부를 확인하여 주십시오.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "모든항목을 입력하십시오.", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

// 중복 ID 여부 확인
class ConfirmIDDB extends AsyncTask<Void, Integer, Void> {

    private String ID, data;
    private Handler handler;

    public ConfirmIDDB(String ID, Handler handler){
        this.ID = ID;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Void... params) {

        String string = "u_id="+ID+"";

        try {
            URL url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/confirmID.php");
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
                Log.i("ConfirmID Data",data);
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
        if(data.contains("OK"))  // 같은 ID가 존재하지 않는 경우
            msg.what=2;
        else // 존재하는 경우
            msg.what=3;

        handler.sendMessage(msg);
    }
}

// 가입하기 위해 정보 전송
class JoinDB extends AsyncTask<Void, Integer, Void> {

    private String ID, PW, EMAIL, data;
    private Handler handler;

    public JoinDB(String ID, String PW, String EMAIL, Handler handler){
        this.ID = ID;
        this.PW = PW;
        this.EMAIL = EMAIL;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Void... params) {

        String string = "u_query=INSERT INTO USER(USR_ID, PW, EMAIL) VALUES ('"+ID+"','"+PW+"','"+EMAIL+"')";

        try {
            URL url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/SetDepContent.php");
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
                Log.i("Join Data",data);
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
        if(data.contains("OK")) // 가입성공
            msg.what=4;
        else
            msg.what=5;

        handler.sendMessage(msg);
    }
}
