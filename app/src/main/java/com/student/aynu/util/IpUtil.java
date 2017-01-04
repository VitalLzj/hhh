package com.student.aynu.util;

/**
 * Created by lzj on 2016/12/22 0022.
 * 邮箱：976623696@qq.com
 */
public class IpUtil {
    private static String Ip_head = "http://www.zujianlee.cn/aynu/index.php/Home/";
    //获取banner
    public static String getHomeBanner = Ip_head + "Home/showBanners";
    //获取首页title
    public static String getHomeTitle = Ip_head + "Home/showHomeTitle";
    //获取首页news
    public static String getHomeNews = Ip_head + "Home/showHomeNews?";
    //获取资讯中的院系信息
    public static String getInfoData = Ip_head + "Info/showInfoData";
    //咨询中的新闻的刷新加载
    public static String getInfoNews = Ip_head + "Info/showInfoNews?";
    //用户注册
    //检测当前账号是否可用
    public static String checkUserIsExist = Ip_head + "User/checkIsRegister?";

}
