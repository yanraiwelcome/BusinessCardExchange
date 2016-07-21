package com.project.businesscardexchange.yan.businesscardexchange.ui.setting;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import  com.project.businesscardexchange.yan.businesscardexchange.R;
import com.project.businesscardexchange.yan.businesscardexchange.ui.common.BaseActivity;
import com.project.businesscardexchange.yan.businesscardexchange.ui.common.FragmentAdapter;
import com.project.businesscardexchange.yan.businesscardexchange.ui.setting.fragment.ReceivedApp;
import com.project.businesscardexchange.yan.businesscardexchange.ui.setting.fragment.ReceivedPicture;

import java.util.ArrayList;
import java.util.List;


public class FileBrowseActivity extends BaseActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browse);
Log.e("Browse","onCreate");
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_receive_browse_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        initWidget();
    }

    private void initWidget() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.card));
        titles.add(getString(R.string.picture));

        mTabLayout = (TabLayout) findViewById(R.id.activity_receive_browse_tabLayout);
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));

        mViewPager = (ViewPager) findViewById(R.id.activity_receive_browse_viewpager);

        List<android.support.v4.app.Fragment> fragments = new ArrayList<>();
        fragments.add(ReceivedApp.newInstance());
        fragments.add(ReceivedPicture.newInstance());

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),
                fragments, titles);
        mViewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
    }
}
