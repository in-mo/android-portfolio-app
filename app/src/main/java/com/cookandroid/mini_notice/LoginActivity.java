package com.cookandroid.mini_notice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.mini_notice.NetWork.ContentParser;
import com.cookandroid.mini_notice.NetWork.NetworkLogin;

public class LoginActivity extends Activity {
    ImageView backBtn;
    EditText inputIdEdit, inputPwdEdit;
    TextView joinPageBtn, idSByPageBtn, pwdByPageBtn;
    Button loginSureBtn;

    final int failAll = -1;
    final int failByPw = 0;
    final int login = 1;
    final int logout = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        backBtn = (ImageView) findViewById(R.id.joinByBackBtn);

        inputIdEdit = (EditText) findViewById(R.id.inputIdEdit);
        inputPwdEdit = (EditText) findViewById(R.id.inputPwdEdit);

        joinPageBtn = (TextView) findViewById(R.id.joinPageBtn);
        idSByPageBtn = (TextView) findViewById(R.id.idSByPageBtn);
        pwdByPageBtn = (TextView) findViewById(R.id.pwdByPageBtn);

        loginSureBtn = (Button) findViewById(R.id.loginSureBtn);

        loginSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = inputIdEdit.getText().toString().trim();
                String password = inputPwdEdit.getText().toString().trim();

                try {
                    String res = new NetworkLogin().execute(id, password).get();
                    int count = ContentParser.getResultJson(res);
                    if (id.length() != 0 && password.length() != 0) {
                        switch (count) {
                            case login:
                                MainActivity.id = id;

                                Intent intent = getIntent();
                                setResult(6, intent);

                                finish();
                                break;
                            case failByPw:
                                Toast.makeText(LoginActivity.this, "비밀번호가 틀렸습니다!", Toast.LENGTH_SHORT).show();
                                break;
                            case failAll:
                                Toast.makeText(LoginActivity.this, "존재하지 않는 아이디입니다!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } else
                        Toast.makeText(LoginActivity.this, "제대로 입력해주세요!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        joinPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

        idSByPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, UserSearchActivity.class);
                intent.putExtra("num", 1);
                startActivity(intent);
            }
        });

        pwdByPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, UserSearchActivity.class);
                intent.putExtra("num", 2);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
