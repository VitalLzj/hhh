package com.student.aynu.bean;

import java.util.List;

/**
 * Created by lzj on 2017/2/27 0027.
 * 邮箱：976623696@qq.com
 */
public class My_Forum {

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
    }
}
