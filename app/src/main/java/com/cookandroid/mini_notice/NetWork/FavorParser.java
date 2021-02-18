package com.cookandroid.mini_notice.NetWork;

import com.cookandroid.mini_notice.ContentInfo;
import com.cookandroid.mini_notice.FavorInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavorParser {
    static public ArrayList getFavorsInfoJson(String response, ArrayList<FavorInfo> favorInfo) throws JSONException {
        String strBoard_num;
        String strReply_num;
        String strIsReply;
        String strIsLike;
        String strRegId;

        JSONObject rootJSON = new JSONObject(response);
        JSONArray jsonArray = new JSONArray(rootJSON.getString("datas"));

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = (JSONObject) jsonArray.get(i);

            if (jsonObj.getString("BOARD_NUM").toString().equals("0"))
                strBoard_num = "-";
            else
                strBoard_num = jsonObj.getString("BOARD_NUM").toString();

            if (jsonObj.getString("REPLY_NUM").toString().equals("null"))
                strReply_num = "-";
            else
                strReply_num = jsonObj.getString("REPLY_NUM").toString();


            if (jsonObj.getString("ISREPLY").toString().equals("null"))
                strIsReply = "-";
            else
                strIsReply = jsonObj.getString("ISREPLY").toString();

            if (jsonObj.getString("ISLIKE").toString().equals("null"))
                strIsLike = "-";
            else
                strIsLike = jsonObj.getString("ISLIKE").toString();

            if (jsonObj.getString("REG_ID").toString().equals("null"))
                strRegId = "-";
            else
                strRegId = jsonObj.getString("REG_ID").toString();

            favorInfo.add(new FavorInfo(strBoard_num, strReply_num, strIsReply, strIsLike, strRegId));
        }
        return favorInfo;
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
