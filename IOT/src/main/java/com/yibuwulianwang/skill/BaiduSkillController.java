package com.yibuwulianwang.skill;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.yibuwulianwang.handle.Handle;
import com.yibuwulianwang.handle.Status;
import com.yibuwulianwang.skill.baidu.SkillBot;

@Controller
@RequestMapping("/baidu/skill")
public class BaiduSkillController{
	/**
     * @see HttpServlet#HttpServlet()
     */
    public BaiduSkillController() {
        super();
    }

    /**
     * 重写doPost方法，处理POST请求
     * 
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
	@RequestMapping(method = RequestMethod.POST)
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//		request.setCharacterEncoding("UTF-8");
//		BufferedReader reader = request.getReader();
//		String json = reader.readLine();
//		Handle.printSkillLog(request.getRequestURI(),"Request-POST:"+json);
		
        // 根据request创建Bot
    	SkillBot bot = new SkillBot(request);

        // 打开签名验证
        // bot.enableVerify();

        // 线下调试时，可以关闭签名验证
        bot.disableVerify();

        try {
            // 调用bot的run方法
            String responseJson = bot.run();
            // 设置response的编码UTF-8
            response.setCharacterEncoding("UTF-8");
            
    		Handle.printSkillLog(request.getRequestURI(),"Response-Json:"+responseJson);
            // 返回response
            response.getWriter().append(responseJson);
        } catch (Exception e) {
            response.getWriter().append(new JSONObject().toJSONString(new Status(1,"")));//"{\"status\":1,\"msg\":\"\"}"
        }
    }
    
	@RequestMapping(method = RequestMethod.HEAD)
    protected void doHead(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//		request.setCharacterEncoding("UTF-8");
//		BufferedReader reader = request.getReader();
//		String json = reader.readLine();
//		Handle.printSkillLog(request.getRequestURI(),"HEAD:"+json);
    }
}
