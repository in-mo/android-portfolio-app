package com.cookandroid.mini_notice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.mini_notice.NetWork.ContentParser;
import com.cookandroid.mini_notice.NetWork.NetworkContentsListGet;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ImageView writeBtn, loginBtn, logoutBtn, joinBtn, idBackImg;
    private TextView idView, none_list;
    private EditText searchEdt;
    private Spinner searchCombo;
    private Button searchBtn;
    private ListView content_list;
    private Boards_Adapter boardAdapter;

    final int CALL_LOGIN = 5;
    final int CB_LOGIN = 6;
    final int CALL_USERINFO = 7;
    final int CB_USERINFO = 8;

    final int SEARCH_TITLE = 0;
    final int SEARCH_NUM = 1;
    final int SEARCH_WRITER = 2;

    public static String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        writeBtn = (ImageView) findViewById(R.id.writeBtn);
        loginBtn = (ImageView) findViewById(R.id.mainLoginBtn);
        logoutBtn = (ImageView) findViewById(R.id.mainLogoutBtn);
        joinBtn = (ImageView) findViewById(R.id.joinBtn);
        idView = (TextView) findViewById(R.id.id);
        none_list = (TextView) findViewById(R.id.none_list);
        idBackImg = (ImageView) findViewById(R.id.idBackImg);

        content_list = (ListView) findViewById(R.id.content_listView);

        searchEdt = (EditText) findViewById(R.id.searchEdt);

        searchCombo = (Spinner) findViewById(R.id.searchCombo);
        ArrayAdapter searchAdapter = ArrayAdapter.createFromResource(this,
                R.array.search_list, R.layout.spinner_layout);
        searchAdapter.setDropDownViewResource(R.layout.spinner_list);
        searchCombo.setAdapter(searchAdapter);

        searchBtn = (Button) findViewById(R.id.searchBtn);

        boardAdapter = new Boards_Adapter(MainActivity.this,
                R.layout.content_list, new ArrayList<ContentInfo>());
        content_list.setAdapter(boardAdapter);

        writeBtn.setVisibility(View.INVISIBLE);
        idView.setVisibility(View.INVISIBLE);
        idBackImg.setVisibility(View.INVISIBLE);
        logoutBtn.setVisibility(View.INVISIBLE);
        none_list.setVisibility(View.INVISIBLE);

        idView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivityForResult(intent, CALL_USERINFO);
            }
        });

        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContentWriteActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, CALL_LOGIN);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, id + "님께서 로그아웃하셨습니다.", Toast.LENGTH_SHORT).show();
                id = "";
                idView.setText(id);

                writeBtn.setVisibility(View.INVISIBLE);
                idView.setVisibility(View.INVISIBLE);
                idBackImg.setVisibility(View.INVISIBLE);
                logoutBtn.setVisibility(View.INVISIBLE);
                loginBtn.setVisibility(View.VISIBLE);
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = searchEdt.getText().toString() == null ? "" : searchEdt.getText().toString();

                int selectNum = searchCombo.getSelectedItemPosition();
                String strNum = String.valueOf(selectNum);

                try {
                    ArrayList<ContentInfo> contentsList = new ArrayList<ContentInfo>();
                    String res = new NetworkContentsListGet((Boards_Adapter) content_list.getAdapter()).execute(strNum, txt).get();
                    int count = ContentParser.getContentsInfoJson(res, contentsList);
                    if (count == 0) {
                        none_list.setVisibility(View.VISIBLE);
                    } else {
                        none_list.setVisibility(View.INVISIBLE);
                    }
                    boardAdapter.setDatas(contentsList);
                    boardAdapter.notifyDataSetInvalidated();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            ArrayList<ContentInfo> contentsList = new ArrayList<ContentInfo>();
            String res = new NetworkContentsListGet((Boards_Adapter) content_list.getAdapter()).execute("", "").get();
            int count = ContentParser.getContentsInfoJson(res, contentsList);
            if (count == 0) {
                none_list.setVisibility(View.VISIBLE);
            } else {
                none_list.setVisibility(View.INVISIBLE);
            }
            boardAdapter.setDatas(contentsList);
            boardAdapter.notifyDataSetInvalidated();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        try {
            ArrayList<ContentInfo> contentsList = new ArrayList<ContentInfo>();
            String res = new NetworkContentsListGet((Boards_Adapter) content_list.getAdapter()).execute("", "").get();
            int count = ContentParser.getContentsInfoJson(res, contentsList);
            if (count == 0) {
                none_list.setVisibility(View.VISIBLE);
            } else {
                none_list.setVisibility(View.INVISIBLE);
            }
            boardAdapter.setDatas(contentsList);
            boardAdapter.notifyDataSetInvalidated();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case CB_USERINFO:
                writeBtn.setVisibility(View.INVISIBLE);
                idView.setVisibility(View.INVISIBLE);
                idBackImg.setVisibility(View.INVISIBLE);
                logoutBtn.setVisibility(View.INVISIBLE);
                loginBtn.setVisibility(View.VISIBLE);
                idView.setText(id);
                break;
            case CB_LOGIN:
                writeBtn.setVisibility(View.VISIBLE);
                idView.setVisibility(View.VISIBLE);
                idBackImg.setVisibility(View.VISIBLE);
                logoutBtn.setVisibility(View.VISIBLE);
                loginBtn.setVisibility(View.INVISIBLE);

                idView.setText(id + "님");
                Toast.makeText(MainActivity.this, id + "님께서 로그인하였습니다.", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
