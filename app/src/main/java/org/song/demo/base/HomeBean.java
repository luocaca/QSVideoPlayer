package org.song.demo.base;

import java.util.List;

/**
 * 平台 首页 bean
 */

public class HomeBean {


    public List<PingtaiBean> pingtai;

    public static class PingtaiBean {
        /**
         * address : jsonxingguang.txt
         * xinimg : http://ww1.sinaimg.cn/large/87c01ec7gy1fqi47x1heoj2020020748.jpg
         * Number : 22
         * title : 星光
         */

        public String address;
        public String xinimg;
        public String Number;
        public String title;
    }
}
