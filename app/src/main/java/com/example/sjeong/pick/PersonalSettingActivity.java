package com.example.sjeong.pick;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mijin on 2017-11-28.
 */

public class PersonalSettingActivity extends AppCompatActivity {
    String url;
    ContentValues values;
    String id;

    EditText now_pw, new_pw, new_pw2, email;
    Button save;

    NetworkTask1 networkTask1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personal_setting);

        SharedPreferences prefs = getSharedPreferences("PrefName",MODE_PRIVATE);
        //id = prefs.getString("id",null);
        id="pick";

        now_pw = (EditText) findViewById(R.id.now_pw);
        new_pw = (EditText) findViewById(R.id.new_pw);
        new_pw2 = (EditText) findViewById(R.id.new_pw2);
        email = (EditText) findViewById(R.id.email);

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/updatePersonal.php?";
                values = new ContentValues();
                values.put("id",id);

                if(!now_pw.getText().toString().equals("")&&!email.getText().toString().equals("")) {
                    values.put("now_pw", now_pw.getText().toString());
                    values.put("email", email.getText().toString());
                    if (!new_pw.getText().toString().equals("") &&!new_pw2.getText().toString().equals("")){
                        if (new_pw.getText().toString().equals(new_pw2.getText().toString())) {
                            values.put("new_pw", new_pw.getText().toString());
                            networkTask1 = new NetworkTask1(url, values);
                            networkTask1.execute();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PersonalSettingActivity.this);
                            builder.setMessage("새 비밀번호가 일치하지 않습니다.");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder.show();
                        }
                    }else if (new_pw.getText().toString().equals("") && new_pw2.getText().toString().equals("")) {
                        networkTask1 = new NetworkTask1(url, values);
                        networkTask1.execute();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalSettingActivity.this);
                        builder.setMessage("새 비밀번호 정보를 모두 입력해주세요.");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(PersonalSettingActivity.this);
                    builder.setMessage("비밀번호와 이메일은 필수 입력입니다.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }
            }
        });


        load();


    }

    public void load(){
        url = "http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/getPersonal.php?";
        values = new ContentValues();
        values.put("id",id);
        networkTask1 = new NetworkTask1(url, values);
        networkTask1.execute();

    }

    public class NetworkTask1 extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask1(String url, ContentValues values) {

            this.url = url;
            this.values = values;

        }

        @Override
        protected String doInBackground(Void... params) {


            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);
            // 해당 URL로 부터 결과물을 얻어온다.


            return result;
        }

        @Override
        protected void onPostExecute(String jsonn) {
            super.onPostExecute(jsonn);

            try {
                if(jsonn!=null){
                  if(jsonn.trim().equals("err1")) {
                      AlertDialog.Builder builder = new AlertDialog.Builder(PersonalSettingActivity.this);
                      builder.setMessage("비밀번호가 일치하지 않습니다.");
                      builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {

                          }
                      });
                      builder.show();
                  }else if(jsonn.trim().equals("success")){
                      AlertDialog.Builder builder = new AlertDialog.Builder(PersonalSettingActivity.this);
                      builder.setMessage("회원정보가 변경되었습니다.");
                      builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              now_pw.setText("");
                              new_pw.setText("");
                              new_pw2.setText("");
                          }
                      });
                      builder.show();
                      load();
                  }else{
                      JSONObject json = new JSONObject(jsonn);
                      if(json!=null){
                          email.setText(json.get("email").toString());
                      }
                  }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
