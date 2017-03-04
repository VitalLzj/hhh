package com.student.aynu.bean;

import java.util.List;

/**
 * Created by lzj on 2017/1/12 0012.
 * 邮箱：976623696@qq.com
 */
public class Yb_entity {

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
        private String yb_id;
        private String yb_name;
        private String yb_office;
        private String yb_phone;

        public String getYb_id() {
            return yb_id;
        }

        public void setYb_id(String yb_id) {
            this.yb_id = yb_id;
        }

        public String getYb_name() {
            return yb_name;
        }

        public void setYb_name(String yb_name) {
            this.yb_name = yb_name;
        }

        public String getYb_office() {
            return yb_office;
        }

        public void setYb_office(String yb_office) {
            this.yb_office = yb_office;
        }

        public String getYb_phone() {
            return yb_phone;
        }

        public void setYb_phone(String yb_phone) {
            this.yb_phone = yb_phone;
        }
    }
}
