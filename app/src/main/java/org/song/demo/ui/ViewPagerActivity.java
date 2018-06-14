package org.song.demo.ui;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.song.demo.CallBack.AjaxCallBack;
import org.song.demo.CallBack.Subscription;
import org.song.demo.CommonUtils.GsonUtil;
import org.song.demo.base.BaseLazyFragment;
import org.song.demo.base.BaseViewPagerActivity;
import org.song.demo.base.HomeBean;
import org.song.demo.constant.DoMain;
import org.song.demo.http.PostUtil;
import org.song.demo.ui.fragment.PlayListFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * base View Page activity 实现类
 */

public class ViewPagerActivity extends BaseViewPagerActivity {


    private Subscription subscription;

    public static void start(Activity mActivity) {
        mActivity.startActivity(new Intent(mActivity, ViewPagerActivity.class));
    }


    @Override
    public void initData() {
        super.initData();
        subscription = PostUtil.asyncGet(DoMain.Host + DoMain.home, new AjaxCallBack() {
            @Override
            public void onSucceed(String json) {
//                Toast.makeText(ViewPagerActivity.this, "" + GsonUtil.formatJson2String(json), Toast.LENGTH_SHORT).show();

                Type beanType = new TypeToken<HomeBean.PingtaiBean>() {
                }.getType();

                HomeBean pingtai = GsonUtil.formateJson2Bean(json, HomeBean.class);

                Log.i("", "onSucceed: " + pingtai.pingtai.size());


                for (int i = 0; i < pingtai.pingtai.size(); i++) {
                    titles.add(pingtai.pingtai.get(i).title + "(" + pingtai.pingtai.get(i).Number + ")");
                    fragments.add(PlayListFragment.newInstance(pingtai.pingtai.get(i).address));
                }
                getmPagerAdapter().notifyDataSetChanged();


                /**
                 * 0 = {HomeBean$PingtaiBean@5333}
                 Number = "22"
                 address = "jsonxingguang.txt"
                 title = "星光"
                 xinimg = "http://ww1.sinaimg.cn/large/87c01ec7gy1fqi47x1heoj2020020748.jpg"
                 */

            }

            @Override
            public void onFailed(Throwable throwable, int errorCode, String errorMsg) {
                Toast.makeText(ViewPagerActivity.this, "jonFailed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancle() {
                Toast.makeText(ViewPagerActivity.this, "onCancle", Toast.LENGTH_SHORT).show();
            }
        });


    }

    List<BaseLazyFragment> fragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();

    @Override
    public List<String> bindTitleList() {


        return titles;
    }


    @Override
    public List<? extends BaseLazyFragment> bindFragmentList() {

        return fragments;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unSubscribe();
        }
    }
}
