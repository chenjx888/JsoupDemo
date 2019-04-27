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
                    doc = Jsoup.connect("https://www.52pojie.cn/thread-845252-1-1.html").get();
//                    doc = Jsoup.connect("https://www.52pojie.cn/thread-941815-1-1.html").get();

//                    Elements elements = doc.select("div.t_fsz");//每层楼的回复内容
                    Elements elements = doc.select("div.alert_info");//要求权限
//                    Elements elements = doc.select("table").select("td.t_f");//每层楼的回复内容
//                    Elements elements = doc.select("div.authi").select("em");//每层楼的回复时间

//                    Elements elements = doc.select("td[rowspan]").select("div.avatar").select("img");//头像列表
//                    Elements elements = doc.select("td[rowspan]").select("a.xw1");//名字列表

//                    //移除图片多余div
//                    elements.select("div.tip").remove();

//                    Elements imgElements = elements.select("img");
//                    for (int i = 0; i < imgElements.size(); i++) {
//                        Element element = imgElements.get(i);
//                        imgElements.set(i, element.attr("file", element.attr("file")));
//                    }

                    int count = 0;
                    for (Element element : elements) {
                        System.out.println("html =" + element.text());
                        count +=1 ;
                    }

                    System.out.println("count = " + count);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
