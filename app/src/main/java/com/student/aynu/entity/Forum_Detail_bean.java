package com.student.aynu.entity;

import java.util.List;

/**
 * Created by lzj on 2017/2/22 0022.
 * 邮箱：976623696@qq.com
 * 帖子详情
 */
public class Forum_Detail_bean {

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
        private String fid;
        private String userid;
        private String fcontent;
        private String fping_num;
        private String fzan_num;
        private String ftime;
        private String userAccount;
        private String userName;
        private String userHead;


        private List<ForumImgsBean> forumImgs;

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

        public String getFcontent() {
            return fcontent;
        }

        public void setFcontent(String fcontent) {
            this.fcontent = fcontent;
        }

        public String getFping_num() {
            return fping_num;
        }

        public void setFping_num(String fping_num) {
            this.fping_num = fping_num;
        }

        public String getFzan_num() {
            return fzan_num;
        }

        public void setFzan_num(String fzan_num) {
            this.fzan_num = fzan_num;
        }

        public String getFtime() {
            return ftime;
        }

        public void setFtime(String ftime) {
            this.ftime = ftime;
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

        public List<ForumImgsBean> getForumImgs() {
            return forumImgs;
        }

        public void setForumImgs(List<ForumImgsBean> forumImgs) {
            this.forumImgs = forumImgs;
        }

        public static class ForumImgsBean {
            private String fimgUrl;

            public String getFimgUrl() {
                return fimgUrl;
            }

            public void setFimgUrl(String fimgUrl) {
                this.fimgUrl = fimgUrl;
            }
        }
    }
}
