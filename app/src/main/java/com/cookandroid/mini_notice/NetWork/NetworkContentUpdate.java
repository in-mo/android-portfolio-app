package com.cookandroid.mini_notice.NetWork;

import android.os.AsyncTask;
import android.util.Log;

import com.cookandroid.mini_notice.Content_Adapter;
import com.cookandroid.mini_notice.UserInfo;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NetworkContentUpdate extends AsyncTask<String, Void, String> {
    private URL Url;
    private String URL_Address = "http://10.100.103.51/AppDB/p_content_update.jsp";

    String num = "";
    Content_Adapter contentAdapter;

    public NetworkContentUpdate(Content_Adapter contentAdapter) {
        this.contentAdapter = contentAdapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String res = "";
        try {
            Url = new URL(URL_Address);
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // 해당 URL로 인터넷 연결

            conn.setDefaultUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=utf-8");

            // 전송값 설정
            StringBuffer buffer = new StringBuffer();
            buffer.append("num").append("=").append(strings[0]);
            buffer.append("&title").append("=").append(strings[1]);
            buffer.append("&contents").append("=").append(strings[2]);
            num = strings[0];

            // 서버로 전송
            OutputStreamWriter outStream = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();

            // 서버 값 받는 부분
            StringBuilder builder = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;

            while ((line = in.readLine()) != null) {
                builder.append(line + "\n");
            }
            res = builder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("Get Con Update result", res);

        return res; // onPostExecute로 전달함.
    }

    @Override
    protected void onPostExecute(String s) { // 위 res를 여기로 리턴함
        super.onPostExecute(s);

        ArrayList<UserInfo> userList = new ArrayList<UserInfo>();
        int res = 0;

        try {
            res = ContentParser.getResultJson(s); // s는 꽉찬값, userList는 빈값이었지만
//                                                     getUserInfoJson을 통해 userList에 값을 채움.
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (res == 0) {

        } else {
            new NetworkContentGet(contentAdapter).execute(num);
        }
    }
}
