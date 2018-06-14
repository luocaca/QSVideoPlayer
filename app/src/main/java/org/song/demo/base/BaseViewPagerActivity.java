package org.song.demo.base;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import org.song.demo.R;
import org.song.demo.adapter.FragmentPagerAdapter_TabLayout;

import java.util.List;


public abstract class BaseViewPagerActivity extends BaseActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FragmentPagerAdapter mPagerAdapter;
//    private FragmentPagerAdapter_TabLayout

    public FragmentPagerAdapter getmPagerAdapter() {
        return mPagerAdapter;
    }

    @Override
    public void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);


        setUpViewPager(mViewPager, mTabLayout);

    }

    protected void setUpViewPager(ViewPager viewPager, TabLayout tabLayout) {
        if (mPagerAdapter == null) {
            mPagerAdapter = new FragmentPagerAdapter_TabLayout(getSupportFragmentManager(), bindTitleList(), bindFragmentList());
        }
        viewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public int bindLayoutID() {
        return R.layout.activity_view_pager;
    }


    public abstract List<String> bindTitleList();

    public abstract List<? extends BaseLazyFragment> bindFragmentList();


}
