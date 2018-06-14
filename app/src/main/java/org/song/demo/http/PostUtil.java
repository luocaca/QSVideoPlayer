package org.song.demo.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.song.demo.CallBack.AjaxCallBack;
import org.song.demo.CallBack.Subscription;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import static org.song.demo.MainActivity.COOKIE;

/**
 *
 */

public class PostUtil {

    public static ConnectInterceptor interceptor;


    public static String post(String actionUrl, Map<String, String> headParams,
                              Map<String, String> params,
                              Map<String, File> files) throws IOException {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";

        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(30 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                + ";boundary=" + BOUNDARY);
        if (headParams != null) {
            for (String key : headParams.keySet()) {
                conn.setRequestProperty(key, headParams.get(key));
            }
        }
        StringBuilder sb = new StringBuilder();

        if (params != null) {
            // 首先组拼文本类型的参数
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);
                sb.append("Content-Disposition: form-data; name=\""
                        + entry.getKey() + "\"" + LINEND);
                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                sb.append(LINEND);
                sb.append(entry.getValue());
                sb.append(LINEND);
            }

        }

        DataOutputStream outStream = new DataOutputStream(
                conn.getOutputStream());
        if (!TextUtils.isEmpty(sb.toString())) {
            outStream.write(sb.toString().getBytes());
        }


        // 发送文件数据
        if (files != null)
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                        + file.getKey() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());

                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                    Log.i("HttpUtil", "写入中...");
                }

                is.close();
                outStream.write(LINEND.getBytes());
            }

        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();
        Log.i("HttpUtil", "conn.getContentLength():" + conn.getContentLength());

        // 得到响应码
        int res = conn.getResponseCode();
        InputStream in = conn.getInputStream();
        if (res == 200) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }

//          int ch;
//          StringBuilder sb2 = new StringBuilder();
//          while ((ch = in.read()) != -1) {
//              sb2.append((char) ch);
//          }
            if (interceptor != null) interceptor.interceptorEnd(conn);

            outStream.close();
            conn.disconnect();
            return buffer.toString();
        }
        outStream.close();

        conn.disconnect();
        return in.toString();

    }


    public static String PrintCookie(HttpURLConnection conn, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("QSVideoPlayer", Context.MODE_PRIVATE);


        String sessionId = "";
        String cookieVal = "";
        String key = null;
        //取cookie
        for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
            if (key.equalsIgnoreCase("set-cookie")) {
                cookieVal = conn.getHeaderField(i);
                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                sessionId = sessionId + cookieVal + ";";
            }
        }


        if (PostUtil.interceptor != null) {
            PostUtil.interceptor = null;
        }

        sharedPreferences.edit().putString(COOKIE, sessionId).apply();


        return sessionId;

    }


    public static Subscription asyncGet(String actionUrl, AjaxCallBack ajaxCallBack) {
        AsyncTask asyncTask = new AsyncGetTask(ajaxCallBack).execute(actionUrl);

        return (Subscription) asyncTask;

    }


    private static class AsyncGetTask extends AsyncTask<String, Void, String> implements Subscription {
        AjaxCallBack ajaxCallBack;

        @Override
        protected String doInBackground(String... strings) {

            // 异步 加载 json

            String result = "";
            try {
                result = doAsyncGet(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            return result;
        }

        AsyncGetTask(AjaxCallBack ajaxCallBack) {
            super();
            this.ajaxCallBack = ajaxCallBack;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ajaxCallBack.onSucceed(s);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            ajaxCallBack.onCancle();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        public void unSubscribe() {

            this.cancel(true);
        }


        String doAsyncGet(String url) throws MalformedURLException {

            StringBuilder json = new StringBuilder();
            try {
                URL oracle = new URL(url);
                URLConnection yc = oracle.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        yc.getInputStream(), "utf-8"));//防止乱码
                String inputLine = null;
                while ((inputLine = in.readLine()) != null) {
                    json.append(inputLine);
                }
                in.close();
            } catch (MalformedURLException e) {

            } catch (IOException e) {
            }
            return json.toString();
        }

    }


}
