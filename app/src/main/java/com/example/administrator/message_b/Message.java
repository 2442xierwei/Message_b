package com.example.administrator.message_b;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2017/1/9.
 */
public class Message extends AppCompatActivity {
    EditText ms_ed;
    Button ms_send;
    private RecyclerView mRecyclerView;
    private List<HashMap<String, String>> mDatas;
    private HomeAdapter mAdapter;
    Handler handler;
    Handler handler1;
    Handler handler2;
    String messageurl = "http://www.xingkong.us/home/android_test/1/comment.php";
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_board);
        final Intent lg = getIntent();
        final String username = lg.getStringExtra("username");
        final String username1 = lg.getStringExtra("username1");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        ms_ed = (EditText) findViewById(R.id.ms_ed);
        ms_send = (Button) findViewById(R.id.ms_send);
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                client cregister = new client(messageurl + "?s0=" + "&n=11", handler);
                cregister.start();
            }
        });
        ms_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler1 = new Handler() {
                    @Override
                    public void handleMessage(android.os.Message msg) {
                        String result = msg.obj.toString();
                        if (result.equals("error:0")) {
                            Toast.makeText(Message.this, R.string.wrong6, Toast.LENGTH_SHORT).show();
                        } else if (result.equals("error:1")) {
                            Toast.makeText(Message.this, R.string.wrong7, Toast.LENGTH_SHORT).show();
                        } else if (result.equals("succ")) {
                            Toast.makeText(Message.this, R.string.ms_succ, Toast.LENGTH_SHORT).show();
                            swipeRefreshLayout.setRefreshing(true);
                            client cregister = new client(messageurl + "?s0=" + "&n=11", handler);
                            cregister.start();
                        }
                    }
                };

                client cregister = null;
                try {
                    cregister = new client(messageurl + "?s=send" + "&u=" + username + "&w=" + URLEncoder.encode(ms_ed.getText().toString(), "utf-8"), handler1);
                    cregister.start();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ms_ed.setText("");
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        //从Intent当中根据key取得value
        if (lg != null) {
            toolbar.setSubtitle(username);
        } else {
            toolbar.setSubtitle(username1);
        }
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toolbar_exit:
                        exit();
                        finish();
                        break;
                }
                return false;
            }
        });
        initData();
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter());
        handler = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                String[] result = msg.obj.toString().split("<br>");
                mDatas.clear();
                for (String s : result) {
                    String[] result2 = s.split("\\[s\\]");
                    HashMap<String, String> d = new HashMap<String, String>();
                    if (result2.length >= 3) {
                        d.put("a", result2[0]);
                        d.put("b", result2[1]);
                        d.put("c", result2[2]);
                        mDatas.add(d);
                    }


                }
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        };
        client cregister = new client(messageurl + "?s0=" + "&n=11", handler);
        cregister.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.u, menu);
        return true;
    }

    protected void initData() {
        mDatas = new ArrayList<HashMap<String, String>>();

    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    Message.this).inflate(R.layout.cardview, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(HomeAdapter.MyViewHolder holder, int position) {

            holder.tv.setText(mDatas.get(position).get("a"));
            holder.tv1.setText(mDatas.get(position).get("c"));
            holder.tv2.setText(mDatas.get(position).get("b"));
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends ViewHolder {

            TextView tv;
            TextView tv1;
            TextView tv2;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.mg_user);
                tv1 = (TextView) view.findViewById(R.id.mg_time);
                tv2 = (TextView) view.findViewById(R.id.mg_message);

            }
        }
    }
    private void exit(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                Context.MODE_PRIVATE);
       sharedPreferences.edit().clear().commit();
    }
}

