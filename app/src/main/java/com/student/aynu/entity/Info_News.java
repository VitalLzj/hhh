package com.student.aynu.entity;

import java.util.List;

/**
 * Created by lzj on 2016/12/26 0026.
 * 邮箱：976623696@qq.com
 */
public class Info_News {

    private int code;
    private String message;

    private List<Info_Array> data;

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

    public List<Info_Array> getData() {
        return data;
    }

    public void setData(List<Info_Array> data) {
        this.data = data;
    }

}
