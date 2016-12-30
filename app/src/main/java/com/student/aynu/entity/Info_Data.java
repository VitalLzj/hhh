package com.student.aynu.entity;

import java.util.List;

/**
 * Created by lzj on 2016/12/26 0026.
 * 邮箱：976623696@qq.com
 */
public class Info_Data {


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
        private String yx_id;
        private String yx_name;
        private String yx_url;

        private List<Info_Array> news;

        public String getYx_id() {
            return yx_id;
        }

        public void setYx_id(String yx_id) {
            this.yx_id = yx_id;
        }

        public String getYx_name() {
            return yx_name;
        }

        public void setYx_name(String yx_name) {
            this.yx_name = yx_name;
        }

        public String getYx_url() {
            return yx_url;
        }

        public void setYx_url(String yx_url) {
            this.yx_url = yx_url;
        }

        public List<Info_Array> getNews() {
            return news;
        }

        public void setNews(List<Info_Array> news) {
            this.news = news;
        }


    }
}
