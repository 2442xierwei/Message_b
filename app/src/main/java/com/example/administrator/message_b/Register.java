package com.example.administrator.message_b;

import android.app.Activity;
import android.content.Intent;
import android.os.*;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/1/9.
 */
public class Register extends AppCompatActivity {
    EditText rg_user;
    EditText rg_pass;
    EditText rg_repass;
    Button rg_bt;
    Button rg_back;
    Handler handler;
    String registerurl = "http://www.xingkong.us/home/android_test/1/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        findViewById();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.obj.toString();
                if (result.equals("succ")) {
                    Toast.makeText(Register.this, R.string.rg_succ, Toast.LENGTH_SHORT).show();
                    Intent rg_lg = new Intent();
                    rg_lg.setClass(Register.this, MainActivity.class);
                    rg_lg.putExtra("username",rg_user.getText().toString());
                    rg_lg.putExtra("password",rg_pass.getText().toString());
                    startActivity(rg_lg);
                    finish();

                } else if (result.equals("error:0")) {
                    Toast.makeText(Register.this, R.string.wrong4, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Register.this, R.string.wrong2, Toast.LENGTH_SHORT).show();
                }
            }
        };
        rg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent();
                back.setClass(Register.this, MainActivity.class);
                startActivity(back);
                finish();
            }
        });
        rg_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断账密规范
                if (rg_user.getText().toString().trim().equals("")
                        || rg_pass.getText().toString().trim().equals("") || rg_repass.getText().toString().trim().equals("")) {
                    Toast.makeText(Register.this,R.string.wrong2, Toast.LENGTH_SHORT).show();
                } else if (rg_pass.getText().toString().length() < 6) {
                    Toast.makeText(Register.this, R.string.wrong3, Toast.LENGTH_SHORT).show();
                } else if (!rg_pass.getText().toString().equals(rg_repass.getText().toString())) {
                    Toast.makeText(Register.this, R.string.wrong5, Toast.LENGTH_SHORT).show();
                } else {
                    //获取网页信息
                    client cregister = new client(registerurl + "?u=" + rg_user.getText().toString() + "&p=" + rg_pass.getText().toString(), handler);
                    cregister.start();
                }
            }
        });
    }
    private void findViewById() {
        rg_bt = (Button) findViewById(R.id.rg_bt);
        rg_user = (EditText) findViewById(R.id.rg_user);
        rg_pass = (EditText) findViewById(R.id.rg_pass);
        rg_repass = (EditText) findViewById(R.id.rg_repass);
        rg_back = (Button) findViewById(R.id.rg_back);
    }
}
