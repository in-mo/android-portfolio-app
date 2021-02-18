package com.cookandroid.mini_notice.NetWork;

import com.cookandroid.mini_notice.ReplyInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserInfoParser {
    static public String[] getUserInfoJson(String response, String[] replyInfo) throws JSONException {
        // userList는 빈그릇
        String strId;
        String strPassword;
        String strName;
        String strEmail;
        String strRegDate;

        JSONObject rootJSON = new JSONObject(response);
        JSONArray jsonArray = new JSONArray(rootJSON.getString("datas"));

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = (JSONObject) jsonArray.get(i);

            if (jsonObj.getString("ID").toString().equals("null"))
                strId = "-";
            else
                strId = jsonObj.getString("ID").toString();

            if (jsonObj.getString("PASSWORD").toString().equals("null"))
                strPassword = "-";
            else
                strPassword = jsonObj.getString("PASSWORD").toString();

            if (jsonObj.getString("NAME").toString().equals("null"))
                strName = "-";
            else
                strName = jsonObj.getString("NAME").toString();

            if (jsonObj.getString("EMAIL").toString().equals("null"))
                strEmail = "-";
            else
                strEmail = jsonObj.getString("EMAIL").toString();


            if (jsonObj.getString("REG_DATE").toString().equals("null"))
                strRegDate = "-";
            else {
                strRegDate = jsonObj.getString("REG_DATE").toString();
//                String temp[] = strRegDate.split(" ");
//                strRegDate = temp[0] + "\n" + temp[1];
            }

            replyInfo = new String[]{strId, strPassword, strName,
                    strEmail, strRegDate
            };
        }
        return replyInfo;
    }


    static public int getResultJson(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
        return Integer.parseInt(jsonObject.getString("RESULT_OK"));
    }
}
