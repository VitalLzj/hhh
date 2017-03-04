package com.student.aynu.bean;

import java.util.List;

/**
 * Created by lzj on 2017/2/27 0027.
 * 邮箱：976623696@qq.com
 * 我的回复
 */
public class Forum_Reply {

    private int code;
    private String message;

    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String fplid;
        private String fplinfo;
        private String fpltime;
        private String fid;
        private String userid;
        private String fplyid;
        private String y_fContent;

        public String getFplid() {
            return fplid;
        }

        public void setFplid(String fplid) {
            this.fplid = fplid;
        }

        public String getFplinfo() {
            return fplinfo;
        }

        public void setFplinfo(String fplinfo) {
            this.fplinfo = fplinfo;
        }

        public String getFpltime() {
            return fpltime;
        }

        public void setFpltime(String fpltime) {
            this.fpltime = fpltime;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getFplyid() {
            return fplyid;
        }

        public void setFplyid(String fplyid) {
            this.fplyid = fplyid;
        }

        public String getY_fContent() {
            return y_fContent;
        }

        public void setY_fContent(String y_fContent) {
            this.y_fContent = y_fContent;
        }
    }
}
