package com.cookandroid.mini_notice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.cookandroid.mini_notice.NetWork.ContentParser;
import com.cookandroid.mini_notice.NetWork.NetworkSearchId;
import com.cookandroid.mini_notice.NetWork.NetworkSearchPassword;
import com.google.android.material.tabs.TabLayout;


public class UserSearchActivity extends FragmentActivity {

    TabLayout tabs;
    ImageView userSearchByBackBtn;
    TextView userSearchText, resultText;
    Button searchSureBtn;
    UserSearchById userSearchById;
    UserSearchByPass userSearchByPass;

    final int ID_SEARCH = 0;
    final int PASS_SEARCH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_user_search);

        userSearchById = new UserSearchById();
        userSearchByPass = new UserSearchByPass();

        userSearchByBackBtn = (ImageView) findViewById(R.id.userSearchByBackBtn);
        resultText = (TextView) findViewById(R.id.resultText);
        userSearchText = (TextView) findViewById(R.id.userSearchText);
        searchSureBtn = (Button) findViewById(R.id.searchSureBtn);

        Intent intent = getIntent();
        int num = intent.getExtras().getInt("num");

        tabs = (TabLayout) findViewById(R.id.tabs);
        switch (num) {
            case 1:
                tabs.addTab(tabs.newTab().setText("아이디 찾기"), true);
                tabs.addTab(tabs.newTab().setText("비밀번호 찾기"), false);
                userSearchText.setText("아이디 찾기");
                searchSureBtn.setText("아이디 찾기");
                getSupportFragmentManager().beginTransaction().add(R.id.container, userSearchById).commit();
                break;
            case 2:
                tabs.addTab(tabs.newTab().setText("아이디 찾기"), false);
                tabs.addTab(tabs.newTab().setText("비밀번호 찾기"), true);
                userSearchText.setText("비밀번호 찾기");
                searchSureBtn.setText("비밀번호 찾기");
                getSupportFragmentManager().beginTransaction().add(R.id.container, userSearchByPass).commit();
                break;
        }

        searchSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectPage = tabs.getSelectedTabPosition();
                switch (selectPage) {
                    case ID_SEARCH:
                        String[] inIdText = userSearchById.getText();
                        try {
                            String res = new NetworkSearchId().execute(inIdText[0], inIdText[1]).get();
                            String id = ContentParser.getStrResultJson(res);
                            if (id != null && !id.equals("")) {
                                resultText.setText("찾으시는 아이디 : " + id);
                            } else if (id.equals("")) {
                                resultText.setText("잘못된 정보를 기입하셨거나 가입되지 않은 정보입니다.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case PASS_SEARCH:
                        String[] inPassText = userSearchByPass.getText();
                        try {
                            String res = new NetworkSearchPassword().execute(inPassText[0], inPassText[1], inPassText[2]).get();
                            String password = ContentParser.getStrResultJson(res);
                            if (password != null && !password.equals("")) {
                                System.out.println("password : " + password);
                                resultText.setText("찾으시는 비밀번호 : " + password);
                            } else if (password.equals("")) {
                                resultText.setText("잘못된 정보를 기입하셨거나 가입되지 않은 정보입니다.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected = null;
                if (position == 0) {
                    selected = userSearchById;
                    userSearchText.setText("아이디 찾기");
                    searchSureBtn.setText("아이디 찾기");
                    resultText.setText("");
                } else if (position == 1) {
                    selected = userSearchByPass;
                    userSearchText.setText("비밀번호 찾기");
                    searchSureBtn.setText("비밀번호 찾기");
                    resultText.setText("");
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        userSearchByBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
