package com.example.sjeong.pick.Trash;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sjeong.pick.R;
import com.example.sjeong.pick.RequestHttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mijin on 2017-08-07.
 */

public class SettingFragment extends Fragment {
    ArrayAdapter<CharSequence> adspin;
    String bank = null;
    String join_target = null;
    String rate = null;
    RadioGroup radioGroup;
    EditText editText;
    Spinner spinner;
    int no;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);

        String userUrl = "http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/getUser.php?";
        ContentValues contentValues = new ContentValues();
        contentValues.put("usr_id","pick");


        NetworkTask networkTask = new NetworkTask(userUrl, contentValues);
        networkTask.execute();

        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/setUser.php?";
                //String url = "http://13.58.182.123/setU.php";
                float cont_rate;
                ContentValues values = new ContentValues();

                rate = editText.getEditableText().toString();
                //rate = editText.getText().toString();
                if(rate!=null&&!rate.equals("")) {
                    cont_rate = Float.parseFloat(rate);
                }else{
                    cont_rate=0;
                }


                values.put("join_target", join_target);
                values.put("cont_rate", cont_rate);
                values.put("bank", bank);
                values.put("usr_id", "pick");


                NetworkTask networkTask = new NetworkTask(url, values);

                networkTask.execute();
            }
        });
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioButton);


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

                Toast.makeText(getContext(), checkedId+":"+join_target, Toast.LENGTH_LONG).show();
            }
        });



        editText = (EditText) rootView.findViewById(R.id.profit);



        spinner = (Spinner) rootView.findViewById(R.id.spinner);

        spinner.setPrompt("주거래 은행");

        adspin = ArrayAdapter.createFromResource(getContext(), R.array.bank, android.R.layout.simple_spinner_item);

        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adspin);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bank = parent.getSelectedItem().toString();
                Toast.makeText(getContext(), bank, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return rootView;

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


