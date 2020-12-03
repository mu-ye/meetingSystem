package com.demo.demo.core.common;


/**
 * @author 牟欢
 * @Classname SystemCommon
 * @Description TODO
 * @Date 2020-04-03 15:50
 */
public class SystemCommon {
    /**
     *  文件添加水印预览
     */

    /**
     *  上传文件保存的本地地址
     */
    public static final String WEB_CONFIG_LOCATION="file:///C:/meeting/";

    /**
     * 前端访问静态资源映射
     */
    public static final String WEB_CONFIG_HANDLER = "/api/file/**";

    /**
     *  文件在服务器中 存储位置的跟目录
     */
    public static final String FILE_SAVE_PATH="C:\\meeting\\";

    /**
     *  水印服务器下载 文件的 根地址
     */
    public static final String DOWNLOAD_BASE_URL = "http://192.168.138.142:8081/api/file/";

    /**
     * 后台文件预览的 APP_ID APP_SECRET (http://192.168.138.142:8081)
     */
//    public static final String APP_ID="502e3e849c664b8e77621cee5e383d28";
//    public static final String APP_SECRET="hAI0n8Irhoals%2BXNvGWOcj%2Bx9iQ%3D";


    public static final String APP_ID="99759a0cd4a943287582a8cb3168c649";
    public static final String APP_SECRET="R8pPXfhgV9NjOKvfA9p%2FJS6qIoM%3D";


    /**
     *  根据ID 获取用户信息的 测试
     */
//    public static final String APP_ID_DD="2020060110064849";
//    public static final String APP_WD_DD="66f224956a0046b69c4d1351c4462c22";

    
    /**
     *   钉钉微应用配置
     */
    public static final String APP_ID_DD="2020060110064849";
    public static final String APP_WD_DD="66f224956a0046b69c4d1351c4462c22";
    /**
     *  南京地铁钉钉
     */
    public static final String CORP_ID = "ding94b3e75e6e35e051";


}