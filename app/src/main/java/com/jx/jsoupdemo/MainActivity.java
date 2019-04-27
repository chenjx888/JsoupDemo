package com.jx.jsoupdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private List<PlateBean> mPlateBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        mPlateBeans = new ArrayList<>();
        mPlateBeans.add(new PlateBean("最新发表", "https://www.52pojie.cn/forum.php?mod=guide&view=newthread"));
        mPlateBeans.add(new PlateBean("技术新帖", "https://www.52pojie.cn/forum.php?mod=guide&view=tech"));
        mPlateBeans.add(new PlateBean("人气热门", "https://www.52pojie.cn/forum.php?mod=guide&view=hot"));
        mPlateBeans.add(new PlateBean("最新精华", "https://www.52pojie.cn/forum.php?mod=guide&view=digest"));

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return PlateFragment.newInstanct(mPlateBeans.get(i).getUrl());
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mPlateBeans.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return mPlateBeans.size();
        }
    }

    private class PlateBean {
        private String title;
        private String url;

        public PlateBean(String title, String url) {
            this.title = title;
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }


}
