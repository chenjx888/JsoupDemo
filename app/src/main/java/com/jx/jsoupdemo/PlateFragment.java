package com.jx.jsoupdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlateFragment extends Fragment {

    private RecyclerView rv;
    private ProgressBar progressBar;
    private TitleAdapter mAdapter;
    private List<TitleBean> mData;

    private String url;

    public static PlateFragment newInstanct(String url) {
        PlateFragment fragment = new PlateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plate, container, false);

        url = getArguments().getString("url");

        mData = new ArrayList<>();
        rv = view.findViewById(R.id.rv);
        progressBar = view.findViewById(R.id.progressBar);
        mAdapter = new TitleAdapter(getActivity(), mData);
        rv.setAdapter(mAdapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(url).get();
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

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
//                    Log.e("TAG","count = " + count);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return view;
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
