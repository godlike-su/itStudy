package com.itStudyService.util;

public class Global
{
    //文件映射位置
    static FileStore fileStore = new FileStore("c:/itStudy/", "/");

    // 临时图片的存储位置
    static FileStore tmpStore = new FileStore("c:/itStudy/tmp/", "/tmp/");

    // 图片的存储位置
    static FileStore photoStore = new FileStore("c:/itStudy/photo/", "/photo/");

    //帖子图片保存路径
    static FileStore messageStore = new FileStore("c:/itStudy/article/", "/article/");

    //问题图片保存路径
    static FileStore analysisStore = new FileStore("c:/itStudy/analysis/", "/analysis/");

    //头像保存路径
    static FileStore thumbStore = new FileStore("c:/itStudy/thumb/", "/thumb/");



    //认证缓存名字
    static String authenticationCache = "AuthenticationCache";
    //认证缓存名字
    static String AdminauthenticationCache = "AdminAuthenticationCache";

    //授权缓存名字
    static String authorizationCache = "AuthorizationCache";

    static String AdminauthorizationCache = "AdminAuthorizationCache";

    static String Module = "itStudyModule";

    public static String getModule()
    {
        return Module;
    }

    public static FileStore getTmpStore()
    {
        return tmpStore;
    }

    public static FileStore getThumbStore()
    {
        return thumbStore;
    }

    public static FileStore getPhotoStore()
    {
        return photoStore;
    }

    public static FileStore getMessageStore()
    {
        return messageStore;
    }

    public static FileStore getAnalysisStoreStore()
    {
        return analysisStore;
    }

    public static String getAuthenticationCache()
    {
        return authenticationCache;
    }

    public static String getAuthorizationCache()
    {
        return authorizationCache;
    }


    public static String getAdminauthenticationCache()
    {
        return AdminauthenticationCache;
    }

    public static String getAdminauthorizationCache()
    {
        return AdminauthorizationCache;
    }


}
