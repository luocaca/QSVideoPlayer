package org.song.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.song.demo.CallBack.AjaxCallBack;
import org.song.demo.bean.DispalayM3u8;
import org.song.demo.bean.RoomMsg;
import org.song.demo.bean.YueGuangBean;
import org.song.demo.http.PostUtil;
import org.song.demo.listvideo.CallBack;
import org.song.demo.listvideo.ListCalculator;
import org.song.demo.listvideo.RecyclerViewGetter;
import org.song.videoplayer.ConfigManage;
import org.song.videoplayer.DemoQSVideoView;
import org.song.videoplayer.IVideoPlayer;
import org.song.videoplayer.PlayListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * http://blog.csdn.net/geekqian/article/details/60468734
 * 透明状态栏 的切换
 */
public class ListVideoActivity1 extends AppCompatActivity implements CallBack, SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    List<YueGuangBean.DataBean.RowsBean> data = new ArrayList<>();
    ListCalculator calculator;
    private SwipeRefreshLayout swipe;

    public static void start(Context context) {
        Intent starter = new Intent(context, ListVideoActivity1.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_video);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(this);
        Window window = this.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 5.0 以上全透明状态栏
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏 加下面几句可以去除透明状态栏的灰色阴影,实现纯透明
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            //6.0 以上可以设置状态栏的字体为黑色.使用下面注释的这行打开亮色状态栏模式,实现黑色字体,白底的需求用这句setStatusBarColor(Color.WHITE);
//            window.getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


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


        requeonline(pagev);
        pagev++;

    }

    public void 下一页(View view) {

        onRefresh();
    }


    private int pagev = 1;

    private void requeonline(final int page) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Map<String, String> objectObjectHashMap = new HashMap<>();
                objectObjectHashMap.put("page", page + "");
                try {
                    final String post = PostUtil.post("https://168c2gu7523c0fnur425.bxguwen.com/minivod/reqlist?_t=" + System.currentTimeMillis(),
                            null, objectObjectHashMap, null);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            渲染recy(post);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();


    }

    private void 渲染recy(String post) {

        data.clear();
        YueGuangBean yueGuangBean = new Gson().fromJson(post, YueGuangBean.class);

        data.addAll(yueGuangBean.getData().getRows());

        recyclerView.getAdapter().notifyDataSetChanged();

    }


    String pagePath = "https://168c2gu7523c0fnur425.bxguwen.com/minivod/reqlist?_t=1588697403168";

    DemoQSVideoView demoQSVideoView;

    @Override
    public void activeOnScrolled(View newActiveView, int position) {
        demoQSVideoView = (DemoQSVideoView) newActiveView.findViewById(R.id.qs);
        demoQSVideoView.play();
        demoQSVideoView.setVisibility(View.VISIBLE);
        demoQSVideoView.enterFullMode = 3;
        Log.d("activeOnScrolled", "" + position);
    }

    @Override
    public void activeOnScrolling(View newActiveView, int position) {
        Log.d("activeOnScrolled", "" + position);
    }

    @Override
    public void deactivate(View currentView, int position) {
        DemoQSVideoView demoQSVideoView = (DemoQSVideoView) currentView.findViewById(R.id.qs);
        demoQSVideoView.release();
        demoQSVideoView.setVisibility(View.GONE);
        Log.d("deactivate", "" + position);
    }

    @Override
    public void onBackPressed() {
        if (demoQSVideoView != null && demoQSVideoView.onBackPressed())
            return;
        super.onBackPressed();
    }

    @Override
    public void onRefresh() {

        requeonline(pagev);
        pagev++;
    }

    class Adapter extends RecyclerView.Adapter<Holder> {

        List<YueGuangBean.DataBean.RowsBean> data;

        Adapter(List<YueGuangBean.DataBean.RowsBean> data) {
            this.data = data;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(View.inflate(ListVideoActivity1.this, R.layout.item_video, null));
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

        DemoQSVideoView demoQSVideoView;
        ImageView iv_bg;
        TextView title;

        Holder(View itemView) {
            super(itemView);
            demoQSVideoView = (DemoQSVideoView) itemView.findViewById(R.id.qs);
            iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
            title = (TextView) itemView.findViewById(R.id.title);
            demoQSVideoView.setPlayListener(new PlayListener() {
                @Override
                public void onStatus(int status) {

                }

                @Override
                public void onMode(int mode) {

                }

                @Override
                public void onEvent(int what, Integer... extra) {
                    if (what == IVideoPlayer.EVENT_PREPARE_START) {
                        //ConfigManage.releaseOther(demoQSVideoView);
                        calculator.setCurrentActiveItem(getLayoutPosition());
                    }
                }
            });
            demoQSVideoView.isShowWifiDialog = false;
        }

        public void bindData(final YueGuangBean.DataBean.RowsBean rowsBean) {
//            final String[] arr = s.split(",");

            if (rowsBean != null && rowsBean.getVodrow() != null && rowsBean.getVodrow().getVodid() != null) {

                title.setText(rowsBean.getVodrow().getTitle());

                PostUtil.asyncGet("https://168c2gu7523c0fnur425.bxguwen.com/minivod/reqplay/" + rowsBean.getVodrow().getVodid(), new AjaxCallBack() {
                    @Override
                    public void onSucceed(String json) {


                        try {

                            DispalayM3u8 dispalayM3u8 = new Gson().fromJson(json, DispalayM3u8.class);

                            demoQSVideoView.setUp(dispalayM3u8.getData().getHttpurl());

                        } catch (Exception e) {
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


//            demoQSVideoView.getCoverImageView().setImageURI();
//            Toast.makeText(ListVideoActivity.this, "" + arr[2], Toast.LENGTH_SHORT).show();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Glide.with(ListVideoActivity.this).load(arr[2]).centerCrop().into(demoQSVideoView.getCoverImageView());
//                }
//            }, 2000);

            try {
                Glide.with(ListVideoActivity1.this).load(rowsBean.getVodrow().getCoverpic()).centerCrop().placeholder(R.drawable.place).into(iv_bg);
//                demoQSVideoView.getCoverImageView().setImageResource(R.drawable.place);
                RelativeLayout.LayoutParams l = new RelativeLayout.LayoutParams(-1, (int) (((int) (Math.random() * 600) + 100) * getResources().getDisplayMetrics().density));
                demoQSVideoView.setLayoutParams(l);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public void onStop() {
        super.onStop();
        ConfigManage.releaseAll();
    }


}
