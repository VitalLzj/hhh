package com.student.aynu.bean;

import java.util.List;

/**
 * Created by lzj on 2017/2/28 0028.
 * 邮箱：976623696@qq.com
 * 图书详情
 */
public class Book_Detail {

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
        private String bcbs;
        private String bintroduce;
        private String btotal;
        private String nnow;
        private String bplace;
        private String bthumb;
        private String byear;
        /**
         * baathor : 李波
         */

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

        public String getBcbs() {
            return bcbs;
        }

        public void setBcbs(String bcbs) {
            this.bcbs = bcbs;
        }

        public String getBintroduce() {
            return bintroduce;
        }

        public void setBintroduce(String bintroduce) {
            this.bintroduce = bintroduce;
        }

        public String getBtotal() {
            return btotal;
        }

        public void setBtotal(String btotal) {
            this.btotal = btotal;
        }

        public String getNnow() {
            return nnow;
        }

        public void setNnow(String nnow) {
            this.nnow = nnow;
        }

        public String getBplace() {
            return bplace;
        }

        public void setBplace(String bplace) {
            this.bplace = bplace;
        }

        public String getBthumb() {
            return bthumb;
        }

        public void setBthumb(String bthumb) {
            this.bthumb = bthumb;
        }

        public String getByear() {
            return byear;
        }

        public void setByear(String byear) {
            this.byear = byear;
        }

        public List<BAuthorBean> getBAuthor() {
            return bAuthor;
        }

        public void setBAuthor(List<BAuthorBean> bAuthor) {
            this.bAuthor = bAuthor;
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
