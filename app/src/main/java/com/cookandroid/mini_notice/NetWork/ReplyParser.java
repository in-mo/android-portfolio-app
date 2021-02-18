package com.cookandroid.mini_notice.NetWork;

import com.cookandroid.mini_notice.ContentInfo;
import com.cookandroid.mini_notice.ReplyInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReplyParser {
    static public int getReplysInfoJson(String response, ArrayList<ReplyInfo> replyList) throws JSONException {
        // userList는 빈그릇
        String strNum;
        String strContents;
        String strLikeCount;
        String strHateCount;
        String strRegId;
        String strRegGNum;
        String strRegDate;

        JSONObject rootJSON = new JSONObject(response);
        JSONArray jsonArray = new JSONArray(rootJSON.getString("datas"));

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = (JSONObject) jsonArray.get(i);

            if (jsonObj.getString("NUM").toString().equals("null"))
                strNum = "-";
            else
                strNum = jsonObj.getString("NUM").toString();

            if (jsonObj.getString("CONTENTS").toString().equals("null"))
                strContents = "-";
            else
                strContents = jsonObj.getString("CONTENTS").toString();

            if (jsonObj.getString("LIKECOUNT").toString().equals("null"))
                strLikeCount = "-";
            else
                strLikeCount = jsonObj.getString("LIKECOUNT").toString();

            if (jsonObj.getString("HATECOUNT").toString().equals("null"))
                strHateCount = "-";
            else
                strHateCount = jsonObj.getString("HATECOUNT").toString();

            if (jsonObj.getString("REG_ID").toString().equals("null"))
                strRegId = "-";
            else
                strRegId = jsonObj.getString("REG_ID").toString();


            if (jsonObj.getString("REG_GNUM").toString().equals("null"))
                strRegGNum = "-";
            else
                strRegGNum = jsonObj.getString("REG_GNUM").toString();

            if (jsonObj.getString("REG_DATE").toString().equals("null"))
                strRegDate = "-";
            else {
                strRegDate = jsonObj.getString("REG_DATE").toString();
                String temp[] = strRegDate.split(" ");
                strRegDate = temp[0] + "\n" + temp[1];
            }

            replyList.add(new ReplyInfo(strNum, strContents,
                    strLikeCount, strHateCount, strRegId, strRegGNum, strRegDate));
        }
        return jsonArray.length();
    }

    static public String[] getReplysInfoJson(String response, String[] replyInfo) throws JSONException {
        // userList는 빈그릇
        String strNum;
        String strContents;
        String strLikeCount;
        String strHateCount;
        String strRegId;
        String strRegGNum;
        String strRegDate;

        JSONObject rootJSON = new JSONObject(response);
        JSONArray jsonArray = new JSONArray(rootJSON.getString("datas"));

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = (JSONObject) jsonArray.get(i);

            if (jsonObj.getString("NUM").toString().equals("null"))
                strNum = "-";
            else
                strNum = jsonObj.getString("NUM").toString();

            if (jsonObj.getString("CONTENTS").toString().equals("null"))
                strContents = "-";
            else
                strContents = jsonObj.getString("CONTENTS").toString();

            if (jsonObj.getString("LIKECOUNT").toString().equals("null"))
                strLikeCount = "-";
            else
                strLikeCount = jsonObj.getString("LIKECOUNT").toString();

            if (jsonObj.getString("HATECOUNT").toString().equals("null"))
                strHateCount = "-";
            else
                strHateCount = jsonObj.getString("HATECOUNT").toString();

            if (jsonObj.getString("REG_ID").toString().equals("null"))
                strRegId = "-";
            else
                strRegId = jsonObj.getString("REG_ID").toString();


            if (jsonObj.getString("REG_GNUM").toString().equals("null"))
                strRegGNum = "-";
            else
                strRegGNum = jsonObj.getString("REG_GNUM").toString();

            if (jsonObj.getString("REG_DATE").toString().equals("null"))
                strRegDate = "-";
            else {
                strRegDate = jsonObj.getString("REG_DATE").toString();
                String temp[] = strRegDate.split(" ");
                strRegDate = temp[0] + "\n" + temp[1];
            }

            replyInfo = new String[]{strNum, strContents, strLikeCount,
                    strHateCount, strRegId, strRegGNum, strRegDate
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
