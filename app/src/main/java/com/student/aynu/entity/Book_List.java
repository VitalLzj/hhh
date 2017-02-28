package com.student.aynu.entity;

import java.util.List;

/**
 * Created by lzj on 2017/2/28 0028.
 * 邮箱：976623696@qq.com
 */
public class Book_List {
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
        private String bid;
        private String bname;
        private String bthumb;

        private List<BAuthorBean> bAuthor;

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getBname() {
            return bname;
        }

        public void setBname(String bname) {
            this.bname = bname;
        }

        public List<BAuthorBean> getBAuthor() {
            return bAuthor;
        }

        public void setBAuthor(List<BAuthorBean> bAuthor) {
            this.bAuthor = bAuthor;
        }

        public String getBthumb() {
            return bthumb;
        }

        public void setBthumb(String bthumb) {
            this.bthumb = bthumb;
        }

        public static class BAuthorBean {
            private String baathor;

            public String getBaathor() {
                return baathor;
            }

            public void setBaathor(String baathor) {
                this.baathor = baathor;
            }
        }
    }
}
