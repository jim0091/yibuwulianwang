package com.yibuwulianwang.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yibuwulianwang.handle.Code;
import com.yibuwulianwang.handle.Handle;
import com.yibuwulianwang.handle.Status;
import com.yibuwulianwang.mysql.dao.TempHumDao;
import com.yibuwulianwang.mysql.dao.impl.TempHumDaoImpl;
import com.yibuwulianwang.mysql.entity.TempHum;
import com.yibuwulianwang.mysql.service.TempHumService;
import com.yibuwulianwang.mysql.service.impl.TempHumServiceImpl;

@Controller
@RequestMapping("/temphum")
public class TempHumController {
	
	@Autowired
    private TempHumService tempHumService;
	
	@RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		TempHum temphum = tempHumService.findLast();
		resp.getWriter().append(new JSONObject().toJSONString(temphum));
		return "temphum";
    }
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
    public void create(HttpServletRequest request,HttpServletResponse resp,TempHum tempHum) {	
		BufferedReader reader;
		String json="";
		try {
			request.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			reader = request.getReader();
			json = reader.readLine();
			if (json != null)
				json = json.replaceAll("\n", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		com.yibuwulianwang.json.me.MyPayloadJson  root = JSON.parseObject(json,new TypeReference<com.yibuwulianwang.json.me.MyPayloadJson>() {});
		;
		tempHum.setHumidity(root.getHumidity());
		tempHum.setTemperature(root.getTemperature());
		tempHum.setDate(Handle.getDate());
		tempHumService.createTempHum(tempHum);
		try {
			resp.getWriter().append(new JSONObject().toJSONString(new Status(request.getRequestURI(),Code.HTTP_API_OK)));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	@RequestMapping(value = "/get/findLast", method = RequestMethod.GET)
    public void getFindLast(HttpServletRequest request,HttpServletResponse resp) {	
		BufferedReader reader;
		String json="";
		try {
			request.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			reader = request.getReader();
			json = reader.readLine();
			if (json != null)
				json = json.replaceAll("\n", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			resp.getWriter().append(new JSONObject().toJSONString(tempHumService.findLast()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }
	
	@RequestMapping(value = "/post/findLast", method = RequestMethod.POST)
    public void postFindLast(HttpServletRequest request,HttpServletResponse resp) {	
		BufferedReader reader;
		String json="";
		try {
			request.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			reader = request.getReader();
			json = reader.readLine();
			if (json != null)
				json = json.replaceAll("\n", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			resp.getWriter().append(new JSONObject().toJSONString(tempHumService.findLast()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }
	
}
