package com.jx.jsoupdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    private TextView tv;
    private String url = "https://www.52pojie.cn/";

    public static void start(Context context, String url) {
        context.startActivity(new Intent(context, DetailActivity.class).putExtra("url", url));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tv = findViewById(R.id.tv);

        url += getIntent().getStringExtra("url");
        Log.e("TAG", "url = " + url);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Document doc = null;
                try {
                    doc = Jsoup.connect(url).get();

                    Elements elements = doc.select("div.t_fsz");
//                    Elements elements1 = elements.select("tr").select("td.t_f");
                    Element element = elements.get(0);


                    Elements imgElements = element.getElementsByTag("img");
                    element.select("div.tip").remove();

                    for (int i = 0; i < imgElements.size(); i++) {
                        Element ele = imgElements.get(i);
                        Log.e("TAG", " img = " + ele.attr("file"));
                        ele.attr("src", ele.attr("file"));
                    }

                    final String detail = element.html();

                    Log.e("TAG", "detail - " + detail);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            tv.setText(Html.fromHtml(detail, new URLImageParser(tv, DetailActivity.this), null));
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
