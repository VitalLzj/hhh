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
    public static String checkUserIsExist = Ip_head + "User/checkIsRegister";
    //进行注册
    public static String registerUser = Ip_head + "User/registerUser";
    //进行登录
    public static String loginUser = Ip_head + "User/login";
    //检测token是否过期
    public static String checkToken = Ip_head + "User/checkToken";
    //设置密保
    public static String setSecurity = Ip_head + "User/setSecurity";
    //获取用户信息
    public static String getUser_info = Ip_head + "User/getUserInfo";
    //修改用户头像
    public static String updateUser_Head = Ip_head + "User/changeUserHead";
    //修改用户名
    public static String updateUser_Name = Ip_head + "User/updateUserName";
    //修改性别
    public static String updateUser_Sex = Ip_head + "User/updateUserSex";
    //检测原密码
    public static String checkUser_Pwd = Ip_head + "User/checkUserPwd";
    //更改密码
    public static String updateUser_Pwd = Ip_head + "User/updateUserPwd";
    //获取当前密保
    public static String getUser_Security = Ip_head + "User/getUserSecurity";
    //设置新的密保
    public static String setNew_Security = Ip_head + "User/setNewSecurity";
    //密保验证
    public static String checkUser_Security = Ip_head + "User/checkSecurity";
    //设置新密码
    public static String setNew_Pwd = Ip_head + "User/setNewPwd";
    //获取关于我们信息
    public static String getAPP_Info = Ip_head + "Introduce/getAboutUsInfo";
    //反馈信息
    public static String upload_Feed = Ip_head + "Feed/saveFeedBack";
    //帮助中心
    public static String getHelp_Info = Ip_head + "Help/showYxPhone?";
    //获取校园图片
    public static String getStyle_Img = Ip_head + "StyleImg/getStyleImg?";

}
