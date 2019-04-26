package com.jx.jsoupdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private TitleAdapter mAdapter;
    private List<TitleBean> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mData = new ArrayList<>();
        rv = findViewById(R.id.rv);
        mAdapter = new TitleAdapter(this, mData);
        rv.setAdapter(mAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://www.52pojie.cn/forum.php?mod=guide&view=digest").get();
                    Elements links = doc.select("tbody");

//                    int count = 0;
                    for (Element element : links) {
                        //每条数据
                        Elements byLinks = element.select("td.by");
                        //标题部分
                        Elements titleLinks = element.select("th.common");
                        //回复-查看部分
                        Elements replayLinks = element.select("td.num");

                        String title = getLabelText(titleLinks, ".xst");//标题
                        String author = getLabelText(byLinks, "a[c]"); //作者
                        String time = getLabelText(byLinks, "em"); //发布时间
                        String replayCount = getLabelText(replayLinks, "a.xi2"); //回复次数
                        String lookCount = getLabelText(replayLinks, "em"); //查看次数
                        String plate = getLabelText(byLinks, "a[target]"); //所在板块
                        String url = getAttrText(titleLinks, "a.xst", "href");

                        if (TextUtils.isEmpty(title)) continue;

                        mData.add(new TitleBean(title, author, time, replayCount, lookCount, plate, url));
//                        count += 1;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
//                    System.out.println("count = " + count);

                    //<table cellspacing="0" cellpadding="0"><tr>

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String getLabelText(Elements links, String select) {
        Elements selectLinks = links.select(select);
        Element firstEle = selectLinks.first();
        if (firstEle != null) {
            System.out.println("firstEle = " + firstEle.text());
            return firstEle.text();
        }
        return "";
    }

    private String getAttrText(Elements links, String select, String attr) {
        Elements selectLinks = links.select(select);
        Element firstEle = selectLinks.first();
        if (firstEle != null) {
            System.out.println("firstEle = " + firstEle.attr(attr));
            return firstEle.attr(attr);
        }
        return "";
    }

}
