package com.cookandroid.mini_notice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cookandroid.mini_notice.NetWork.NetworkContentGet;
import com.cookandroid.mini_notice.NetWork.NetworkReplyGet;
import com.cookandroid.mini_notice.NetWork.ReplyParser;

import java.util.ArrayList;

public class ContentActivity extends Activity {
    ListView reply_listView;
    ListView content_listView;
    ImageView contentByBackBtn;

    Reply_Adapter replyAdapter;
    Content_Adapter contentAdapter;

    TextView replyCountView;

    Intent intent;
    static String num = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);

        replyCountView = (TextView) findViewById(R.id.replyCountView);
        contentByBackBtn = (ImageView) findViewById(R.id.contentByBackBtn);

        intent = getIntent();
        num = intent.getExtras().getString("num");

        reply_listView = (ListView) findViewById(R.id.reListView);
        replyAdapter = new Reply_Adapter(ContentActivity.this,
                R.layout.content_reply, new ArrayList<ReplyInfo>());
        reply_listView.setAdapter(replyAdapter);


        content_listView = (ListView) findViewById(R.id.conListView);
        contentAdapter = new Content_Adapter(ContentActivity.this,
                R.layout.selected_content, new ArrayList<ContentInfo>(), replyAdapter);
        content_listView.setAdapter(contentAdapter);

        new NetworkReplyGet((Reply_Adapter) reply_listView.getAdapter()).execute(num);

        new NetworkContentGet((Content_Adapter) content_listView.getAdapter(),
                (Reply_Adapter) reply_listView.getAdapter()).execute(num);

//        new NetworkFavorSetInfo()

        contentByBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            ArrayList<ReplyInfo> replyInfos = new ArrayList<ReplyInfo>();
            String resGet = new NetworkReplyGet(replyAdapter).execute(num).get();
            int countGet = ReplyParser.getReplysInfoJson(resGet, replyInfos);
            replyCountView.setText("댓글 " + countGet + "개");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
