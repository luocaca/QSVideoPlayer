package org.song.demo.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import org.song.demo.base.BaseLazyFragment;

import java.util.List;

/**
 * view pager 适配器   防止回收fragment
 */

public class FragmentPagerAdapter_TabLayout extends FragmentPagerAdapter {


    private List<String> list_title;
    private  List<? extends BaseLazyFragment> list_fragment;

    public FragmentPagerAdapter_TabLayout(FragmentManager fm, List<String> list_title, List<? extends BaseLazyFragment> list_fragment) {
        super(fm);
        this.list_title = list_title;
        this.list_fragment = list_fragment;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_fragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list_title.get(position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);


    }

}
