package org.song.demo.bean;

import java.util.List;

/**
 * 主播
 */

public class ZhuBoGsonBean {


    public List<ZhuboBean> zhubo;

    public static class ZhuboBean {
        /**
         * address : rtmp://down.rtmp.qxiu.com/live/442385
         * img : http://qiqi-resource.qxiu.com/op/2018/05/29/27400c7db50e472a903aa46c814daf51.jpg
         * title : 蜜蜜贺我Hero子爵
         */

        public String address;
        public String img;
        public String title;
    }
}
