package com.example.scanner;

import android.content.ContentValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class RequestHttpURLConnection {
    public String request(String _url, ContentValues _params) throws IOException {
        //HttpURLConnection 참조 변수
        HttpURLConnection urlConn = null;
        //URL뒤에 붙여 보낼 파라미터
        StringBuffer sbParams = new StringBuffer();

        /**
         * 1. StringBuffer에 파라미터 연결
         */
        //보낼 데이터가 없는 경우 파라미터 비움
        if (_params == null)
            sbParams.append("");
        else {
            //파라미터가 두 개 이상이면 &가 필요하므로 스위칭 변수 생성
            boolean isAnd = false;

            String key;
            String value;

            for(Map.Entry<String, Object> parameter : _params.valueSet()){
                key = parameter.getKey();
                value = parameter.getValue().toString();

                if (isAnd)
                    sbParams.append("&");

                sbParams.append(key).append("=").append(value);

                if(!isAnd)
                    if (_params.size() >= 2)
                        isAnd = true;
            }
        }
        /**
         * 2. HttpURLConnection을 통해 web 데이터를 가져온다
         */
        try{
            URL url = new URL(_url);
            urlConn = (HttpURLConnection) url.openConnection();

            //urlConn 설정
            urlConn.setRequestMethod("POST"); //URL요청에 대한 메소드 설정 : POST
            urlConn.setRequestProperty("Accept-Charset", "UTF-8"); //Accept-Charset 설정
            urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

            //paramater 전달 및 데이터 가져오기
            String strParams = sbParams.toString();
            OutputStream os = urlConn.getOutputStream();
            os.write(strParams.getBytes("UTF-8"));
            os.flush();
            os.close();

            //연결 요청 확인
            if (urlConn.getResponseCode()!=HttpURLConnection.HTTP_OK)
                return null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            String line;
            String page = "";

            while ((line = reader.readLine())!=null){
                page+=line;
            }

            return page;
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(urlConn != null)
                urlConn.disconnect();
        }
        return null;
    }
}
