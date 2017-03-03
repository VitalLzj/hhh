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
    //发表论坛
    public static String upload_Forum = Ip_head + "Forum/saveForum?";
    //获取论坛信息
    public static String getForum_List = Ip_head + "Forum/getForumList?";
    //获取论坛详情
    public static String getForum_Detail = Ip_head + "Forum/getForumDetail?";
    //提交评论
    public static String upload_Forum_Pl = Ip_head + "ForumPl/saveForumPl?";
    //获取帖子评论列表
    public static String getForum_Pl_List = Ip_head + "ForumPl/getForumPlList?";
    //检测用户是否点赞
    public static String check_User_IsZan = Ip_head + "ForumPl/getUserIsZan?";
    //用户点赞
    public static String zan_Forum = Ip_head + "ForumPl/zanForum?";
    //获取用户的帖子
    public static String get_User_Forum = Ip_head + "Forum/getUserForum?";
    //删除用户帖子
    public static String delete_User_Forum = Ip_head + "Forum/deleteUserForum?";
    //获取用户的回复
    public static String get_User_Reply = Ip_head + "ForumPl/getMyReply?";
    //删除用户回复
    public static String delete_User_Reply = Ip_head + "ForumPl/deleteUserReply?";
    //获取图书查询banner
    public static String get_Book_Banner = Ip_head + "Book/showBanners?";
    //获取图书列表
    public static String get_Book_List = Ip_head + "Book/getBooks?";
    //获取图书详情
    public static String get_Book_Detail = Ip_head + "Book/getBookDetail?";
    //用户是否收藏
    public static String get_User_Is_Sc = Ip_head + "Book/getUserIsSc?";
    //收藏书籍
    public static String doSc_Book = Ip_head + "Book/scBook?";
    //获取收藏列表
    public static String get_Sc_List = Ip_head + "Book/getScList?";
    //取消收藏书籍
    public static String do_Quite_Sc = Ip_head + "Book/quiteBookSc?";
    //获取推荐图书
    public static String get_Tj_Books = Ip_head + "Book/getTjBooks";
    //搜索图书
    public static String search_Books = Ip_head + "Book/searchBooks?";
    //获取可选择的学年
    public static String get_Xn = Ip_head + "Student/getStudentXq";

    //获取验证码的url
    public static String get_Code = "http://202.196.240.43/jwweb/sys/ValidateCode.aspx";
    //获取cookie的url
    public static String get_Cookie = "http://202.196.240.43/jwweb/_data/index_LOGIN.aspx";
    //进行登录的url
    public static String do_Login = "http://202.196.240.43/jwweb/_data/home_login.aspx";
    //学生信息url
    public static String get_Student_Info = "http://202.196.240.43/jwweb/xsxj/Stu_MyInfo_RPT.aspx";
    //学生成绩信息
    public static String get_Student_Head = "http://202.196.240.43/jwweb/xscj/Stu_MyScore_rpt.aspx";
    //学生成绩
    public static String get_Student_Score = "http://202.196.240.43/jwweb/xscj/Stu_MyScore_Drawimg.aspx?";
}
