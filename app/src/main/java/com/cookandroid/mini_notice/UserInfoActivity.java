package com.cookandroid.mini_notice;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.cookandroid.mini_notice.NetWork.NetworkFavorSetInfo;
import com.cookandroid.mini_notice.NetWork.NetworkUserInfo;
import com.cookandroid.mini_notice.NetWork.ReplyParser;
import com.cookandroid.mini_notice.NetWork.UserInfoParser;

public class UserInfoActivity extends Activity {

    EditText modifyId, modifyPwd, modifyPwdChk, modifyName, modifyEmail, modifyRegWrite;
    Button userModifyBtn, userDeleteBtn, userBackBtn;
    LinearLayout passLayout;
    Intent intent;
    LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        intent = getIntent();

        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        modifyId = (EditText) findViewById(R.id.modifyId);
        modifyPwd = (EditText) findViewById(R.id.modifyPwd);
        modifyPwdChk = (EditText) findViewById(R.id.modifyPwdChk);
        modifyName = (EditText) findViewById(R.id.modifyName);
        modifyEmail = (EditText) findViewById(R.id.modifyEmail);
        modifyRegWrite = (EditText) findViewById(R.id.modifyRegWrite);

        userModifyBtn = (Button) findViewById(R.id.userModifyBtn);
        userDeleteBtn = (Button) findViewById(R.id.userDeleteBtn);
        userBackBtn = (Button) findViewById(R.id.userBackBtn);

        passLayout = (LinearLayout) findViewById(R.id.passLayout);

        passLayout.setVisibility(View.GONE);

        modifyId.setEnabled(false);
        modifyName.setEnabled(false);
        modifyEmail.setEnabled(false);
        modifyRegWrite.setEnabled(false);

