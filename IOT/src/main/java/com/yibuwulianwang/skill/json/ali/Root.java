package com.yibuwulianwang.skill.json.ali;

import java.util.List;

public class Root {
	private Header header;

	private Payload payload;

	public void setHeader(Header header) {
		this.header = header;
	}

	public Header getHeader() {
		return this.header;
	}

	public void setPayload(Payload payload) {
		this.payload = payload;
	}

	public Payload getPayload() {
		return this.payload;
	}

	/***
	 * 查询属�?�数�?
	 */
	private List<Properties> properties;

	public List<Properties> getProperties() {
		return properties;
	}

	public void setProperties(List<Properties> properties) {
		this.properties = properties;
	}

}