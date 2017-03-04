package com.student.aynu.bean;

import java.util.List;

/**
 * Created by lzj on 2017/3/3 0003.
 * 邮箱：976623696@qq.com
 * 学期选择器
 */
public class Student {

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
        private String xnid;
        private String xntext;

        private List<XxxxBean> xxxx;

        public String getXnid() {
            return xnid;
        }

        public void setXnid(String xnid) {
            this.xnid = xnid;
        }

        public String getXntext() {
            return xntext;
        }

        public void setXntext(String xntext) {
            this.xntext = xntext;
        }

        public List<XxxxBean> getXxxx() {
            return xxxx;
        }

        public void setXxxx(List<XxxxBean> xxxx) {
            this.xxxx = xxxx;
        }

        public static class XxxxBean {
            private String xq;

            public String getXq() {
                return xq;
            }

            public void setXq(String xq) {
                this.xq = xq;
            }
        }
    }
}
