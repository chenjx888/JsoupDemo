package com.jx.jsoupdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private RecyclerView rv;
    private TextView tv_permission;
    private ProgressBar progressBar;

    private DetailAdapter mAdapter;

    private String url = "https://www.52pojie.cn/";

    private List<DetailBean> mData;

    public static void start(Context context, String url) {
        context.startActivity(new Intent(context, DetailActivity.class).putExtra("url", url));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        rv = findViewById(R.id.rv);
        tv_permission = findViewById(R.id.tv_permission);
        progressBar = findViewById(R.id.progressBar);

        mData = new ArrayList<>();
        mAdapter = new DetailAdapter(this, mData);
        rv.setAdapter(mAdapter);

        url += getIntent().getStringExtra("url");
        Log.e("TAG", "url = " + url);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Document doc = null;
                try {
                    doc = Jsoup.connect(url).get();

                    List<Map<String, String>> names = getNameAvatar(doc);
                    List<String> contents = getContent(doc);
                    List<String> times = getTime(doc);
                    for (int i = 0; i < names.size(); i++) {
                        mData.add(new DetailBean(names.get(i).get("name"), names.get(i).get("avatar"), times.get(i), contents.get(i)));
                    }

                    if (mData.size() == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                tv_permission.setText("抱歉，本帖要求阅读权限高于 10 才能浏览");
                            }
                        });
                    }

                    Log.e("TAG", "mData = " + mData.size());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            mAdapter.notifyDataSetChanged();
                        }
                    });

                } catch (IOException e) {
                    Toast.makeText(DetailActivity.this, "解析错误", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获取头像和名字
     */
    private List<Map<String, String>> getNameAvatar(Document doc) {
        List<Map<String, String>> data = new ArrayList<>();
        Elements elements = doc.select("td[rowspan]");
        for (Element element : elements) {
            Map<String, String> map = new HashMap<>();
            map.put("avatar", element.select("div.avatar").select("img").attr("src"));
            map.put("name", element.select("a.xw1").text());
            data.add(map);
        }
        return data;
    }

    /**
     * 获取回复内容
     */
    private List<String> getContent(Document doc) {
        List<String> data = new ArrayList<>();
        Elements elements = doc.select("div.t_fsz");
        //有时候取不到t_fsz
        if (elements.size() == 0) {
            elements = doc.select("table").select("td.t_f");
        }

        for (Element element : elements) {
            //删除图片附带的多余div
            elements.select("div.tip").remove();
            //替换img标签内的src属性为file
            Elements imgElements = element.getElementsByTag("img");

            for (int i = 0; i < imgElements.size(); i++) {
                Element ele = imgElements.get(i);
                String file = ele.attr("file");
                if (!TextUtils.isEmpty(file)) {
                    ele.attr("src", file);
                }
            }
            data.add(element.html());
        }
        return data;
    }

    /**
     * 获取回复时间
     */
    private List<String> getTime(Document doc) {
        List<String> data = new ArrayList<>();
        Elements elements = doc.select("div.authi").select("em");
        for (Element element : elements) {
            data.add(element.text());
        }
        return data;
    }

}
