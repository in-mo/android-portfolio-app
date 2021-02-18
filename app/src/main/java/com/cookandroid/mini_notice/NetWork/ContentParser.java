package com.cookandroid.mini_notice.NetWork;

import com.cookandroid.mini_notice.ContentInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContentParser {
    static public int getContentsInfoJson(String response, ArrayList<ContentInfo> contentInfo) throws JSONException {
        // userList는 빈그릇
        int iNum;
        String strTitle;
        String strContents;
        int iReadcount;
        int iLikeCount;
        int iHateCount;
        String strRegId;
        String strRegDate;

        JSONObject rootJSON = new JSONObject(response);
        JSONArray jsonArray = new JSONArray(rootJSON.getString("datas"));

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = (JSONObject) jsonArray.get(i);

            if (jsonObj.getString("NUM").toString().equals("0"))
                iNum = 999;
            else
                iNum = Integer.parseInt(jsonObj.getString("NUM").toString());

            if (jsonObj.getString("TITLE").toString().equals("null"))
                strTitle = "-";
            else
                strTitle = jsonObj.getString("TITLE").toString();


            if (jsonObj.getString("CONTENTS").toString().equals("null"))
                strContents = "-";
            else
                strContents = jsonObj.getString("CONTENTS").toString();

            if (jsonObj.getString("READCOUNT").toString().equals("null"))
                iReadcount = 999;
            else
                iReadcount = Integer.parseInt(jsonObj.getString("READCOUNT").toString());

            if (jsonObj.getString("LIKECOUNT").toString().equals("null"))
                iLikeCount = 999;
            else
                iLikeCount = Integer.parseInt(jsonObj.getString("LIKECOUNT").toString());

            if (jsonObj.getString("HATECOUNT").toString().equals("null"))
                iHateCount = 999;
            else
                iHateCount = Integer.parseInt(jsonObj.getString("HATECOUNT").toString());

            if (jsonObj.getString("REG_ID").toString().equals("null"))
                strRegId = "-";
            else
                strRegId = jsonObj.getString("REG_ID").toString();

            if (jsonObj.getString("REG_DATE").toString().equals("null"))
                strRegDate = "-";
            else {
                strRegDate = jsonObj.getString("REG_DATE").toString();
                String temp[] = strRegDate.split(" ");
                strRegDate = temp[0] + "\n" + temp[1];
            }

            contentInfo.add(new ContentInfo(iNum, strTitle, strContents,
                    iReadcount, iLikeCount, iHateCount, strRegId, strRegDate));
        }
        return jsonArray.length();
    }

    static public String[] getContentsInfoArr(String response, String[] contentInfo) throws JSONException {
        // userList는 빈그릇
        String strNum;
        String strTitle;
        String strContents;
        String strReadcount;
        String strLikeCount;
        String strHateCount;
        String strRegId;
        String strRegDate;

        JSONObject rootJSON = new JSONObject(response);
        JSONArray jsonArray = new JSONArray(rootJSON.getString("datas"));

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = (JSONObject) jsonArray.get(i);

            if (jsonObj.getString("NUM").toString().equals("0"))
                strNum = "-";
            else
                strNum = jsonObj.getString("NUM").toString();

            if (jsonObj.getString("TITLE").toString().equals("null"))
                strTitle = "-";
            else
                strTitle = jsonObj.getString("TITLE").toString();


            if (jsonObj.getString("CONTENTS").toString().equals("null"))
                strContents = "-";
            else
                strContents = jsonObj.getString("CONTENTS").toString();

            if (jsonObj.getString("READCOUNT").toString().equals("null"))
                strReadcount = "-";
            else
                strReadcount = jsonObj.getString("READCOUNT").toString();

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

            if (jsonObj.getString("REG_DATE").toString().equals("null"))
                strRegDate = "-";
            else {
                strRegDate = jsonObj.getString("REG_DATE").toString();
                String temp[] = strRegDate.split(" ");
                strRegDate = temp[0] + "\n" + temp[1];
            }
            contentInfo = new String[]{strNum, strTitle, strContents,
                    strReadcount, strLikeCount, strHateCount, strRegId, strRegDate};
        }
        return contentInfo;
    }

    static public int getResultJson(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
        return Integer.parseInt(jsonObject.getString("RESULT_OK"));
    }

    static public String getStrResultJson(String response) throws JSONException {
        JSONArray jsonArray = new JSONArray(response);
        JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
        return jsonObject.getString("RESULT_OK");
    }
}
