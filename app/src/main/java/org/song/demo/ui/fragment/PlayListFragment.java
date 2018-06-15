package org.song.demo.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.song.demo.CallBack.AjaxCallBack;
import org.song.demo.CommonUtils.GsonUtil;
import org.song.demo.R;
import org.song.demo.base.BaseLazyFragment;
import org.song.demo.bean.ZhuBoGsonBean;
import org.song.demo.constant.DoMain;
import org.song.demo.http.PostUtil;
import org.song.demo.listvideo.CallBack;
import org.song.demo.listvideo.ListCalculator;
import org.song.demo.listvideo.RecyclerViewGetter;
import org.song.videoplayer.DemoQSVideoView;
import org.song.videoplayer.IVideoPlayer;
import org.song.videoplayer.PlayListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 播放列表 fragment
 */

public class PlayListFragment extends BaseLazyFragment implements CallBack {

    private RecyclerView mRecyclerView;
    ListCalculator calculator;
    List<String> data = new ArrayList<>();


    public static PlayListFragment newInstance(String url) {
        String hostUrl = DoMain.Host + url;
        PlayListFragment playListFragment = new PlayListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", hostUrl);
        playListFragment.setArguments(bundle);
        return playListFragment;
    }


    public String getUrl() {
//        Toast.makeText(mActivity, "" + getArguments().getString("url", ""), Toast.LENGTH_SHORT).show();

        return getArguments().getString("url", "");
    }


    @Override
    protected void onFragmentVisibleChange(boolean b) {

        if (b) {
            if (demoQSVideoView != null) {
//                demoQSVideoView.play();
                demoQSVideoView.setiMediaControl(1);
                demoQSVideoView.enterFullMode = 3;
                demoQSVideoView.play();
                demoQSVideoView.setVisibility(View.VISIBLE);


            }
        } else {
            if (demoQSVideoView != null) {
//                demoQSVideoView.pause();
                demoQSVideoView.setVisibility(View.GONE);
                demoQSVideoView.releaseInThread();


            }
        }

    }

    @Override
    protected void onFragmentFirstVisible() {

        mRecyclerView = getView(R.id.recyclerView);


        PostUtil.asyncGet(getUrl(), new AjaxCallBack() {
            @Override
            public void onSucceed(String json) {
//                Toast.makeText(mActivity, "" + GsonUtil.formatJson2String(json), Toast.LENGTH_SHORT).show();


                ZhuBoGsonBean zhuBoGsonBean = null;
                try {
                    zhuBoGsonBean = GsonUtil.formateJson2Bean(json, ZhuBoGsonBean.class);
                    setUpRecycleView(mRecyclerView, zhuBoGsonBean);

                } catch (Exception e) {
                    Toast.makeText(mActivity, "数据解析失败", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailed(Throwable throwable, int errorCode, String errorMsg) {

            }

            @Override
            public void onCancle() {

            }
        });


    }


    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected int bindLayoutID() {
        return R.layout.fragment_play_list;
    }

    DemoQSVideoView demoQSVideoView;

    @Override
    public void activeOnScrolled(View newActiveView, int position) {
        demoQSVideoView = (DemoQSVideoView) newActiveView.findViewById(R.id.qs);
        if (demoQSVideoView != null) {
            demoQSVideoView.setiMediaControl(1);
            demoQSVideoView.enterFullMode = 3;
            demoQSVideoView.play();
            demoQSVideoView.setVisibility(View.VISIBLE);
        }

        Log.d("activeOnScrolled", "" + position);
    }

    @Override
    public void activeOnScrolling(View newActiveView, int position) {
        Log.d("activeOnScrolled", "" + position);
    }

    @Override
    public void deactivate(View currentView, int position) {
        final DemoQSVideoView demoQSVideoView = (DemoQSVideoView) currentView.findViewById(R.id.qs);
        if (demoQSVideoView != null) {
            demoQSVideoView.setVisibility(View.GONE);
            demoQSVideoView.releaseInThread();

        }
        Log.d("deactivate", "" + position);


    }

    class Adapter extends RecyclerView.Adapter<Holder> {

        List<String> data;

        Adapter(List<String> data) {
            this.data = data;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(View.inflate(mActivity, R.layout.item_video_recycle, null));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.bindData(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }


    class Holder extends RecyclerView.ViewHolder {

        DemoQSVideoView qsVideoView;
        ImageView iv_bg;

        Holder(View itemView) {
            super(itemView);

            qsVideoView = (DemoQSVideoView) itemView.findViewById(R.id.qs);
            iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
            qsVideoView.setPlayListener(new PlayListener() {
                @Override
                public void onStatus(int status) {

                }

                @Override
                public void onMode(int mode) {

                }

                @Override
                public void onEvent(int what, Integer... extra) {
                    if (what == IVideoPlayer.EVENT_PREPARE_START) {
                        //ConfigManage.releaseOther(qsVideoView);
                        calculator.setCurrentActiveItem(getLayoutPosition());
                    }
                }
            });
            qsVideoView.isShowWifiDialog = false;
        }

        public void bindData(String s) {
            String[] arr = s.split(",");
            qsVideoView.setUp(arr[1], arr[0]);
            Glide.with(mActivity).load(arr[2]).placeholder(R.mipmap.loading).crossFade(300).error(R.mipmap.dong).into(iv_bg);
//            qsVideoView.getCoverImageView().setImageResource(R.mipmap.ic_launcher);
            FrameLayout.LayoutParams l = new FrameLayout.LayoutParams(-1, (int) (((int) (Math.random() * 600) + 100) * getResources().getDisplayMetrics().density));
            //qsVideoView.setLayoutParams(l);
        }

    }


    private void setUpRecycleView(RecyclerView recyclerView, ZhuBoGsonBean zhuBoGsonBean) {


        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        for (int i = 0; i < zhuBoGsonBean.zhubo.size(); i++)
            data.add(zhuBoGsonBean.zhubo.get(i).title + "," + zhuBoGsonBean.zhubo.get(i).address + "," + zhuBoGsonBean.zhubo.get(i).img);


        // http://www.luocaca.cn/hello-ssm/m3u8/20.m3u8
        recyclerView.setAdapter(new Adapter(data));


        calculator = new ListCalculator(new RecyclerViewGetter((LinearLayoutManager) recyclerView.getLayoutManager(), recyclerView), this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int newState = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                this.newState = newState;
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    calculator.onScrolled(300);
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                calculator.onScrolling(newState);

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (demoQSVideoView != null) {
//                demoQSVideoView.pause();
            demoQSVideoView.setVisibility(View.GONE);
            demoQSVideoView.releaseInThread();


        }
    }
}
