package com.jx.jsoupdemo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Test2 {

    //<table cellspacing="0" cellpadding="0"><tr>

    //<ignore_js_op> 图片

    public static void main(String[] args) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://www.52pojie.cn/thread-937873-1-1.html").get();

                    Elements elements = doc.select("div.t_fsz");
                    Elements elements1 = elements.select("tr").select("td.t_f");
                    Elements imgElements = elements.select("img");
                    for (int i = 0; i < imgElements.size(); i++) {
                        Element element = imgElements.get(i);
                        imgElements.set(i, element.attr("file", element.attr("file")));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
