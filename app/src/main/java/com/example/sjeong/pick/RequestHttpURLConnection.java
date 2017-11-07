package com.example.sjeong.pick;

import android.content.ContentValues;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by mijin on 2017-11-06.
 */

public class RequestHttpURLConnection {

    public String request(String _url, ContentValues _params){
        // HttpURLConnection 참조 변수.
        HttpURLConnection urlConn = null;
        // URL 뒤에 붙여서 보낼 파라미터.
        StringBuffer sbParams = new StringBuffer();


        /**
         * 1. StringBuffer에 파라미터 연결
         * */
        // 보낼 데이터가 없으면 파라미터를 비운다.
        if (_params == null)
            sbParams.append("");
            // 보낼 데이터가 있으면 파라미터를 채운다.
        else {
            // 파라미터가 2개 이상이면 파라미터 연결에 &가 필요하므로 스위칭할 변수 생성.
            boolean isAnd = false;
            // 파라미터 키와 값.
            String key;
            String value;

            for (Map.Entry<String, Object> parameter : _params.valueSet()) {
                if (parameter.getValue() != null) {
                    key = parameter.getKey();
                    value = parameter.getValue().toString();


                    // 파라미터가 두개 이상일때, 파라미터 사이에 &를 붙인다.
                    if (isAnd)
                        sbParams.append("&");
                  /*  else
                        sbParams.append("?");
*/
                    sbParams.append(key).append("=").append(value);
                }
                // 파라미터가 2개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 &를 붙인다.
                if (!isAnd)
                    if (_params.size() >= 2)
                        isAnd = true;


            }
        }


        /**
         * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
         * */
        try {

            URL url = new URL(_url);
            urlConn = (HttpURLConnection) url.openConnection();


            // [2-1]. urlConn 설정.
            urlConn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.
            urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
            urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

            // [2-2]. parameter 전달 및 데이터 읽어오기.
            String strParams = sbParams.toString(); //sbParams에 정리한 파라미터들을 스트링으로 저장. 예)id=id1&pw=123;

            Log.d("연결2",strParams);
            /*
            OutputStream os = urlConn.getOutputStream();

            os.write(strParams.getBytes("UTF-8")); // 출력 스트림에 출력.

            Log.d("연결","output 쓰기");
            os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
            Log.d("연결","output 비우기");
            os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.
            Log.d("연결","output 닫기");
*/
            OutputStreamWriter outStream = new OutputStreamWriter(urlConn.getOutputStream(), "EUC-KR");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(strParams);
            writer.flush();
            // [2-3]. 연결 요청 확인.
            // 실패 시 null을 리턴하고 메서드를 종료.
            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;



            // [2-4]. 읽어온 결과물 리턴.
            // 요청한 URL의 출력물을 BufferedReader로 받는다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
            // 출력물의 라인과 그 합에 대한 변수.
            Log.d("연결2", "input 받아오기");


            String line;
            String page = "";

            // 라인을 받아와 합친다.
            while ((line = reader.readLine()) != null) {
                page += line;
            }

            Log.d("연결2", page);


            return page;

        } catch (MalformedURLException e) { // for URL.
            e.printStackTrace();
        } catch (IOException e) { // for openConnection().
            e.printStackTrace();
        }finally {
            if (urlConn != null)
                urlConn.disconnect();
        }

        return null;

    }
}