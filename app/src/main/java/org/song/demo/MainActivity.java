package org.song.demo;

import android.accounts.NetworkErrorException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.song.demo.http.ConnectInterceptor;
import org.song.demo.http.JsonUtil;
import org.song.demo.http.LogUtil;
import org.song.demo.http.PostUtil;
import org.song.demo.ui.ViewPagerActivity;
import org.song.videoplayer.DemoQSVideoView;
import org.song.videoplayer.IVideoPlayer;
import org.song.videoplayer.PlayListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class MainActivity extends SwipeBackActivity {

    public static String COOKIE = "cookie";

    public static int page = 0;
    public static int state = 1;
    DemoQSVideoView demoVideoView;


    String mp4 = "http://videos.kpie.com.cn/videos/20170526/037DCE54-EECE-4520-AA92-E4002B1F29B0.mp4";
    String m3u8 = "http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8";


    String m3u8s[] = {
            "http://9180.liveplay.myqcloud.com/live/9180_674395.m3u8",
            "http://9180.liveplay.myqcloud.com/live/9180_883859.m3u8",
            "http://9180.liveplay.myqcloud.com/live/9180_900846.m3u8",
            "http://9180.liveplay.myqcloud.com/live/9180_899214.m3u8",
            "http://9180.liveplay.myqcloud.com/live/9180_608825.m3u8",
            "http://9180.liveplay.myqcloud.com/live/9180_797736.m3u8",
            "http://9180.liveplay.myqcloud.com/live/9180_678313.m3u8",
            "http://9180.liveplay.myqcloud.com/live/9180_903701.m3u8",
            "http://9180.liveplay.myqcloud.com/live/9180_204314.m3u8",
            "http://9180.liveplay.myqcloud.com/live/9180_353764.m3u8",
            "http://9180.liveplay.myqcloud.com/live/9180_737281.m3u8",
    };


    int roomId = 674395;


    String m3u8_1 = "http://27.152.181.221/v53915232/playlist.m3u8";

    String url;
    int media;
    private String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setSwipeBackEnable(true);

//        requestLogin("version=3.5.0&platform=android&packageId=3&channel=and-laosiji.cpd-3&deviceName=HUAWEI+FRD-AL00&androidVersion=7.0");


//        requestLogin("version=3.5.0&platform=android&packageId=3&channel=and-laosiji.cpd-3&deviceName=HUAWEI+FRD-AL00&androidVersion=7.0&username=17074990702&password=123456aa&remember=true");
//        requestLogin("");

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_url).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                changeUrl();
                return true;
            }
        });
        demoVideoView = (DemoQSVideoView) findViewById(R.id.qs);
        demoVideoView.getCoverImageView().setImageResource(R.mipmap.cover);
        demoVideoView.setPlayListener(new PlayListener() {
            @Override
            public void onStatus(int status) {//播放状态
                if (status == IVideoPlayer.STATE_AUTO_COMPLETE)
                    demoVideoView.quitWindowFullscreen();//播放完成退出全屏
            }

            @Override//全屏/普通
            public void onMode(int mode) {

            }

            @Override
            public void onEvent(int what, Integer... extra) {

            }

        });
