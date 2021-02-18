package com.cookandroid.mini_notice.NetWork;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.cookandroid.mini_notice.ContentInfo;
import com.cookandroid.mini_notice.Content_Adapter;
import com.cookandroid.mini_notice.FavorInfo;
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

public class NetworkFavorSetInfo extends AsyncTask<String, Void, String> {
    private URL Url;
    private String URL_Address = "http://10.100.103.51/AppDB/p_favor_";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String res = "";
        try {
            // 전송값 설정
            StringBuffer buffer = new StringBuffer();
            buffer.append("board_num").append("=").append(strings[0]);
            buffer.append("&reply_num").append("=").append(strings[1]);
            buffer.append("&isReply").append("=").append(strings[2]);
            buffer.append("&isLike").append("=").append(strings[3]);
            buffer.append("&reg_id").append("=").append(strings[4]);
            URL_Address += strings[5] + ".jsp";

            Url = new URL(URL_Address);
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection(); // 해당 URL로 인터넷 연결

            conn.setDefaultUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=utf-8");

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

        Log.i("Get favor get result", res);

        return res; // onPostExecute로 전달함.
    }

    @Override
    protected void onPostExecute(String s) { // 위 res를 여기로 리턴함
        super.onPostExecute(s);

//        ArrayList<FavorInfo> favorInfo = new ArrayList<FavorInfo>();
//        if (URL_Address.equals("http://10.100.103.51/AppDB/p_favor_get")) {
//            int count = 0;
//            try {
//                count = FavorParser.getContentsInfoJson(s, favorInfo);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            if (count == 0) {
//
//            } else {
//
//            }
//        }
    }
}