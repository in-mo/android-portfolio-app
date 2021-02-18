package com.cookandroid.mini_notice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cookandroid.mini_notice.NetWork.ContentParser;
import com.cookandroid.mini_notice.NetWork.NetworkJoin;
import com.cookandroid.mini_notice.NetWork.NetworkUserIdCheck;

import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class JoinActivity extends Activity {
    TextView checkPwd;
    ImageView joinByBackBtn;
    EditText addId, addPwd, addPwdChk, addName, addEmail;
    CheckBox allAgreeRBtn, privacyRBtn, margetRBtn, limitRBtn;
    Button overlapChkBtn, joinSureBtn;

    // 아이디 영어와 숫자만 입력하는 필터 정의
    public InputFilter filterAlphaNumber = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

    // 아이디 최대 글자수 11자리로 정의
    InputFilter[] filters = new InputFilter[]{
            new InputFilter.LengthFilter(11),
            filterAlphaNumber
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        checkPwd = (TextView) findViewById(R.id.checkPwd);
        joinByBackBtn = (ImageView) findViewById(R.id.joinByBackBtn);

        addId = (EditText) findViewById(R.id.addID);
        addId.setFilters(filters);

        addPwd = (EditText) findViewById(R.id.addPwd);
        addPwdChk = (EditText) findViewById(R.id.addPwdChk);
        addName = (EditText) findViewById(R.id.addName);
        addEmail = (EditText) findViewById(R.id.addEmail);

        allAgreeRBtn = (CheckBox) findViewById(R.id.allAgreeRBtn);
        privacyRBtn = (CheckBox) findViewById(R.id.privacyRBtn);
        margetRBtn = (CheckBox) findViewById(R.id.margetRBtn);
        limitRBtn = (CheckBox) findViewById(R.id.limitRBtn);

        overlapChkBtn = (Button) findViewById(R.id.overlapChkBtn);
        joinSureBtn = (Button) findViewById(R.id.joinSureBtn);
        joinSureBtn.setEnabled(false);

        Spinner emailSpinner = (Spinner) findViewById(R.id.email_list);
        ArrayAdapter emailAdapter = ArrayAdapter.createFromResource(this,
                R.array.email_list, android.R.layout.simple_spinner_item);
        emailAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        emailSpinner.setAdapter(emailAdapter);

        joinByBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        overlapChkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strId = addId.getText().toString().trim();
                if (strId.length() > 4 && strId.length() < 12)
                    try {
                        String res = new NetworkUserIdCheck().execute(strId).get();
                        int count = ContentParser.getResultJson(res);
                        if (count == 0) {
                            Toast.makeText(JoinActivity.this, "등록 가능한 ID 입니다.", Toast.LENGTH_SHORT).show();
                            joinSureBtn.setEnabled(true);
                        }
                        else {
                            Toast.makeText(JoinActivity.this, "존재하는 ID 입니다.", Toast.LENGTH_SHORT).show();
                            joinSureBtn.setEnabled(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                else {
                    joinSureBtn.setEnabled(false);
                    Toast.makeText(JoinActivity.this,
                            "아이디의 길이는 5~11자리로 해야합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        allAgreeRBtn.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (allAgreeRBtn.isChecked()) {
                    privacyRBtn.setChecked(true);
                    margetRBtn.setChecked(true);
                    limitRBtn.setChecked(true);

                } else {
                    privacyRBtn.setChecked(false);
                    margetRBtn.setChecked(false);
                    limitRBtn.setChecked(false);
                }
            }
        });

        addPwdChk.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String pwd = addPwd.getText().toString();
                    String pwdChk = addPwdChk.getText().toString();
                    if (!pwdChk.equals("")) {
                        if (pwd.equals(pwdChk)) {
                            checkPwd.setTextColor(Color.BLUE);
                            checkPwd.setText("옳음");
                        } else {
                            checkPwd.setTextColor(Color.RED);
                            checkPwd.setText("틀림!");
                        }
                    } else {
                        checkPwd.setTextColor(Color.RED);
                        checkPwd.setText("정확히 입력해주세요!");
                    }
                }
            }
        });

        joinSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idLength = addId.getText().toString().trim().length();
                int pwdLength = addPwd.getText().toString().trim().length();
                int nameLength = addName.getText().toString().trim().length();
                int emailLength = addEmail.getText().toString().trim().length();

                boolean idLengthChk = idLength > 4 ? true : false;
                boolean pwdLengthChk = pwdLength > 7 ? true : false;
                boolean pwdChk = addPwd.getText().toString().trim().equals(addPwdChk.getText().toString().trim());
                boolean nameChk = nameLength > 0 ? true : false;
                boolean emailChk = emailLength > 0 ? true : false;
                boolean privacyRBtnisChecked = privacyRBtn.isChecked();
                boolean limitRBtnisChecked = limitRBtn.isChecked();
                boolean emailSpinnerChk = emailSpinner.getSelectedItemPosition() != 0 ? true : false;

                if (idLengthChk && pwdLengthChk && pwdChk && nameChk
                        && emailChk && privacyRBtnisChecked && limitRBtnisChecked && emailSpinnerChk) {

                    String id = addId.getText().toString().trim();
                    String password = addPwd.getText().toString().trim();
                    String name = addName.getText().toString().trim();
                    String email = addEmail.getText().toString().trim() + "@" + emailSpinner.getSelectedItem().toString();

                    Toast.makeText(JoinActivity.this, id+"님 가입을 축하드립니다.", Toast.LENGTH_SHORT).show();
                    new NetworkJoin().execute(id, password, name, email);

                    finish();
                } else {
                    Toast.makeText(JoinActivity.this, "각 항목의 조건에 맞게 기입해주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
