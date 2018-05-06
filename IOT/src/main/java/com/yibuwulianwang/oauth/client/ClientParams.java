package com.yibuwulianwang.oauth.client;

/**
 * 封装OAuth Server端认证需要的参数
 */
public class ClientParams {

	public static final String CLIENT_ID = "e85ca381-0df7-4c94-ab8a-a40bfbf401d2"; // 应用id CLIENT_ID

    public static final String CLIENT_SECRET = "6bd671cf-3502-45b7-9840-64bc9aa74e8a"; // 应用secret CLIENT_SECRET
	
	public static final String USERNAME = "admin"; // 用户名

	public static final String PASSWORD = "admin"; // 密码
	
	public static final String YIBUWULIANWANG="https://yibuwulianwang.com:8443";
	
	public static final String LOCALHOST="http://localhost:8080";

	public static final String OAUTH_SERVER_URL = YIBUWULIANWANG+ "/iot/oauthserver/getOAuthorizationCode"; // 授权地址

	public static final String OAUTH_SERVER_TOKEN_URL = YIBUWULIANWANG+ "/iot/oauthserver/getAccessToken"; // ACCESS_TOKEN换取地址

    public static final String OAUTH_SERVER_REDIRECT_URI =YIBUWULIANWANG+"/iot/callback"; // 回调地址

    public static final String OAUTH_SERVICE_API = YIBUWULIANWANG+ "/iot/oauthserver/getUserData"; // 测试开放数据api

}
