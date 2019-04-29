package com.jx.jsoupdemo.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Test {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://www.52pojie.cn/forum.php?mod=guide&view=digest").get();
                    Elements links = doc.select("tbody");

                    int count = 0;
                    for (Element element : links) {
                        //每条数据
                        Elements byLinks = element.select("td.by");
                        //标题部分
                        Elements titleLinks = element.select("th.common");
                        //回复-查看部分
                        Elements replayLinks = element.select("td.num");

                        getLabelText(titleLinks, ".xst");//标题
                        getLabelText(byLinks, "a[c]"); //作者
                        getLabelText(byLinks, "em"); //发布时间
                        getLabelText(replayLinks, "a.xi2"); //回复次数
                        getLabelText(replayLinks, "em"); //查看次数
                        getLabelText(byLinks, "a[target]"); //所在板块
                        getAttrText(titleLinks, "a.xst", "href");

                        count += 1;
                    }
                    System.out.println("count = " + count);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }

    private static String getLabelText(Elements links, String select) {
        Elements selectLinks = links.select(select);
        Element firstEle = selectLinks.first();
        if (firstEle != null) {
            System.out.println("firstEle = " + firstEle.text());
            return firstEle.text();
        }
        return "";
    }

    private static String getAttrText(Elements links, String select, String attr) {
        Elements selectLinks = links.select(select);
        Element firstEle = selectLinks.first();
        if (firstEle != null) {
            System.out.println("firstEle = " + firstEle.attr(attr));
            return firstEle.text();
        }
        return "";
    }

}
