package com.yibuwulianwang.oauth.client;

import org.apache.oltu.oauth2.common.message.types.ResponseType;

import com.alibaba.fastjson.JSONObject;

import javax.ws.rs.HttpMethod;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/5/29.
 */
public class UrlClient {
	
	static String refresh_token = null ;
	static String expires_in = null;

    /**
     * 获取授权码
     * @return
     */
    private static String getAuthCode() throws Exception{

        Map<String,Object> params = new LinkedHashMap<String,Object>();
        params.put("username",ClientParams.USERNAME);
        params.put("password",ClientParams.PASSWORD);
        params.put("client_id",ClientParams.CLIENT_ID);
        params.put("response_type", ResponseType.CODE.toString());
        params.put("redirect_uri",ClientParams.OAUTH_SERVER_REDIRECT_URI);

        StringBuilder postStr = new StringBuilder();

        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postStr.length() != 0){postStr.append('&');}
            postStr.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postStr.append('=');
            postStr.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postStrBytes = postStr.toString().getBytes("UTF-8");

        URL url = new URL(ClientParams.OAUTH_SERVER_URL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod(HttpMethod.POST);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postStrBytes.length));
        connection.getOutputStream().write(postStrBytes);

        ((HttpURLConnection) connection).setInstanceFollowRedirects(false);// 必须设置该属性
        String location = connection.getHeaderField("Location");
        System.out.println("getAuthCode:"+location);
        return location.substring(location.indexOf("=")+1);
    }

    /**
     * 获取accessToken
     * @return
     */
    private static String getAccessToken(String authCode) throws Exception{

        Map<String,Object> params = new LinkedHashMap<String,Object>();
        params.put("client_id",ClientParams.CLIENT_ID);
        params.put("client_secret",ClientParams.CLIENT_SECRET);
        params.put("grant_type","authorization_code");
        params.put("code", authCode);
        params.put("redirect_uri",ClientParams.OAUTH_SERVER_REDIRECT_URI);

        StringBuilder postStr = new StringBuilder();

        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postStr.length() != 0){postStr.append('&');}
            postStr.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postStr.append('=');
            postStr.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postStrBytes = postStr.toString().getBytes("UTF-8");

        URL url = new URL(ClientParams.OAUTH_SERVER_TOKEN_URL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod(HttpMethod.POST);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postStrBytes.length));
        connection.getOutputStream().write(postStrBytes);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("getAccessToken:"+response.toString());
        
        //GSON 解包

        Map<String,String> map = JSONObject.parseObject(response.toString(),Map.class);
        String accessToken = map.get("access_token");
        UrlClient.refresh_token = map.get("refresh_token");
        UrlClient.expires_in = String.valueOf(map.get("expires_in"));
        return accessToken;
    }

    /**
     * 获取accessToken
     * @return
     */
    private static String getNewAccessToken(String refresh_token) throws Exception{
    	
    	//https://XXXXX/token?grant_type=refresh_token&client_id=XXXXX&client_secret=XXXXXX&refresh_token=XXXXXX

        Map<String,Object> params = new LinkedHashMap<String,Object>();
        params.put("client_id",ClientParams.CLIENT_ID);
        params.put("client_secret",ClientParams.CLIENT_SECRET);
        params.put("grant_type","refresh_token");
        params.put("refresh_token", refresh_token);
        params.put("redirect_uri",ClientParams.OAUTH_SERVER_REDIRECT_URI);

        StringBuilder postStr = new StringBuilder();

        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postStr.length() != 0){postStr.append('&');}
            postStr.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postStr.append('=');
            postStr.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postStrBytes = postStr.toString().getBytes("UTF-8");

        URL url = new URL(ClientParams.OAUTH_SERVER_TOKEN_URL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod(HttpMethod.POST);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(postStrBytes.length));
        connection.getOutputStream().write(postStrBytes);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("getAccessToken:"+response.toString());
        
        //GSON 解包
        
        //Gson gson = new GsonBuilder().create();
        //Map<String,String> map = gson.fromJson(response.toString(),Map.class);
        
        Map<String,String> map = JSONObject.parseObject(response.toString(),Map.class);
        String accessToken = map.get("access_token");
        UrlClient.refresh_token = map.get("refresh_token");

        return accessToken;
    }
    
    /**
     * 获取数据
     * @return
     */
    private static void getUserData(String accessToken) throws Exception{

        URL url = new URL(ClientParams.OAUTH_SERVICE_API+"?access_token="+accessToken);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod(HttpMethod.GET);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("getUserData:"+response.toString());
    }


    public static void main(String[] args) throws Exception{
        String authCode = getAuthCode();
        String accessToken = getAccessToken(authCode);
        getUserData(accessToken);
        while(true) {
        	System.out.println(expires_in);
        	Double D1=new Double(expires_in); 
        	int i1=D1.intValue(); 
        	Thread.sleep(i1);
        	getNewAccessToken(refresh_token);
        }
    }
}
