package com.student.aynu.bean;

import java.util.List;

/**
 * Created by lzj on 2017/2/24 0024.
 * 邮箱：976623696@qq.com
 * 论坛评论
 */
public class Forum_Pl {

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
        private String userid;
        private String fplyid;
        private String userAccount;
        private String userName;
        private String userHead;
        private String lc;
        private String y_userAccount;
        private String y_userName;
        private String y_fcontent;
        private String y_ftime;

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

        public String getUserAccount() {
            return userAccount;
        }

        public void setUserAccount(String userAccount) {
            this.userAccount = userAccount;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserHead() {
            return userHead;
        }

        public void setUserHead(String userHead) {
            this.userHead = userHead;
        }

        public String getLc() {
            return lc;
        }

        public void setLc(String lc) {
            this.lc = lc;
        }

        public String getY_userAccount() {
            return y_userAccount;
        }

        public void setY_userAccount(String y_userAccount) {
            this.y_userAccount = y_userAccount;
        }

        public String getY_userName() {
            return y_userName;
        }

        public void setY_userName(String y_userName) {
            this.y_userName = y_userName;
        }

        public String getY_fcontent() {
            return y_fcontent;
        }

        public void setY_fcontent(String y_fcontent) {
            this.y_fcontent = y_fcontent;
        }

        public String getY_ftime() {
            return y_ftime;
        }

        public void setY_ftime(String y_ftime) {
            this.y_ftime = y_ftime;
        }
    }
}
