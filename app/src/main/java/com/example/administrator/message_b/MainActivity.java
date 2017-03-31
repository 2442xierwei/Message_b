package com.example.administrator.message_b;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String TAG;
    EditText lg_user;
    EditText lg_pass;
    Button lg_bt1;
    Button lg_bt2;
    Handler handler;
    Handler handler1;
    String loginurl = "http://www.xingkong.us/home/android_test/1/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        findViewById();
        Intent rg = getIntent();
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                String result = msg.obj.toString();
                //返回值信息判断
                if (result.equals("succ")) {
                    Toast.makeText(MainActivity.this, R.string.lg_succ, Toast.LENGTH_LONG).show();
                    Intent lg_mg = new Intent();
                    lg_mg.setClass(MainActivity.this, Message.class);
                    lg_mg.putExtra("username", lg_user.getText().toString());
                    startActivity(lg_mg);
                } else if (result.equals("error:0")) {
                    Toast.makeText(MainActivity.this, R.string.wrong0, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, R.string.wrong2, Toast.LENGTH_SHORT).show();
                }
            }
        };
        check();
        if (rg != null) {
            String username = rg.getStringExtra("username");
            String password = rg.getStringExtra("password");
            lg_user.setText(username);
            lg_pass.setText(password);
        }
        lg_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lg_rg = new Intent();
                lg_rg.setClass(MainActivity.this, Register.class);
                startActivity(lg_rg);
            }
        });
        lg_bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 账密判定
                if (lg_user.getText().toString().trim().equals("")
                        || lg_pass.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.wrong1,
                            Toast.LENGTH_LONG).show();
                } else {
                    save(lg_user.getText().toString(), lg_pass.getText().toString());
                    try {
                        client cregister = null;
                        cregister = new client(loginurl + "?u=" + URLEncoder.encode(lg_user.getText().toString(), "utf-8") + "&p=" + URLEncoder.encode(lg_pass.getText().toString(), "utf-8"), handler);
                        cregister.start();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void findViewById() {
        lg_bt1 = (Button) findViewById(R.id.lg_bt1);
        lg_bt2 = (Button) findViewById(R.id.lg_bt2);
        lg_user = (EditText) findViewById(R.id.lg_user);
        lg_pass = (EditText) findViewById(R.id.lg_pass);
    }

    private void save(String u, String p) {
        System.out.printf("issave____________________________________________________");
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Islogin", "1");
        editor.putString("Username", u);
        editor.putString("Password", p);
        editor.apply();
    }

    private void check() {
        SharedPreferences sharedPreferences = getSharedPreferences("user",
              Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
        String userId = sharedPreferences.getString("Username", "");
        String userPwd = sharedPreferences.getString("Password", "");
        String isLogin = sharedPreferences.getString("Islogin", "");
        if (isLogin.equals("1")) {
            handler1=new Handler() {
                @Override
                public void handleMessage(android.os.Message msg) {
                    String result = msg.obj.toString();
                    System.out.printf("dasdasd");
                }
            };
            client cregister = new client(loginurl +"?u=xierwei" + "&p=xierwei", handler1);
            cregister.start();
            Intent lg_mg1 = new Intent();
            lg_mg1.setClass(MainActivity.this, Message.class);
            startActivity(lg_mg1);
           /* startActivity(lg_mg1);
            try {
                System.out.println("check____asdasdasdasdas_____________________________"+userId+userPwd+isLogin);
                cregister1 = new client(loginurl + "?u=xierwei" +URLEncoder.encode(userId,"utf-8")+"&p="+URLEncoder.encode(userPwd,"utf-8"), handler);
                cregister1.start();
                startActivity(lg_mg1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
/*            lg_mg1.putExtra("username1",userId);*/


        }
    }

}
