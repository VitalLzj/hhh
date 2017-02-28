package com.student.aynu.entity;

import java.util.List;

/**
 * Created by lzj on 2017/2/28 0028.
 * 邮箱：976623696@qq.com
 * 图书检索banner
 */
public class Book_Banner {

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
        private String bbid;
        private String bburl;

        public String getBbid() {
            return bbid;
        }

        public void setBbid(String bbid) {
            this.bbid = bbid;
        }

        public String getBburl() {
            return bburl;
        }

        public void setBburl(String bburl) {
            this.bburl = bburl;
        }
    }
}
