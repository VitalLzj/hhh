package com.student.aynu.entity;

/**
 * Created by lzj on 2017/1/10 0010.
 * 邮箱：976623696@qq.com
 */
public class User_Security {

    private int code;
    private String message;

    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String usersecurity1;
        private String usersecurity2;
        private String usersecurity3;

        public String getUsersecurity1() {
            return usersecurity1;
        }

        public void setUsersecurity1(String usersecurity1) {
            this.usersecurity1 = usersecurity1;
        }

        public String getUsersecurity2() {
            return usersecurity2;
        }

        public void setUsersecurity2(String usersecurity2) {
            this.usersecurity2 = usersecurity2;
        }

        public String getUsersecurity3() {
            return usersecurity3;
        }

        public void setUsersecurity3(String usersecurity3) {
            this.usersecurity3 = usersecurity3;
        }
    }
}
