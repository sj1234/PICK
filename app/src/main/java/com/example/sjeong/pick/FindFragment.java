package com.example.sjeong.pick;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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

public class FindFragment extends Fragment implements View.OnClickListener {

    private EditText email_id, id_pw, email_pw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);

        email_id = (EditText)view.findViewById(R.id.email_id);
        id_pw = (EditText)view.findViewById(R.id.id_pw);
        email_pw = (EditText)view.findViewById(R.id.email_pw);

        Button find_id = (Button)view.findViewById(R.id.find_id);
        find_id.setOnClickListener(this);
        Button find_pw = (Button)view.findViewById(R.id.find_pw);
        find_pw.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        String id, email;

        switch(v.getId()){

            case R.id.find_id:
                email = email_id.getText().toString();

                if(email.isEmpty())
                    Toast.makeText(getActivity(), "EMAIL을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                else {
                    FindHandler handler = new FindHandler();
                    FindDB test = new FindDB("ID", email, "", handler);
                    test.execute();
                }
                break;
            case R.id.find_pw:
                id = id_pw.getText().toString();
                email = email_pw.getText().toString();

                if(id.isEmpty() || email.isEmpty())
                    Toast.makeText(getActivity(), "모든 정보를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                else{
                    FindHandler handler = new FindHandler();
                    FindDB test = new FindDB("PW", email, id, handler);
                    test.execute();
                }
                break;
        }
    }

    class FindHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String info = msg.obj.toString();

            switch(msg.what){
                case 1: //아이디 찾기
                    if(info.equals("NO")) // 해당 이메일의 user가 없는 경우
                        Toast.makeText(getActivity(), "해당 정보의 USER가 존재하지 않습니다.\n다시 입력해주십시오.", Toast.LENGTH_SHORT).show();
                    else if(info.contains("Error:")) {
                        Log.i("find id error", info);
                        Toast.makeText(getActivity(), "오류가 발생하였습니다.\n반복시 문의해주십시오", Toast.LENGTH_SHORT).show();
                    }
                    else{ // ID 있는 경우
                        String ID= info.substring(0, info.length()-1).replace("Your ID is ", "").replace(",", "\n");

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("존재하는 아이디");
                        builder.setMessage(ID);
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                    break;
                case 2: // 비밀번호 찾기
                    if(info.equals("NO")) // 해당 정보의 user가 없는 경우
                        Toast.makeText(getActivity(), "해당 정보의 USER가 존재하지 않습니다.\n다시 입력해주십시오.", Toast.LENGTH_SHORT).show();
                    else if(info.contains("Error:")) {
                        Log.i("find pw error", info);
                        Toast.makeText(getActivity(), "오류가 발생하였습니다.\n반복시 문의해주십시오", Toast.LENGTH_SHORT).show();
                    }
                    else{ // 정보 변경
                        String ID=info.replace("Your ID is ", "").replace(",", "");

                        ChangePWFragment fragment = new ChangePWFragment();
                        Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
                        bundle.putString("ID", ID);
                        fragment.setArguments(bundle);

                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.find_fragment, fragment);
                        ft.commit();
                    }
                    break;
                default: // 없는정보
                    break;
            }
        }
    }
}

class FindDB extends AsyncTask<Void, Integer, Void> {

    private String type, string, data;
    private FindFragment.FindHandler handler;

    public FindDB(String type, String EMAIL, String ID, FindFragment.FindHandler handler){
        this.type = type;
        if(type.equals("ID"))
            string = "u_query=SELECT USR_ID FROM USER WHERE EMAIL='"+EMAIL+"'";
        else
            string = "u_query=SELECT USR_ID FROM USER WHERE USR_ID='"+ID+"' AND EMAIL='"+EMAIL+"'";
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL("http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/FindInfo.php");
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
        if(type.equals("ID"))
            msg.what=1;
        else
            msg.what=2;
        msg.obj = data;
        handler.sendMessage(msg);
    }
}
