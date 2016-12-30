package com.student.aynu.entity;

import java.util.List;

/**
 * Created by lzj on 2016/12/23 0023.
 * 邮箱：976623696@qq.com
 */
public class Home_Title {


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
        private String title_id;
        private String title_name;

        public String getTitle_id() {
            return title_id;
        }

        public void setTitle_id(String title_id) {
            this.title_id = title_id;
        }

        public String getTitle_name() {
            return title_name;
        }

        public void setTitle_name(String title_name) {
            this.title_name = title_name;
        }
    }
}
