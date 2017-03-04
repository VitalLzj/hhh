package com.student.aynu.bean;

import java.util.List;

/**
 * Created by lzj on 2017/3/1 0001.
 * 邮箱：976623696@qq.com
 * 收藏列表
 */
public class Book_Sc {

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
        private String bsid;
        private String userid;
        private String bid;
        private String b_name;
        private String b_thumb;

        public String getBsid() {
            return bsid;
        }

        public void setBsid(String bsid) {
            this.bsid = bsid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getB_name() {
            return b_name;
        }

        public void setB_name(String b_name) {
            this.b_name = b_name;
        }

        public String getB_thumb() {
            return b_thumb;
        }

        public void setB_thumb(String b_thumb) {
            this.b_thumb = b_thumb;
        }
    }
}
