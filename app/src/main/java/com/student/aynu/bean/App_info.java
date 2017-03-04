package com.student.aynu.bean;

/**
 * Created by lzj on 2017/1/11 0011.
 * 邮箱：976623696@qq.com
 */
public class App_info {

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
        private String aid;
        private String aintroduce;
        private String aphone;
        private String aqq;
        private String aemail;

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getAintroduce() {
            return aintroduce;
        }

        public void setAintroduce(String aintroduce) {
            this.aintroduce = aintroduce;
        }

        public String getAphone() {
            return aphone;
        }

        public void setAphone(String aphone) {
            this.aphone = aphone;
        }

        public String getAqq() {
            return aqq;
        }

        public void setAqq(String aqq) {
            this.aqq = aqq;
        }

        public String getAemail() {
            return aemail;
        }

        public void setAemail(String aemail) {
            this.aemail = aemail;
        }
    }
}
