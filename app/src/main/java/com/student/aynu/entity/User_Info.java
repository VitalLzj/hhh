package com.student.aynu.entity;

import java.util.List;

/**
 * Created by lzj on 2017/1/9 0009.
 * 邮箱：976623696@qq.com
 */
public class User_Info {

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
        private String userid;
        private String username;
        private String userpwd;
        private String usersex;
        private String userhead;
        private String usernike;
        private String usersecurity1;
        private String usersecurity2;
        private String usersecurity3;


        public String getUsersecurity3() {
            return usersecurity3;
        }

        public void setUsersecurity3(String usersecurity3) {
            this.usersecurity3 = usersecurity3;
        }

        public String getUsersecurity2() {
            return usersecurity2;
        }

        public void setUsersecurity2(String usersecurity2) {
            this.usersecurity2 = usersecurity2;
        }

        public String getUsersecurity1() {
            return usersecurity1;
        }

        public void setUsersecurity1(String usersecurity1) {
            this.usersecurity1 = usersecurity1;
        }

        public String getUsernike() {
            return usernike;
        }

        public void setUsernike(String usernike) {
            this.usernike = usernike;
        }

        public String getUserhead() {
            return userhead;
        }

        public void setUserhead(String userhead) {
            this.userhead = userhead;
        }

        public String getUsersex() {
            return usersex;
        }

        public void setUsersex(String usersex) {
            this.usersex = usersex;
        }

        public String getUserpwd() {
            return userpwd;
        }

        public void setUserpwd(String userpwd) {
            this.userpwd = userpwd;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

    }
}
