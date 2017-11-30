package com.example.sjeong.pick;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText id, pw;
    private Context context;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        id = (EditText)findViewById(R.id.id);
        pw = (EditText)findViewById(R.id.pw);
        Button login  = (Button)findViewById(R.id.login);
        TextView join = (TextView)findViewById(R.id.join);
        TextView find = (TextView)findViewById(R.id.find);

        final CheckBox auto = (CheckBox)findViewById(R.id.auto_login);
        auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { // 자동로그인설정 확인
                if(isChecked){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("자동로그인 설정")
                            .setMessage("APP을 종료하더라도 로그인이 유지될 수 있습니다. 단, 공공장소에서 이용시 개인정보가 유출될 수 있으니 유의하여 주십시오.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            auto.setChecked(false);
                        }
                    });
                    builder.show();
                }
            }
        });

        // login handler
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what==1)
                {
                    String result =  (msg.obj).toString();
                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

                    // 맞는 회원정보인 경우 화면 전환 및 activity 종료
                    if(result.contains("Welcome")){

                        // 자동로그인 설정해둔 경우 id 정보 저장
                        SharedPreferences preferences = getSharedPreferences("person", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        if(auto.isChecked()){
                            editor.putString("auto_login", result.replace("Welcome!", ""));
                        }
                        else{
                            editor.putString("auto_login", "");
                        }
                        editor.putString("id", result.replace("Welcome!", ""));
                        editor.commit();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };

        login.setOnClickListener(this);
        join.setOnClickListener(this);
        find.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login: // 로그인 정보확인 위해 id, pw 전달
                if(id.getText().toString().isEmpty()||pw.getText().toString().isEmpty()){
                    Toast.makeText(context, "ID와 PW를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    LoginDB test = new LoginDB(id.getText().toString(), pw.getText().toString(), handler);
                    test.execute();
                }
                break;
            case R.id.join: // 회원가입
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
                break;
            case R.id.find: // 아아디, 비밀번호 확인
                Intent intent_find = new Intent(LoginActivity.this, FindInfoActivity.class);
                startActivity(intent_find);
                break;
        }
    }
}

class LoginDB extends AsyncTask<Void, Integer, Void> {

    private String ID, PW, data;
    private Handler handler;

    public LoginDB(String ID, String PW, Handler handler){
        this.ID = ID;
        this.PW = PW;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Void... params) {

        String string = "u_id="+ID+"&u_pw="+PW+"";

        try {
            URL url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/login.php");
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
                Log.i("Login Data",data);
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
        msg.what=1;
        handler.sendMessage(msg);
    }
}
