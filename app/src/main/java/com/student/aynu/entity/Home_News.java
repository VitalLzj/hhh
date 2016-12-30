package com.student.aynu.entity;

import java.util.List;

/**
 * Created by lzj on 2016/12/23 0023.
 * 邮箱：976623696@qq.com
 */
public class Home_News {



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
        private String news_id;
        private String news_title;
        private String news_info;
        private String news_url;
        private String news_time;
        private String news_thumbnail;
        private String title_id;

        public String getNews_id() {
            return news_id;
        }

        public void setNews_id(String news_id) {
            this.news_id = news_id;
        }

        public String getNews_title() {
            return news_title;
        }

        public void setNews_title(String news_title) {
            this.news_title = news_title;
        }

        public String getNews_info() {
            return news_info;
        }

        public void setNews_info(String news_info) {
            this.news_info = news_info;
        }

        public String getNews_url() {
            return news_url;
        }

        public void setNews_url(String news_url) {
            this.news_url = news_url;
        }

        public String getNews_time() {
            return news_time;
        }

        public void setNews_time(String news_time) {
            this.news_time = news_time;
        }

        public String getNews_thumbnail() {
            return news_thumbnail;
        }

        public void setNews_thumbnail(String news_thumbnail) {
            this.news_thumbnail = news_thumbnail;
        }

        public String getTitle_id() {
            return title_id;
        }

        public void setTitle_id(String title_id) {
            this.title_id = title_id;
        }
    }
}
