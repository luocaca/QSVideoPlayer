package org.song.demo.http;

import java.net.HttpURLConnection;

/**
 * Created by Administrator on 2017/11/24 0024.
 */

public interface ConnectInterceptor {

    void interceptorEnd(HttpURLConnection connection);

    void interceptorStart(HttpURLConnection connection);

}
