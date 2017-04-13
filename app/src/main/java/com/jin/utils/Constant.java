package com.jin.utils;

/*
 * 版本升级注意的问题：
 * 1、改Constant中的BASE_URL为
 * 2、改Manifese中的版本号和版本名称  发布版本是4   4.0
 * 3、改服务器中的4个参数
 */
public class Constant {
    public static final String BASE_URL = "http://60674e3.all123.net/SLNews";
//    public static final String BASE_URL = "http://121.42.175.80/SLNews";
    // 访问电脑服务器
    //public static final String BASE_URL = "http://192.168.199.209:8080/SLNews";

    /**
     * 发布表白
     */
    public static final String FA_BIAOBAI_URL = BASE_URL + "/servlet/FaBiaoBaiServlet";
    /**
     * 表白列表
     */
    public static final String BIAOBAI_LISTS_URL = BASE_URL + "/servlet/BiaoBaiWellServlet";
    /**
     * 点赞
     */
    public static final String ZAN_URL = BASE_URL + "/servlet/ZanServlet";

    /**
     * 自动更新json网址
     */
    public static final String UPDATE_JSON_URL = BASE_URL + "/update/update.json";
    // public static final String UPDATE_JSON_URL =
    // "http://www.java666.com/SLNews_update/update.json";
    /**
     * 获取分类信息的接口
     */
    public static final String CATEGORIES_URL = BASE_URL + "/categories.json";
}
