package com.example.sjeong.pick.Setting;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.sjeong.pick.R;
import com.example.sjeong.pick.RequestHttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by mijin on 2017-11-29.
 */

public class SearchSettingActivity extends AppCompatActivity {
    ArrayAdapter<CharSequence> adspin;
    String bank = null;
    String join_target = null;
    String rate = null;
    RadioGroup radioGroup;
    EditText editText;
    Spinner spinner;
    int no;
    String id;
    AlertDialog.Builder builder2, builder4;
    ///bankList = getResources().getStringArray(R.array.bank);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting);

        SharedPreferences prefs = getSharedPreferences("person", MODE_PRIVATE);
        id = prefs.getString("id", null);

        String userUrl = "http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/getUser.php?";
        ContentValues contentValues = new ContentValues();
        contentValues.put("usr_id", id);


        TextView title2 = (TextView) findViewById(R.id.title2);
        title2.setText(id + "님의 개인정보 설정");

        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && view != null) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        ImageButton back_to_main = (ImageButton) findViewById(R.id.back_to_main);
        back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        NetworkTask networkTask = new NetworkTask(userUrl, contentValues);
        networkTask.execute();

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/setUser.php?";
                //String url = "http://13.58.182.123/setU.php";
                float cont_rate;
                ContentValues values = new ContentValues();

                rate = editText.getEditableText().toString();
                //rate = editText.getText().toString();
                if (rate != null && !rate.equals("")) {
                    cont_rate = Float.parseFloat(rate);
                } else {
                    cont_rate = 0;
                }


                values.put("join_target", join_target);
                values.put("cont_rate", cont_rate);
                values.put("bank", bank);
                values.put("usr_id", "pick");


                NetworkTask networkTask = new NetworkTask(url, values);

                networkTask.execute();
            }
        });
        radioGroup = (RadioGroup) findViewById(R.id.radioButton);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {


                switch (checkedId) {
                    case R.id.radioButton1:
                        join_target = "100";
                        break;
                    case R.id.radioButton2:
                        join_target = "010";
                        break;
                    case R.id.radioButton3:
                        join_target = "001";
                        break;
                    default:
                        join_target = "000";
                }


            }
        });


        editText = (EditText) findViewById(R.id.profit);


        spinner = (Spinner) findViewById(R.id.spinner);


        spinner.setPrompt("주거래 은행");

        adspin = ArrayAdapter.createFromResource(getApplicationContext(), R.array.bank, android.R.layout.simple_spinner_item);

        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adspin);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bank = parent.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

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
                JSONArray jarr = new JSONArray(jsonn);
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject json = (JSONObject) jarr.get(i);
                    if (json.getString("join_target") != null)
                        switch (json.getInt("join_target")) {
                            case 4:
                                radioGroup.check(R.id.radioButton1);
                                break;
                            case 2:
                                radioGroup.check(R.id.radioButton2);
                                break;
                            case 1:
                                radioGroup.check(R.id.radioButton3);
                                break;
                            default:
                                radioGroup.clearCheck();
                        }


                    Double cont_rate = json.getDouble("cont_rate");
                    if (cont_rate != null) editText.setText(cont_rate.toString());
                    else editText.getText();

                    if (json.getString("bank") != null) {
                        if (json.getString("bank").contains("신한"))
                            spinner.setSelection(0);
                        else if (json.getString("bank").contains("우리"))
                            spinner.setSelection(1);
                        else if (json.getString("bank").contains("국민"))
                            spinner.setSelection(2);
                        else if (json.getString("bank").contains("KEB"))
                            spinner.setSelection(3);
                        else if (json.getString("bank").contains("씨티"))
                            spinner.setSelection(4);
                        else if (json.getString("bank").contains("NH"))
                            spinner.setSelection(5);
                        else if (json.getString("bank").contains("스탠다드"))
                            spinner.setSelection(6);
                        else if (json.getString("bank").contains("기업"))
                            spinner.setSelection(7);
                        else if (json.getString("bank").contains("우체국"))
                            spinner.setSelection(8);
                        else if (json.getString("bank").contains("수협"))
                            spinner.setSelection(9);
                        else if (json.getString("bank").contains("KDB"))
                            spinner.setSelection(10);
                        else if (json.getString("bank").contains("경남"))
                            spinner.setSelection(11);
                        else if (json.getString("bank").contains("대구"))
                            spinner.setSelection(12);
                        else if (json.getString("bank").contains("전북"))
                            spinner.setSelection(13);
                        else if (json.getString("bank").contains("부산"))
                            spinner.setSelection(14);
                        else if (json.getString("bank").contains("제주"))
                            spinner.setSelection(15);
                        else if (json.getString("bank").contains("광주"))
                            spinner.setSelection(16);
                        else if (json.getString("bank").contains("케이"))
                            spinner.setSelection(17);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