//        play(mp4, 0);


    }


    public void requestNet() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //执行登录代码  start
                try {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("username", "17074990702");
                    map.put("password", "gcSZtaXMPUUrDs9YBXzroQaph4mhIB/rEmkwlwNQHSUhOlRxBkH65hVjWje41Vy9FDeawfWwtXfMbY/suBAPPkaGj+3JPk+k7OsJRwzDhOuEKH2hOvYC1Z3ihqDKIElv4gOzuAjddHMH6tpZPOPcq6qIJKAPzFwzfodfKg7Wv+s=");
                    map.put("remember", "true");
                    cookie = MainActivity.this.getSharedPreferences("QSVideoPlayer", Context.MODE_PRIVATE).getString(COOKIE, "");
                    if (TextUtils.isEmpty(cookie)) {
                        PostUtil.interceptor = new ConnectInterceptor() {
                            @Override
                            public void interceptorEnd(HttpURLConnection connection) {
                                //结束可以用来保存cookie
                                String sessionId = cookie = PostUtil.PrintCookie(connection, MainActivity.this);
                                Log.i(TAG, "interceptorEnd: sessionId \n" + sessionId);
                            }

                            @Override
                            public void interceptorStart(HttpURLConnection connection) {

                            }
                        };

                        String json = PostUtil.post(host + "?" + "version=3.5.0&platform=android&packageId=3&channel=and-laosiji.cpd-3&deviceName=HUAWEI+FRD-AL00&androidVersion=7.0", null, map, null);

                        LogUtil.i(TAG, "run: " + JsonUtil.formatJson(json));

                    }
                    //执行登录代码  end
                    Log.i(TAG, "run: " + cookie);


                    Map<String, String> map1 = new HashMap<String, String>();
//                    map1.put("Cookie", cookie);

//                    uid=2316539;PHPSESSID=v6sj09n48iss9b9ka3jsj13hv1;
//                    cookie = "PHPSESSID=v6sj09n48iss9b9ka3jsj13hv1;uid=2316539;";
                    map1.put("Cookie", cookie);

                    Map<String, String> params = new HashMap<String, String>();
                    Log.i(TAG, "params: page" + page);
//                    params.put("haha", "haha");
//                    params.put("page",  "3");
//                    params.put("status", "1");
//                    params.put("version", "3.5.0");
//                    params.put("platform", "android");
//                    params.put("packageId", "3");
                    //final String hst = "http://123.207.176.15/room/getRooms";
//                    final String paramsStr = "page=0&status=1&version=3.5.0&platform=android&packageId=3";
//                    requestRoomMsg();

                    String str = PostUtil.post("http://123.207.176.15/room/getRooms?page=" + page + "&status=" + state + "&version=3.5.0&platform=android&packageId=3", map1, null, null);
//                  String str = PostUtil.post("http://123.207.176.15/room/getRooms?", map1, params, null);


                    ListVideoActivity.json = str;
                    LogUtil.i(TAG, "run: " + JsonUtil.formatJson(str));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    ;


    private static final String TAG = "MainActivity";

    private void play(String url, int media) {
        Log.e("=====url:", url);
        demoVideoView.release();
        demoVideoView.setiMediaControl(media);
        demoVideoView.setUp(url, "这是一一一一一一一一一个标题");
        //qsVideoView.seekTo(12300);
        demoVideoView.play();

        this.url = url;
        this.media = media;

        //qsVideoView.enterFullMode =1;
    }


    public void 系统硬解(View v) {
        play(url, 0);
        setTitle("系统硬解");

    }

    public void ijk_ffmepg解码(View v) {
        play(url, 1);
        setTitle("ijk_ffmepg解码");

    }

    public void exo解码(View v) {
        play(url, 2);
        setTitle("exo解码");

    }

    public void ijk_exo解码(View v) {
        play(url, 3);
        setTitle("ijk_exo解码");

    }

    public void 网络视频(View v) {
        play(mp4, media);

    }

    public void 视频列表(View v) {
        page++;
        Log.i(TAG, "视频列表: page=" + page);
        startActivity(new Intent(this, ListVideoActivity.class));
    }


    public void 视频列表1(View v) {
//        page++;
//        Log.i(TAG, "视频列表: page=" + page);
//        startActivity(new Intent(this, RecyVideoActivity.class));

        ViewPagerActivity.start(this);
    }


    public void m3u8直播(View v) {
        play(getRoomId(), media);

    }

    int index = 0;

    public String getRoomId() {
        EditText editText = (EditText) findViewById(R.id.et_room_id);
        String romid = editText.getText().toString();
        Log.i("getRoomId_first", "getRoomId: " + romid);
        if (TextUtils.isEmpty(romid)) {

            if (index > m3u8s.length - 1) {
                index = 0;
            }
            return m3u8s[index++];
        }
//        romid

        editText.setText((Integer.parseInt(romid) + 1) + "");

        Log.i("getRoomId_second", "getRoomId: " + romid);
        return "http://9180.liveplay.myqcloud.com/live/9180_" + romid + ".m3u8";
    }

    String[] arr = {"适应", "填充", "原尺寸", "拉伸", "16:9", "4:3"};
    int mode;

    public void 缩放模式(View v) {
        demoVideoView.setAspectRatio(++mode > 5 ? mode = 0 : mode);
        ((Button) v).setText(arr[mode]);
    }


    public void 销毁(View v) {
        demoVideoView.release();
    }


    EditText editText;

    public void changeUrl() {
        editText = new EditText(this);
        new AlertDialog.Builder(this).setView(editText).setTitle("网络视频地址").setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mp4 = editText.getText().toString();
                play(mp4, media);
            }
        }).setPositiveButton("本地视频", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
                //startActivityForResult(new Intent(getApplication(), ExSelectDialog.class), 1000);
            }
        }).create().show();

    }

    @Override
    public void onBackPressed() {
        if (demoVideoView.onBackPressed())
            return;
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 & resultCode == RESULT_OK) {
            mp4 = "file://" + data.getStringExtra(ExSelectDialog.KEY_RESULT);
            play(mp4, media);
        }

        if (requestCode == 1 & resultCode == RESULT_OK) {
            mp4 = data.getData().toString();
            Toast.makeText(this, mp4, Toast.LENGTH_LONG).show();
            play(mp4, media);
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText("text", mp4));
        }
    }


    //以下生命周期控制
    @Override
    public void onResume() {
        super.onResume();
        requestNet();
        if (flag)
            demoVideoView.play();
        handler.removeCallbacks(runnable);
        if (position > 0) {
            demoVideoView.seekTo(position);
            position = 0;
        }


        demoVideoView.enterFullMode = 3;

    }

    boolean flag;//记录退出时播放状态 回来的时候继续播放
    int position;//记录销毁时的进度 回来继续盖进度播放

    @Override
    public void onPause() {
        super.onPause();//暂停
        flag = demoVideoView.isPlaying();
        demoVideoView.pause();
    }


    @Override
    public void onStop() {
        super.onStop();//不马上销毁 延时10秒
        handler.postDelayed(runnable, 1000 * 10);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();//销毁
        demoVideoView.release();
    }


    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (demoVideoView.getCurrentState() != IVideoPlayer.STATE_AUTO_COMPLETE)
                position = demoVideoView.getPosition();
            demoVideoView.release();
        }
    };


    String host = "http://123.207.176.15/user/login";
