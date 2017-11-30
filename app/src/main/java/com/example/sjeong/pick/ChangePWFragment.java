package com.example.sjeong.pick;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


public class ChangePWFragment extends Fragment implements View.OnClickListener {

    private EditText change_pw, confirm_pw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_pw, container, false);

        change_pw = (EditText)view.findViewById(R.id.change_pw);
        confirm_pw= (EditText)view.findViewById(R.id.confirm_pw);

        Button change=(Button)view.findViewById(R.id.change_pwB);
        change.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_pwB:
                String change_pw_string = change_pw.getText().toString();
                String confirm_pw_string = confirm_pw.getText().toString();

                if(change_pw_string.isEmpty()||confirm_pw_string.isEmpty()) // 모든항목을 입력하지 않은 경우
                    Toast.makeText(getActivity(), "모든항목을 입력해주십시오.", Toast.LENGTH_SHORT).show();
                else if(!change_pw_string.equals(confirm_pw_string)) // 비밀번호와 확인이 일치하지 않는 경우
                    Toast.makeText(getActivity(), "비밀번호가 일치하지 않습니다.\n다시 입력해주십시오.", Toast.LENGTH_SHORT).show();
                else{ // 비밀번호 변경
                    String ID = getArguments().getString("ID"); // 이전 fragment에서 전달한 ID 값
                    ChangeHandler handler = new ChangeHandler();
                    ChangePWDB test = new ChangePWDB(ID, change_pw_string, handler);
                    test.execute();
                }
                break;
        }
    }

    class ChangeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.obj.toString().equals("OK")){
                Toast.makeText(getActivity(), "비밀번호가 변경되었습니다. :-)", Toast.LENGTH_SHORT).show();
                getActivity().finish(); // 메인화면으로 돌아가기
            }
            else{
                Log.i("change pw error", msg.obj.toString());
                Toast.makeText(getActivity(), "오류가 발생하였습니다.\n반복시 문의해주십시오", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

class ChangePWDB extends AsyncTask<Void, Integer, Void> {

    private String data, string;
    private ChangePWFragment.ChangeHandler handler;

    public ChangePWDB(String id, String pw, ChangePWFragment.ChangeHandler handler){
        string = "u_query=UPDATE USER SET PW='"+pw+"' WHERE USR_ID='"+id+"'";
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Void... params) {

        Log.i("SetInterest DB", string);
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
        msg.obj=data;
        handler.sendMessage(msg);
    }
}
