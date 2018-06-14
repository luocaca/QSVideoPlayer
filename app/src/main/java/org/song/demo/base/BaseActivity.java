package org.song.demo.base;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * 基础 activity  执行一些 统一 代码  如 状态栏 ..
 */

@SuppressLint("Registered")
public abstract class BaseActivity extends SwipeBackActivity {


    private ProgressDialog progressDialog;


    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setSwipeBackEnable(true);
        initStatusBar();


        initView();
        initData();
        initListener();


    }

    protected void initListener() {

    }

    public void initData() {

    }

    protected void initStatusBar() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(bindLayoutID());


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


    }


    public abstract void initView();


    public abstract int bindLayoutID();


    @Override
    protected void onDestroy() {


        super.onDestroy();
    }


}
