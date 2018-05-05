package com.yibuwulianwang.handle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yibuwulianwang.skill.ali.PackSkillResponse;
/***
 * 对响应的封装
 * @author Administrator
 *
 */
public class Handle {
	/***
	 * 封装请求和响�?
	 * @param request 请求
	 * @param resp 响应
	 * @param obj 响应的json实体类对�?
	 */
	public static void request(HttpServletRequest request,HttpServletResponse resp,Object obj){
		try {
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			request.setCharacterEncoding("UTF-8");
			BufferedReader reader = request.getReader();
			String json = reader.readLine();
			printRequestLog(request.getRequestURI(),json);
			if (json == null) {		
				String jsonResponse1 = new JSONObject().toJSONString(new Status(request.getRequestURI(),"null",Code.HTTP_API_NULL));
				printResponseLog(request.getRequestURI(),jsonResponse1);
				resp.getWriter().append(jsonResponse1);
			}else {
				String jsonResponse2 = new JSONObject().toJSONString(obj);
				printResponseLog(request.getRequestURI(),jsonResponse2);
				resp.getWriter().append(jsonResponse2);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void printResponseLog(String url, String msg) {
		System.out.println(getDate()+"-Response-" + url + "-" + msg);// 2016-05-12 10:41:38
	}

	public static void printRequestLog(String url, String msg) {
		System.out.println(getDate() + "-Request-" + url + "-" + msg);// 2016-05-12 10:41:38
	}
	
	public static void printMqttLog(String url, String msg) {
		System.out.println(getDate() + "-Mqtt-" + url + "-" + msg);// 2016-05-12 10:41:38
	}
	
	public static void printMysqlLog(String url, String msg) {
		System.out.println(getDate() + "-Mysql-" + url + "-" + msg);// 2016-05-12 10:41:38
	}
	
	public static void printSkillLog(String url, String msg) {
		System.out.println(getDate() + "-Skill-" + url + "-" + msg);// 2016-05-12 10:41:38
	}

	public static String getDate() {
		// 日期 -> 文本
		Date date = new Date();
		// 无参构�??
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// 带参构�??
		String time = dateFormat.format(date);

		return time;
	}
}
