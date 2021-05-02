package org.song.demo.bean;

public class DispalayM3u8 {

    /**
     * retcode : 0
     * errmsg : 游客权限范围内免费播放
     * data : {"xxx_api_auth":"3233353735663939656232333463376666386237363337333738346533366130","isfavorite":0,"iszan":0,"playtask":{"playnum":5,"tasknum":0,"taskcoin":0,"logid":0},"httpurl":"https://xsp.yjzcrw.com/20190930/w3KrkncL/index.m3u8"}
     */

    private int retcode;
    private String errmsg;
    private DataBean data;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * xxx_api_auth : 3233353735663939656232333463376666386237363337333738346533366130
         * isfavorite : 0
         * iszan : 0
         * playtask : {"playnum":5,"tasknum":0,"taskcoin":0,"logid":0}
         * httpurl : https://xsp.yjzcrw.com/20190930/w3KrkncL/index.m3u8
         */

        private String xxx_api_auth;
        private int isfavorite;
        private int iszan;
        private PlaytaskBean playtask;
        private String httpurl;
        private String httpurl_preview;

        public String getXxx_api_auth() {
            return xxx_api_auth;
        }

        public void setXxx_api_auth(String xxx_api_auth) {
            this.xxx_api_auth = xxx_api_auth;
        }

        public int getIsfavorite() {
            return isfavorite;
        }

        public void setIsfavorite(int isfavorite) {
            this.isfavorite = isfavorite;
        }

        public int getIszan() {
            return iszan;
        }

        public void setIszan(int iszan) {
            this.iszan = iszan;
        }

        public PlaytaskBean getPlaytask() {
            return playtask;
        }

        public void setPlaytask(PlaytaskBean playtask) {
            this.playtask = playtask;
        }

        public String getHttpurl() {
            if (httpurl == null) {
                return httpurl_preview;
            }
            return httpurl;
        }

        public void setHttpurl(String httpurl) {
            this.httpurl = httpurl;
        }

        public static class PlaytaskBean {
            /**
             * playnum : 5
             * tasknum : 0
             * taskcoin : 0
             * logid : 0
             */

            private int playnum;
            private int tasknum;
            private int taskcoin;
            private int logid;

            public int getPlaynum() {
                return playnum;
            }

            public void setPlaynum(int playnum) {
                this.playnum = playnum;
            }

            public int getTasknum() {
                return tasknum;
            }

            public void setTasknum(int tasknum) {
                this.tasknum = tasknum;
            }

            public int getTaskcoin() {
                return taskcoin;
            }

            public void setTaskcoin(int taskcoin) {
                this.taskcoin = taskcoin;
            }

            public int getLogid() {
                return logid;
            }

            public void setLogid(int logid) {
                this.logid = logid;
            }
        }
    }
}
