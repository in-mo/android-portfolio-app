package com.cookandroid.mini_notice.NetWork;

import android.os.AsyncTask;
import android.util.Log;

import com.cookandroid.mini_notice.ReplyInfo;
import com.cookandroid.mini_notice.Reply_Adapter;

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

public class NetworkReplyGet extends AsyncTask<String, Void, String> {

    private URL Url;
    private String URL_Address = "http://10.100.103.51/AppDB/replys_list.jsp";
    private Reply_Adapter replyAdapter;

    public NetworkReplyGet(Reply_Adapter replyAdapter) {
        this.replyAdapter = replyAdapter;
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
            buffer.append("reg_gnum").append("=").append(strings[0]);

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

        Log.i("Get Reply result", res);

        return res; // onPostExecute로 전달함.
    }

    @Override
    protected void onPostExecute(String s) { // 위 res를 여기로 리턴함
        super.onPostExecute(s);

        ArrayList<ReplyInfo> replysList = new ArrayList<ReplyInfo>();
        int count = 0;
        try {
            count = ReplyParser.getReplysInfoJson(s, replysList); // s는 꽉찬값, contentsList는 빈값이었지만
//                                                              getUserInfoJson을 통해 contentsList에 값을 채움.
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (count == 0) {
//            adapter.setDatas(replysList);
//            adapter.notifyDataSetInvalidated(); // 화면 갱신용
        } else {
            replyAdapter.setDatas(replysList); // JsonParser에서 데이터를 채워온것을 넣음.
            replyAdapter.notifyDataSetInvalidated(); // 화면 갱신용
        }
    }
}