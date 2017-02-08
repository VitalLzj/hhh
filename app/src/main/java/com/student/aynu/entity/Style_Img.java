package com.student.aynu.entity;

import java.util.List;

/**
 * Created by lzj on 2017/2/8 0008.
 * 邮箱：976623696@qq.com
 * 校园图片
 */
public class Style_Img {

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
        private String style_img_id;
        private String style_img_url;

        public String getStyle_img_id() {
            return style_img_id;
        }

        public void setStyle_img_id(String style_img_id) {
            this.style_img_id = style_img_id;
        }

        public String getStyle_img_url() {
            return style_img_url;
        }

        public void setStyle_img_url(String style_img_url) {
            this.style_img_url = style_img_url;
        }
    }
}
