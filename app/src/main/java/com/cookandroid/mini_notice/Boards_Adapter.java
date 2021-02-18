package com.cookandroid.mini_notice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.cookandroid.mini_notice.NetWork.ContentParser;
import com.cookandroid.mini_notice.NetWork.NetworkContentsListGet;
import com.cookandroid.mini_notice.NetWork.NetworkFavorSetInfo;
import com.cookandroid.mini_notice.NetWork.NetworkReadCountSet;

import org.json.JSONException;

import java.util.ArrayList;

public class Boards_Adapter extends BaseAdapter {
    private Activity mAct;
    LayoutInflater mInflater;
    ArrayList<ContentInfo> mContentsInfoObjArr;
    int mListLayout;

    public Boards_Adapter(Activity a, int listLayout, ArrayList<ContentInfo> contentsInfoObjArrayListT) {
        mAct = a;
        mListLayout = listLayout;
        mContentsInfoObjArr = contentsInfoObjArrayListT;
        mInflater = (LayoutInflater) a.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDatas(ArrayList<ContentInfo> arrayList) {
        mContentsInfoObjArr = arrayList;
    }

    @Override
    public int getCount() {
        return mContentsInfoObjArr.size();
    }

    @Override
    public Object getItem(int i) {
        return mContentsInfoObjArr.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = mInflater.inflate(mListLayout, viewGroup, false);

        final TableRow table = (TableRow) view.findViewById(R.id.board);
        final int index = i;
        table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = String.valueOf(mContentsInfoObjArr.get(index).num);
                Intent intent = new Intent(mAct.getApplicationContext(), ContentActivity.class);
                intent.putExtra("num", num);
                try {
                    String resReadCount = new NetworkReadCountSet().execute(num).get();
                    int countReadCount = ContentParser.getResultJson(resReadCount);
                    if (countReadCount == 1) {
                        mAct.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        final TextView content_num = (TextView) view.findViewById(R.id.content_num);
        content_num.setText(String.valueOf(mContentsInfoObjArr.get(i).num));

        final TextView content_title = (TextView) view.findViewById(R.id.content_title);
        content_title.setText(mContentsInfoObjArr.get(i).title);

        final TextView content_id = (TextView) view.findViewById(R.id.content_id);
        content_id.setText(mContentsInfoObjArr.get(i).reg_id);

        final TextView content_date = (TextView) view.findViewById(R.id.content_date);
        content_date.setText(mContentsInfoObjArr.get(i).reg_date);

        final TextView content_count = (TextView) view.findViewById(R.id.content_count);
        content_count.setText(String.valueOf(mContentsInfoObjArr.get(i).readCount));

        return view;
    }
}
