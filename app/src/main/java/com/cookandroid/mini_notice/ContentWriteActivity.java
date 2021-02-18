package com.cookandroid.mini_notice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cookandroid.mini_notice.NetWork.NetworkContentGet;
import com.cookandroid.mini_notice.NetWork.NetworkContentInsert;
import com.cookandroid.mini_notice.NetWork.NetworkContentUpdate;
import com.cookandroid.mini_notice.NetWork.NetworkContentsListGet;

public class ContentWriteActivity extends Activity {
    ImageView contentWriteBackBtn;
    EditText inputTitle, inputContents;
    Button attachBtn, submitBtn;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_insert);
        contentWriteBackBtn = (ImageView) findViewById(R.id.contentWriteBackBtn);
        inputTitle = (EditText) findViewById(R.id.input_title);
        inputContents = (EditText) findViewById(R.id.input_contents);

        attachBtn = (Button) findViewById(R.id.attachBtn);
        submitBtn = (Button) findViewById(R.id.submitBtn);

        Intent intent = getIntent();
        if (intent.getExtras().getString("id") != null)
            id = intent.getExtras().getString("id");

        contentWriteBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = getIntent();
                setResult(2, intent2);

                finish();
            }
        });

        attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 10);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = inputTitle.getText().toString();
                String content = inputContents.getText().toString();

                if (id != null) {
                    new NetworkContentInsert().execute(title, content, id);
                    Intent intent2 = getIntent();
                    setResult(2, intent2);
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
