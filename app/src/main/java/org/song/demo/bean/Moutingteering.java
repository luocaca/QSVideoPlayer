package org.song.demo.bean;

/**
 * Created by Administrator on 2018/6/25 0025.
 */

public class Moutingteering {


    /**
     * code : 1
     * msg : 查询成功
     * data : {"id":2,"imagesBanner":"[{\"url\": \"http://image.hmeg.cn/upload/image/201805/252aefefe9d2409d8b1c99e4a0b983d3.jpeg\"}, {\"url\": \"http://image.hmeg.cn/upload/image/201805/7d2a44cda36c441fab629aa086284b72.jpeg\"}, {\"url\": \"http://image.hmeg.cn/upload/image/201805/47a33c4413794f2d99e9914c5f81755c.jpeg\"}]","imagesMore":"[{\"url\": \"http://image.hmeg.cn/upload/image/201805/252aefefe9d2409d8b1c99e4a0b983d3.jpeg\"}, {\"url\": \"http://image.hmeg.cn/upload/image/201805/7d2a44cda36c441fab629aa086284b72.jpeg\"}, {\"url\": \"http://image.hmeg.cn/upload/image/201805/47a33c4413794f2d99e9914c5f81755c.jpeg\"}]","createDate":"2018-03-19","title":"摘大杨梅餐厅海鲜大餐+镇好觉看景游泳一天游","desc":"报名客获得100积分每人\\n微信分享可获得1000积分，达成交易还可获得2000积分/人，上不封顶报名客获得100积分每人\\n微信分享可获得1000积分，达成交易还可获得2000积分/人","userJoin":"1/45","leaderName":"二傻么","specialOffers":"优惠活动详情，请查看"}
     */

    public String code;
    public String msg;
    public DataBean data;
    /**
     * url : http://image.hmeg.cn/upload/image/201805/252aefefe9d2409d8b1c99e4a0b983d3.jpeg
     */

    public String url;

    public static class DataBean {
        /**
         * id : 2
         * imagesBanner : [{"url": "http://image.hmeg.cn/upload/image/201805/252aefefe9d2409d8b1c99e4a0b983d3.jpeg"}, {"url": "http://image.hmeg.cn/upload/image/201805/7d2a44cda36c441fab629aa086284b72.jpeg"}, {"url": "http://image.hmeg.cn/upload/image/201805/47a33c4413794f2d99e9914c5f81755c.jpeg"}]
         * imagesMore : [{"url": "http://image.hmeg.cn/upload/image/201805/252aefefe9d2409d8b1c99e4a0b983d3.jpeg"}, {"url": "http://image.hmeg.cn/upload/image/201805/7d2a44cda36c441fab629aa086284b72.jpeg"}, {"url": "http://image.hmeg.cn/upload/image/201805/47a33c4413794f2d99e9914c5f81755c.jpeg"}]
         * createDate : 2018-03-19
         * title : 摘大杨梅餐厅海鲜大餐+镇好觉看景游泳一天游
         * desc : 报名客获得100积分每人\n微信分享可获得1000积分，达成交易还可获得2000积分/人，上不封顶报名客获得100积分每人\n微信分享可获得1000积分，达成交易还可获得2000积分/人
         * userJoin : 1/45
         * leaderName : 二傻么
         * specialOffers : 优惠活动详情，请查看
         */

        public int id;
        public String imagesBanner;
        public String imagesMore;
        public String createDate;
        public String title;
        public String desc;
        public String userJoin;
        public String leaderName;
        public String specialOffers;
    }


}