//    String host = "http://www.luocaca.com/hello-ssm";

    //


    //http://123.207.176.15/room/getRooms?page=0&status=1&version=3.5.0&platform=android&packageId=3
//    page:0
//    status:1
//    version:3.5.0
//    platform:android
//    packageId:3
//channel:and-laosiji.cpd-3
//deviceName:HUAWEI+FRD-AL00
//androidVersion:7.0
//uid:12136547


    private void requestRoomMsg() {

        final String hst = "http://123.207.176.15/room/getRooms";
        final String paramsStr = "page=0&status=1&version=3.5.0&platform=android&packageId=3";

        Log.i(TAG, "requestLogin: 开启线程" + hst + paramsStr);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "requestLogin: 请求登录");

                HttpURLConnection conn = null;

                try {
                    URL mUrl = new URL(hst + "?" + paramsStr);
                    //调用 url 的openConnection（）方法。获取httpurlconn 对象
                    conn = (HttpURLConnection) mUrl.openConnection();


                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    conn.setDoOutput(true);//允许此方法，向服务器输出内容
//                    conn.setRequestProperty("Content-Type","multipart/form-data");
//                    conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                    conn.setRequestProperty("Accept-Charset", "utf-8");  //设置编码语言
//                    conn.setRequestProperty("X-Auth-Token", "token");  //设置请求的token
                    conn.setRequestProperty("Connection", "keep-alive");  //设置连接的状态
                    conn.setRequestProperty("Cookie", "uid=12136547; expires=Thu, 22-Nov-2018 05:36:06 GMT; path=/; domain=123.207.176.15");  //设置连接的状态

//                    conn.setRequestProperty("Transfer-Encoding", "chunked");//设置传输编码

//                    conn.setRequestProperty("Content-Type", "multipart/form-data"
//                            + ";boundary=" + "M-Cz8iiTY6DddlKc6gSBM7hk_8bycEe81d2N");


                    StringBuilder sb = new StringBuilder();

//                    String bod = "--M-Cz8iiTY6DddlKc6gSBM7hk_8bycEe81d2N\n" +
//                            "Content-Disposition: form-data; name=\"username\"\n" +
//                            "\n" +
//                            "17074990702\n" +
//                            "--M-Cz8iiTY6DddlKc6gSBM7hk_8bycEe81d2N\n" +
//                            "Content-Disposition: form-data; name=\"password\"\n" +
//                            "\n" +
//                            "VcdwP8I93jqG8BDpNlmhr98NjKKR9mPOVZgQyZ24Lu66Z+9gHaEE6BI3iZwIA7ysScU1lp+1Jbs2qVH7jgfFwkjlyfzbjimhc7wLNFEyZlo8InoeJpyS+gnZtEQN/X6emXM3bUAyr33X2qetrf7uTW6PMOatOHmwRt1QrVS8ebo=\n" +
//                            "--M-Cz8iiTY6DddlKc6gSBM7hk_8bycEe81d2N\n" +
//                            "Content-Disposition: form-data; name=\"remember\"\n" +
//                            "\n" +
//                            "true\n" +
//                            "--M-Cz8iiTY6DddlKc6gSBM7hk_8bycEe81d2N--";


                    //post 请求的参数
                    String data = paramsStr;
                    OutputStream out = conn.getOutputStream();
                    out.write(data.getBytes());
//                    out.write(bod.getBytes());
                    out.flush();
                    out.close();

                    int responseCode = conn.getResponseCode();// 调用此方法就不必再使用conn.connect()方法

                    if (responseCode == 200) {
                        InputStream is = conn.getInputStream();
                        String response = getStringFromInputStream(is);
                        Log.i(TAG, "run: " + response);
                    } else {
                        throw new NetworkErrorException("response status is " + responseCode);
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();// 关闭连接
                    }
                }

            }
        }).start();


    }


    private void requestLogin(final String content) {
//        String temp = "book/getAllBook";
        //线程中请求 登录

        Log.i(TAG, "requestLogin: 开启线程" + content);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "requestLogin: 请求登录");

                HttpURLConnection conn = null;
                PrintWriter pw = null;
                try {
                    URL mUrl = new URL(host + "?" + content);
                    //调用 url 的openConnection（）方法。获取httpurlconn 对象
                    conn = (HttpURLConnection) mUrl.openConnection();


                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(5000);
                    conn.setDoOutput(true);//允许此方法，向服务器输出内容
                    conn.setDoInput(true);
                    conn.setUseCaches(false); // 不允许使用缓存
                    conn.setRequestProperty("connection", "keep-alive");
                    conn.setRequestProperty("Charsert", "UTF-8");
//                    conn.setRequestProperty("Content-Type","multipart/form-data");
//                    conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

//                    conn.addRequestProperty("Accept-Charset", "Unicode");  //设置编码语言
//                    conn.setRequestProperty("X-Auth-Token", "token");  //设置请求的token
//                    conn.addRequestProperty("Connection", "application/x-www-form-urlencoded");  //设置连接的状态
//                    conn.addRequestProperty("Content-Type", "multipart/form-data");  //设置连接的状态
//                    conn.setRequestProperty("password", "VcdwP8I93jqG8BDpNlmhr98NjKKR9mPOVZgQyZ24Lu66Z+9gHaEE6BI3iZwIA7ysScU1lp+1Jbs2qVH7jgfFwkjlyfzbjimhc7wLNFEyZlo8InoeJpyS+gnZtEQN/X6emXM3bUAyr33X2qetrf7uTW6PMOatOHmwRt1QrVS8ebo=");  //设置连接的状态
//                    conn.setRequestProperty("remember", "true");  //设置连接的状态
//                    conn.addRequestProperty("Accept-Encoding", "gzip");//设置传输编码

//                    conn.setRequestProperty("Content-Type", "multipart/form-data"
//                            + ";boundary=" + "M-Cz8iiTY6DddlKc6gSBM7hk_8bycEe81d2N");


                    StringBuilder sb = new StringBuilder();

                    String bod = "--M-Cz8iiTY6DddlKc6gSBM7hk_8bycEe81d2N\n" +
                            "Content-Disposition: form-data; name=\"username\"\n" +
                            "\n" +
                            "17074990702\n" +
                            "--M-Cz8iiTY6DddlKc6gSBM7hk_8bycEe81d2N\n" +
                            "Content-Disposition: form-data; name=\"password\"\n" +
                            "\n" +
                            "VcdwP8I93jqG8BDpNlmhr98NjKKR9mPOVZgQyZ24Lu66Z+9gHaEE6BI3iZwIA7ysScU1lp+1Jbs2qVH7jgfFwkjlyfzbjimhc7wLNFEyZlo8InoeJpyS+gnZtEQN/X6emXM3bUAyr33X2qetrf7uTW6PMOatOHmwRt1QrVS8ebo=\n" +
                            "--M-Cz8iiTY6DddlKc6gSBM7hk_8bycEe81d2N\n" +
                            "Content-Disposition: form-data; name=\"remember\"\n" +
                            "\n" +
                            "true\n" +
                            "--M-Cz8iiTY6DddlKc6gSBM7hk_8bycEe81d2N--";


                    pw = new PrintWriter(conn.getOutputStream());
                    pw.print(bod);
                    pw.flush();

                    //post 请求的参数
                    String data = content;
                    OutputStream out = conn.getOutputStream();
//                    out.write(data.getBytes());
//                    out.write(bod.getBytes());
                    out.flush();
                    out.close();

                    int responseCode = conn.getResponseCode();// 调用此方法就不必再使用conn.connect()方法


                    if (responseCode == 200) {

                        InputStream is = conn.getInputStream();
                        String response = getStringFromInputStream(is);
                        Log.i(TAG, "run: " + response);
                    } else {

                        throw new NetworkErrorException("response status is " + responseCode);
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();// 关闭连接
                    }
                }

            }
        }).start();


    }


    private static String getStringFromInputStream(InputStream is) throws IOException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        //模板代码 必须熟练
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }

        is.close();
        String state = os.toString();//把流中的数据转换成字符串  ， 采用的编码是UtF-8 （模拟器默认编码）
        os.close();
        return state;


    }


}
