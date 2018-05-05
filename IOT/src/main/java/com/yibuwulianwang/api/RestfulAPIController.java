package com.yibuwulianwang.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/restfulapi")
public class RestfulAPIController {
	// 查找
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	private Object get(@PathVariable long id, 
			@RequestParam(value = "name", defaultValue = "huangdayu") String name) {
		JSONObject json = new JSONObject();
		json.put("mothod","GET");
		json.put("id",id);
		json.put("name", name);
		return json;
	}
	// 增加
	@RequestMapping(method = RequestMethod.POST)
	private Object post() {
		JSONObject json = new JSONObject();
		json.put("mothod","POST");
		return json;
	}
	// 修改
	@RequestMapping(method = RequestMethod.PUT)
	private Object put() {
		return null;
	}

	// 删除
	@RequestMapping(method = RequestMethod.DELETE)
	private Object delete() {
		return null;
	}

	@RequestMapping(method = RequestMethod.HEAD)
	private Object head() {
		return null;
	}

	@RequestMapping(method = RequestMethod.OPTIONS)
	private Object options() {
		return null;
	}

	@RequestMapping(method = RequestMethod.PATCH)
	private Object patch() {
		return null;
	}

	@ResponseBody // 返回json,xml,text 等文本,需要添加@ResponseBody注解
	@RequestMapping(method = RequestMethod.TRACE)
	private Object trace(@PathVariable("id") Integer id) {
		return null;
	}
}
