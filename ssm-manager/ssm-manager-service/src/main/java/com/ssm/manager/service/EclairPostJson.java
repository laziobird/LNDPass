package com.ssm.manager.service;

import java.util.List;

public class EclairPostJson {
	String method;
	String jsonrpc;
	List<Object> params;
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getJsonrpc() {
		return jsonrpc;
	}
	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}
	public List<Object> getParams() {
		return params;
	}
	public void setParams(List<Object> params) {
		this.params = params;
	}
	@Override
	public String toString() {
		return "EclairPostJson [method=" + method + ", jsonrpc=" + jsonrpc + ", params=" + params + "]";
	}
	
}
