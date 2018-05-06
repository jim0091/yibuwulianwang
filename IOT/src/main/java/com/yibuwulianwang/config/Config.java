package com.yibuwulianwang.config;

public class Config {
	// oauth2 server
	public final static String OAUTH_CLIENT_ID = "e85ca381-0df7-4c94-ab8a-a40bfbf401d2";
	public final static String OAUTH_CLIENT_SECRET = "6bd671cf-3502-45b7-9840-64bc9aa74e8a";
	public final static String ACCESS_TOKEN_TIME = String.valueOf(36000);
	public final static String OAUTH_USERNAME = "admin";
	public final static String OAUTH_PASSWORD = "admin";
	// http api publish mqtt payload
	public final static String EMQTT_HTTP_API_URL = "http://yibuwulianwang.com:18000/api/v2/mqtt/publish";
	public final static String EMQTT_USER_PASSWORD = "admin:public";
	public final static String EMQTT_CLIEND_ID = "iot";
	public final static String EMQTT_TOPIC = "yibuwulianwang";
	public final static int QOS = 1;
	public final static String BROKER = "tcp://yibuwulianwang.com:1883";
	public final static String USER_NAME = "SpringMVC";
	public final static String USER_PASSWORD = "yibuwulianwang";
}