        try {
            String userInfoGet = new NetworkUserInfo().execute(MainActivity.id, "get", "", "", "").get();
            String[] userInfoStr = UserInfoParser.getUserInfoJson(userInfoGet, new String[5]);
            modifyId.setText(userInfoStr[0]);
            modifyPwd.setText(userInfoStr[1]);
            modifyName.setText(userInfoStr[2]);
            modifyEmail.setText(userInfoStr[3]);
            modifyRegWrite.setText(userInfoStr[4]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        userModifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = mInflater.inflate(R.layout.user_info, null);
                try {
                    String userInfoGet = new NetworkUserInfo().execute(MainActivity.id, "get", "", "", "").get();
                    String[] userInfoStr = UserInfoParser.getUserInfoJson(userInfoGet, new String[5]);
                    EditText modifyIdNew = (EditText) view.findViewById(R.id.modifyId);
                    EditText modifyNameNew = (EditText) view.findViewById(R.id.modifyName);
                    EditText modifyEmailNew = (EditText) view.findViewById(R.id.modifyEmail);
                    EditText modifyRegWriteNew = (EditText) view.findViewById(R.id.modifyRegWrite);

                    ((TextView) view.findViewById(R.id.userInfoTitle)).setVisibility(View.GONE);
                    modifyIdNew.setText(userInfoStr[0]);
                    modifyNameNew.setText(userInfoStr[2]);
                    modifyEmailNew.setText(userInfoStr[3]);
                    modifyRegWriteNew.setText(userInfoStr[4]);

                    modifyIdNew.setEnabled(false);
                    modifyRegWriteNew.setEnabled(false);

                    ((Button) view.findViewById(R.id.userModifyBtn)).setVisibility(View.GONE);
                    ((Button) view.findViewById(R.id.userDeleteBtn)).setVisibility(View.GONE);
                    ((Button) view.findViewById(R.id.userBackBtn)).setVisibility(View.GONE);

                    AlertDialog.Builder diglogModify = new AlertDialog.Builder(UserInfoActivity.this)
                            .setTitle("회원 정보 수정")
                            .setView(view)
                            .setPositiveButton(android.R.string.ok, null)
                            .setNegativeButton(android.R.string.cancel, null)
                            .setCancelable(false);
                    final AlertDialog dialogView = diglogModify.create();
                    dialogView.show();
                    Button okBtn = dialogView.getButton(AlertDialog.BUTTON_POSITIVE);
                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String password = ((EditText) view.findViewById(R.id.modifyPwd)).getText().toString();
                            String passwordChk = ((EditText) view.findViewById(R.id.modifyPwdChk)).getText().toString();
                            String name = ((EditText) view.findViewById(R.id.modifyName)).getText().toString();
                            String email = ((EditText) view.findViewById(R.id.modifyEmail)).getText().toString();

                            if (password.equals(passwordChk) && password.length() > 7)
                                try {
                                    String res = new NetworkUserInfo().execute(MainActivity.id, "update", password, name, email).get();
                                    int count = ReplyParser.getResultJson(res);
                                    if (count == 1) {
                                        Toast.makeText(UserInfoActivity.this, "수정하였습니다", Toast.LENGTH_SHORT).show();


                                        String userInfoGet = new NetworkUserInfo().execute(MainActivity.id, "get", "", "", "").get();
                                        String[] userInfoStr = UserInfoParser.getUserInfoJson(userInfoGet, new String[5]);
                                        modifyId.setText(userInfoStr[0]);
                                        modifyPwd.setText(userInfoStr[1]);
                                        modifyName.setText(userInfoStr[2]);
                                        modifyEmail.setText(userInfoStr[3]);
                                        modifyRegWrite.setText(userInfoStr[4]);

                                        dialogView.dismiss();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            else
                                Toast.makeText(UserInfoActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Button cancelBtn = dialogView.getButton(AlertDialog.BUTTON_NEGATIVE);
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogView.dismiss();
                            Toast.makeText(UserInfoActivity.this, "취소하였습니다", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        userDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = mInflater.inflate(R.layout.user_info, null);
                ((TextView) view.findViewById(R.id.userInfoTitle)).setVisibility(View.GONE);
                ((TextView) view.findViewById(R.id.idTitle)).setVisibility(View.GONE);
                ((TextView) view.findViewById(R.id.nameTitle)).setVisibility(View.GONE);
                ((TextView) view.findViewById(R.id.emailTitle)).setVisibility(View.GONE);
                ((TextView) view.findViewById(R.id.writeDateTitle)).setVisibility(View.GONE);

                ((EditText) view.findViewById(R.id.modifyId)).setVisibility(View.GONE);
                ((EditText) view.findViewById(R.id.modifyName)).setVisibility(View.GONE);
                ((EditText) view.findViewById(R.id.modifyEmail)).setVisibility(View.GONE);
                ((EditText) view.findViewById(R.id.modifyRegWrite)).setVisibility(View.GONE);

                ((Button) view.findViewById(R.id.userModifyBtn)).setVisibility(View.GONE);
                ((Button) view.findViewById(R.id.userDeleteBtn)).setVisibility(View.GONE);
                ((Button) view.findViewById(R.id.userBackBtn)).setVisibility(View.GONE);

                AlertDialog.Builder diglogDelete = new AlertDialog.Builder(UserInfoActivity.this)
                        .setTitle("회원 탈퇴 - 비밀번호 확인")
                        .setView(view)
                        .setPositiveButton(android.R.string.ok, null)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setCancelable(false);
                final AlertDialog dialogView = diglogDelete.create();
                dialogView.show();

                Button okBtn = dialogView.getButton(AlertDialog.BUTTON_POSITIVE);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String password = ((EditText) view.findViewById(R.id.modifyPwd)).getText().toString();
                        String passwordChk = ((EditText) view.findViewById(R.id.modifyPwdChk)).getText().toString();

                        if (password.equals(passwordChk) && password.length() > 7 && modifyPwd.getText().toString().equals(password))
                            try {
                                String resUserInfoDelete = new NetworkUserInfo().execute(MainActivity.id, "delete", "", "", "").get();
                                int count = ReplyParser.getResultJson(resUserInfoDelete);
                                if (count == 1) {
                                    Toast.makeText(UserInfoActivity.this, MainActivity.id + "님의 아이디가 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                                    setResult(8, intent);
                                    MainActivity.id = "";
                                    dialogView.dismiss();
                                    finish();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        else
                            Toast.makeText(UserInfoActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                Button cancelBtn = dialogView.getButton(AlertDialog.BUTTON_NEGATIVE);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogView.dismiss();
                        Toast.makeText(UserInfoActivity.this, "취소하였습니다", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        userBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
