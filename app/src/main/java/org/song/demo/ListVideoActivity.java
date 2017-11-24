package org.song.demo;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.song.demo.bean.RoomMsg;
import org.song.demo.listvideo.CallBack;
import org.song.demo.listvideo.ListCalculator;
import org.song.demo.listvideo.RecyclerViewGetter;
import org.song.videoplayer.ConfigManage;
import org.song.videoplayer.DemoQSVideoView;
import org.song.videoplayer.IVideoPlayer;
import org.song.videoplayer.PlayListener;

import java.util.ArrayList;
import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * http://blog.csdn.net/geekqian/article/details/60468734
 * 透明状态栏 的切换
 */
public class ListVideoActivity extends SwipeBackActivity implements CallBack {

    RecyclerView recyclerView;
    List<String> data = new ArrayList<>();
    ListCalculator calculator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_video);
        setSwipeBackEnable(true);

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

        RoomMsg roomMsg = getFormatJson();
        //模拟网络请求。。获取 json


        if (roomMsg.data == null) {
            Toast.makeText(this, "亲 ， 没有数据了，正在为您切换频道^_^ \n" + "state=" + MainActivity.state + "page" + MainActivity.page, Toast.LENGTH_SHORT).show();
            if (MainActivity.state == 0) {
                MainActivity.state = 1;
            } else if (MainActivity.state == 1) {
                MainActivity.state = 2;
            } else if (MainActivity.state == 2) {
                MainActivity.state = 0;
            }
            MainActivity.page = 0;
            this.finish();
            return;
        }

        for (int i = 0; i < roomMsg.data.size(); i++) {

            RoomMsg.DataBean dataBean = roomMsg.data.get(i);
//            data.add("这是一个标题" + i + ",http://videos.kpie.com.cn/videos/20170526/037DCE54-EECE-4520-AA92-E4002B1F29B0.mp4");
            data.add(dataBean.nickname + "," + getRoomId(dataBean.rid + "") + "," + dataBean.headPic);
        }


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

    class Adapter extends RecyclerView.Adapter<Holder> {

        List<String> data;

        Adapter(List<String> data) {
            this.data = data;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(View.inflate(ListVideoActivity.this, R.layout.item_video, null));
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

        Holder(View itemView) {
            super(itemView);
            demoQSVideoView = (DemoQSVideoView) itemView.findViewById(R.id.qs);
            iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
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

        public void bindData(String s) {
            final String[] arr = s.split(",");
            demoQSVideoView.setUp(arr[1], arr[0]);
//            demoQSVideoView.getCoverImageView().setImageURI();
//            Toast.makeText(ListVideoActivity.this, "" + arr[2], Toast.LENGTH_SHORT).show();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Glide.with(ListVideoActivity.this).load(arr[2]).centerCrop().into(demoQSVideoView.getCoverImageView());
//                }
//            }, 2000);
            Glide.with(ListVideoActivity.this).load(arr[2]).centerCrop().into(iv_bg);
//            demoQSVideoView.getCoverImageView().setImageResource(R.mipmap.ic_launcher);
            FrameLayout.LayoutParams l = new FrameLayout.LayoutParams(-1, (int) (((int) (Math.random() * 600) + 100) * getResources().getDisplayMetrics().density));
            //demoQSVideoView.setLayoutParams(l);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        ConfigManage.releaseAll();
    }


    /**
     * 解析json 对象
     *
     * @param jsonObject
     * @return
     */
    public RoomMsg.DataBean praseJsonFile(JSONObject jsonObject) {

        RoomMsg.DataBean dataBean = new RoomMsg.DataBean();

        try {
            dataBean.rid = jsonObject.getInt("rid");
            dataBean.playStartTime = jsonObject.getInt("playStartTime");
            dataBean.sex = jsonObject.getInt("sex");
            dataBean.mid = jsonObject.getString("mid");
            dataBean.nickname = jsonObject.getString("nickname");
            dataBean.headPic = jsonObject.getString("headPic");
            dataBean.isPlaying = jsonObject.getBoolean("isPlaying");
            dataBean.onlineNum = jsonObject.getInt("onlineNum");
            dataBean.fansNum = jsonObject.getInt("fansNum");
            dataBean.announcement = jsonObject.getString("announcement");
            dataBean.moderatorLevel = jsonObject.getInt("moderatorLevel");
            dataBean.verified = jsonObject.getBoolean("verified");
            dataBean.verifyInfo = jsonObject.getString("verifyInfo");
            dataBean.videoPlayUrl = jsonObject.getString("videoPlayUrl");
            dataBean.weight = jsonObject.getInt("weight");
            dataBean.timeZoneHotWeight = jsonObject.getInt("timeZoneHotWeight");
            dataBean.city = jsonObject.getString("city");
            dataBean.addTime = jsonObject.getString("addTime");
            dataBean.contentPackageId = jsonObject.getInt("contentPackageId");
            dataBean.sHeight = jsonObject.getInt("sHeight");
            dataBean.sWidth = jsonObject.getInt("sWidth");
            dataBean.id = jsonObject.getInt("id");
            dataBean.doMission = jsonObject.getBoolean("doMission");
        } catch (JSONException e) {
            Log.w("praseJsonFile", "解析json 失败" + e.getMessage());
            e.printStackTrace();
            return dataBean;
        }

        return dataBean;

    }

    private RoomMsg getFormatJson() {
        RoomMsg roomMsg = new RoomMsg();
        try {
            JSONObject jsonObject = new JSONObject(json);
            roomMsg.errno = jsonObject.getInt("errno");
            roomMsg.msg = jsonObject.getString("msg");
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            if (jsonArray != null && jsonArray.length() > 0) {
                List<RoomMsg.DataBean> dataBeans = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    dataBeans.add(praseJsonFile((JSONObject) jsonArray.get(i)));
                }
                roomMsg.data = dataBeans;
            }
//            Toast.makeText(this, "errno" + roomMsg.errno, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return roomMsg;
    }


    public String getRoomId(String roomId) {
        return "http://9180.liveplay.myqcloud.com/live/9180_" + roomId + ".m3u8";
    }


    public static String json = "{\n" +
            "    \"errno\": 0,\n" +
            "    \"msg\": \"\",\n" +
            "    \"data\": [\n" +
            "        {\n" +
            "            \"rid\": 623378,\n" +
            "            \"playStartTime\": 1511409342,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"90988\",\n" +
            "            \"nickname\": \"TOP✨明智的选择✨\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_11_22/5f5f51ec9b3047664e0aabae148a6228.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 1827,\n" +
            "            \"fansNum\": 85,\n" +
            "            \"announcement\": \"终点\uD83D\uDC9E停留\uD83D\uDE18走是自己的选择\uD83E\uDD17\",\n" +
            "            \"moderatorLevel\": 10,\n" +
            "            \"verified\": false,\n" +
            "            \"verifyInfo\": \"\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_623378\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000001,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"深圳市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2017-04-09 10:07:25\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"640\",\n" +
            "            \"sWidth\": \"360\",\n" +
            "            \"id\": 623378,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 259463,\n" +
            "            \"playStartTime\": 1511408863,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"3923861\",\n" +
            "            \"nickname\": \"☞米*瑶☜\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_11_23/b579b0b0dfab9e3f93f92971f2c8d9b2.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 2204,\n" +
            "            \"fansNum\": 118747,\n" +
            "            \"announcement\": \"星光签约主播\",\n" +
            "            \"moderatorLevel\": 18,\n" +
            "            \"verified\": true,\n" +
            "            \"verifyInfo\": \"星光签约主播\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_259463\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000204,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"上海市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2016-12-23 13:59:51\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"1280\",\n" +
            "            \"sWidth\": \"720\",\n" +
            "            \"id\": 259463,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 896845,\n" +
            "            \"playStartTime\": 1511407831,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"11152191\",\n" +
            "            \"nickname\": \"甜心戴丝\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_10_15/49bb6a3696ae36b0e3c1394fb1a7998a.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 2187,\n" +
            "            \"fansNum\": 3445,\n" +
            "            \"announcement\": \"星光签约主播\",\n" +
            "            \"moderatorLevel\": 13,\n" +
            "            \"verified\": true,\n" +
            "            \"verifyInfo\": \"星光签约主播\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_896845\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000164,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"岳阳市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2017-09-28 13:48:24\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"960\",\n" +
            "            \"sWidth\": \"540\",\n" +
            "            \"id\": 896845,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 718930,\n" +
            "            \"playStartTime\": 1511409333,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"7970770\",\n" +
            "            \"nickname\": \"小蝶、感谢相遇\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_11_04/780d9b9f9a893d0da489093e88462f77.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 2212,\n" +
            "            \"fansNum\": 167956,\n" +
            "            \"announcement\": \"感冒了\uD83D\uDE2D\",\n" +
            "            \"moderatorLevel\": 19,\n" +
            "            \"verified\": true,\n" +
            "            \"verifyInfo\": \"星光签约主播\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_718930\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000617,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"福州市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2017-05-16 00:19:16\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"1280\",\n" +
            "            \"sWidth\": \"720\",\n" +
            "            \"id\": 718930,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 270005,\n" +
            "            \"playStartTime\": 1511406161,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"4175282\",\n" +
            "            \"nickname\": \"戒饭宝宝啊\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_08_28/71cda3a0c420645ba7bc16f40139c6d6.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 2234,\n" +
            "            \"fansNum\": 206288,\n" +
            "            \"announcement\": \"星光签约主播\",\n" +
            "            \"moderatorLevel\": 22,\n" +
            "            \"verified\": true,\n" +
            "            \"verifyInfo\": \"星光签约主播\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_270005\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000700,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"北京市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2017-01-07 20:18:56\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"1280\",\n" +
            "            \"sWidth\": \"720\",\n" +
            "            \"id\": 270005,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 148499,\n" +
            "            \"playStartTime\": 1511409867,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"1618803\",\n" +
            "            \"nickname\": \"婷大大大\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_10_06/2f812fe83f14b9acf5ed68e9d4553266.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 2208,\n" +
            "            \"fansNum\": 8672,\n" +
            "            \"announcement\": \"拥有你的我比国王更富有！\",\n" +
            "            \"moderatorLevel\": 20,\n" +
            "            \"verified\": false,\n" +
            "            \"verifyInfo\": \"\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_148499\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000658,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"北京市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2016-08-31 23:34:10\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"960\",\n" +
            "            \"sWidth\": \"540\",\n" +
            "            \"id\": 148499,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 722242,\n" +
            "            \"playStartTime\": 1511408269,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"8009156\",\n" +
            "            \"nickname\": \"轻奢\uD83D\uDC8D一笑\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_11_15/b2e9c5474d0dfbc260c9190928dd9b00.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 1810,\n" +
            "            \"fansNum\": 1671,\n" +
            "            \"announcement\": \"少玩儿一会儿，有来的吗？\",\n" +
            "            \"moderatorLevel\": 10,\n" +
            "            \"verified\": false,\n" +
            "            \"verifyInfo\": \"\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_722242\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000006,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"济南市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2017-05-17 10:54:58\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"960\",\n" +
            "            \"sWidth\": \"540\",\n" +
            "            \"id\": 722242,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 632606,\n" +
            "            \"playStartTime\": 1511409652,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"7035211\",\n" +
            "            \"nickname\": \"大鱼宝贝\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_11_11/36e68fe4818d11f4974624c56a7d4941.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 2202,\n" +
            "            \"fansNum\": 844,\n" +
            "            \"announcement\": \"星光签约主播\",\n" +
            "            \"moderatorLevel\": 15,\n" +
            "            \"verified\": true,\n" +
            "            \"verifyInfo\": \"星光签约主播\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_632606\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000401,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"外星人？\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2017-04-12 12:45:13\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"960\",\n" +
            "            \"sWidth\": \"540\",\n" +
            "            \"id\": 632606,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 826690,\n" +
            "            \"playStartTime\": 1511404364,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"9604944\",\n" +
            "            \"nickname\": \"邻家妹妹\uD83D\uDC23嘉宝贝\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_11_16/5199a6a79f5f6bdc5441f3ed74000703.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 2239,\n" +
            "            \"fansNum\": 1321,\n" +
            "            \"announcement\": \"\uD83D\uDE09\",\n" +
            "            \"moderatorLevel\": 16,\n" +
            "            \"verified\": true,\n" +
            "            \"verifyInfo\": \"星光人气主播\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_826690\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000208,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"广州市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2017-07-08 10:33:33\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"960\",\n" +
            "            \"sWidth\": \"540\",\n" +
            "            \"id\": 826690,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 263335,\n" +
            "            \"playStartTime\": 1511409291,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"4036190\",\n" +
            "            \"nickname\": \"涵宝宝吖\uD83D\uDE18\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_10_20/ba9ae7e84612c20d27fa751cc2f05c4a.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 2208,\n" +
            "            \"fansNum\": 54623,\n" +
            "            \"announcement\": \"星光认证主播\",\n" +
            "            \"moderatorLevel\": 16,\n" +
            "            \"verified\": true,\n" +
            "            \"verifyInfo\": \"星光认证主播\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_263335\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000555,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"崇左市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2016-12-29 03:01:54\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"960\",\n" +
            "            \"sWidth\": \"540\",\n" +
            "            \"id\": 263335,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 583202,\n" +
            "            \"playStartTime\": 1511408117,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"6523167\",\n" +
            "            \"nickname\": \"摔不碎的小雪花\uD83C\uDF38\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_11_22/7bbce0d9c5889dc5a94ad7f5337569e6.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 2249,\n" +
            "            \"fansNum\": 2872,\n" +
            "            \"announcement\": \"星光签约主播\",\n" +
            "            \"moderatorLevel\": 16,\n" +
            "            \"verified\": true,\n" +
            "            \"verifyInfo\": \"星光签约主播\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_583202\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000138,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"绥化市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2017-03-29 20:12:41\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"640\",\n" +
            "            \"sWidth\": \"360\",\n" +
            "            \"id\": 583202,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 893778,\n" +
            "            \"playStartTime\": 1511408628,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"10924881\",\n" +
            "            \"nickname\": \"♬田田\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_10_23/5d9ed97a5c7c6bb78abdf7e42e33fe1e.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 1807,\n" +
            "            \"fansNum\": 230,\n" +
            "            \"announcement\": \"中午好\uD83D\uDE0A\",\n" +
            "            \"moderatorLevel\": 10,\n" +
            "            \"verified\": false,\n" +
            "            \"verifyInfo\": \"\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_893778\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000009,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"长春市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2017-09-04 12:50:00\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"640\",\n" +
            "            \"sWidth\": \"360\",\n" +
            "            \"id\": 893778,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 353764,\n" +
            "            \"playStartTime\": 1511408317,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"4963215\",\n" +
            "            \"nickname\": \"甜心小公主\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_09_27/08f7f74c0a7d694eb54f45c928961efe.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 2210,\n" +
            "            \"fansNum\": 14376,\n" +
            "            \"announcement\": \"人最怕什么？\",\n" +
            "            \"moderatorLevel\": 20,\n" +
            "            \"verified\": false,\n" +
            "            \"verifyInfo\": \"\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_353764\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000353,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"外星人？\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2017-02-19 13:57:16\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"960\",\n" +
            "            \"sWidth\": \"540\",\n" +
            "            \"id\": 353764,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 748929,\n" +
            "            \"playStartTime\": 1511399339,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"8361267\",\n" +
            "            \"nickname\": \"凡 妞 ☀\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_10_31/705220790653e594e3b68c296534c6b3.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 2208,\n" +
            "            \"fansNum\": 4527,\n" +
            "            \"announcement\": \"磨人的炸房和任务～\",\n" +
            "            \"moderatorLevel\": 16,\n" +
            "            \"verified\": true,\n" +
            "            \"verifyInfo\": \"星光第一脱口秀女神\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_748929\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000409,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"武汉市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2017-05-27 02:50:19\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"1280\",\n" +
            "            \"sWidth\": \"720\",\n" +
            "            \"id\": 748929,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 182535,\n" +
            "            \"playStartTime\": 1511409080,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"1458711\",\n" +
            "            \"nickname\": \"宠爱*\uD83C\uDF40\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_11_08/ac227e348234e5975e1c2ddc5d835568.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 2214,\n" +
            "            \"fansNum\": 196408,\n" +
            "            \"announcement\": \"\\u2026\\u2026\",\n" +
            "            \"moderatorLevel\": 23,\n" +
            "            \"verified\": true,\n" +
            "            \"verifyInfo\": \"傻白甜\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_182535\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000704,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"外星人？\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2016-10-08 13:01:08\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"960\",\n" +
            "            \"sWidth\": \"540\",\n" +
            "            \"id\": 182535,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 902149,\n" +
            "            \"playStartTime\": 1511403214,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"9358575\",\n" +
            "            \"nickname\": \"舞姬\uD83C\uDF19柳无双\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_10_27/25dc6a8975d9cc823869323c4b8a7db3.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 1855,\n" +
            "            \"fansNum\": 1220,\n" +
            "            \"announcement\": \"守护天使～\",\n" +
            "            \"moderatorLevel\": 10,\n" +
            "            \"verified\": false,\n" +
            "            \"verifyInfo\": \"\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_902149\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000321,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"漳州市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2017-10-27 15:13:58\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"1280\",\n" +
            "            \"sWidth\": \"720\",\n" +
            "            \"id\": 902149,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 898160,\n" +
            "            \"playStartTime\": 1511403784,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"10987290\",\n" +
            "            \"nickname\": \"火柴姑娘✨✨\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_10_18/24082bab65dff583996e215e0b9b0afb.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 1822,\n" +
            "            \"fansNum\": 680,\n" +
            "            \"announcement\": \"上午十点见，下午一点散，不见不散\uD83D\uDC4C\uD83D\uDC37\",\n" +
            "            \"moderatorLevel\": 9,\n" +
            "            \"verified\": false,\n" +
            "            \"verifyInfo\": \"\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_898160\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000107,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"沈阳市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2017-10-06 18:55:06\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"640\",\n" +
            "            \"sWidth\": \"360\",\n" +
            "            \"id\": 898160,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 883287,\n" +
            "            \"playStartTime\": 1511409034,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"10683553\",\n" +
            "            \"nickname\": \"\uD83D\uDC67\uD83C\uDFFB啊莲莲\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_11_10/268b7a9278a2e9c83c5e13ec98334135.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 2207,\n" +
            "            \"fansNum\": 914,\n" +
            "            \"announcement\": \"星光签约主播\",\n" +
            "            \"moderatorLevel\": 16,\n" +
            "            \"verified\": true,\n" +
            "            \"verifyInfo\": \"星光签约主播\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_883287\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000059,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"漳州市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2017-08-06 10:56:20\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"960\",\n" +
            "            \"sWidth\": \"540\",\n" +
            "            \"id\": 883287,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 901922,\n" +
            "            \"playStartTime\": 1511409519,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"11639514\",\n" +
            "            \"nickname\": \"希希很哇塞\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_11_14/46ae790149af06826ee4b2b865459f82.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 1204,\n" +
            "            \"fansNum\": 108,\n" +
            "            \"announcement\": \"在这里就是为了遇见你\",\n" +
            "            \"moderatorLevel\": 5,\n" +
            "            \"verified\": false,\n" +
            "            \"verifyInfo\": \"\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_901922\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000002,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"济宁市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2017-10-26 13:27:48\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"960\",\n" +
            "            \"sWidth\": \"540\",\n" +
            "            \"id\": 901922,\n" +
            "            \"doMission\": true\n" +
            "        },\n" +
            "        {\n" +
            "            \"rid\": 894446,\n" +
            "            \"playStartTime\": 1511409012,\n" +
            "            \"sex\": 2,\n" +
            "            \"mid\": \"10988584\",\n" +
            "            \"nickname\": \"半半\uD83C\uDFB8\",\n" +
            "            \"headPic\": \"http://static.guojiang.tv/app/upload/2017_10_01/904c5074531dd0611fdf60d01791d5a1.jpg\",\n" +
            "            \"isPlaying\": true,\n" +
            "            \"onlineNum\": 2209,\n" +
            "            \"fansNum\": 854,\n" +
            "            \"announcement\": \"欢迎来看我的直播哦\",\n" +
            "            \"moderatorLevel\": 14,\n" +
            "            \"verified\": false,\n" +
            "            \"verifyInfo\": \"\",\n" +
            "            \"videoPlayUrl\": \"rtmp://9180.liveplay.myqcloud.com/live/9180_894446\",\n" +
            "            \"topics\": [],\n" +
            "            \"weight\": 2100000043,\n" +
            "            \"timeZoneHotWeight\": 1000000000,\n" +
            "            \"city\": \"武汉市\",\n" +
            "            \"tags\": [],\n" +
            "            \"addTime\": \"2017-09-11 16:11:21\",\n" +
            "            \"contentPackageId\": 2,\n" +
            "            \"sHeight\": \"1280\",\n" +
            "            \"sWidth\": \"720\",\n" +
            "            \"id\": 894446,\n" +
            "            \"doMission\": true\n" +
            "        }\n" +
            "    ]\n" +
            "}";

}
