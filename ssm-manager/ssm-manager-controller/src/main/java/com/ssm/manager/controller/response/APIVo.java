package com.ssm.manager.controller.response;

import com.google.gson.Gson;
import com.ssm.manager.service.common.CommonEnum;

public class APIVo {
	
	Object result;
	boolean success = false;
	String msg;
	int code = CommonEnum.SUCCESS.getValue();
	
	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}


	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

    public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static void main(String[] args) {
		APIVo v = new APIVo();
		Integer i = 5;
		v.setResult(i);
		v.setSuccess(true);
		Gson gson = new Gson();
		v.setMsg("添加活动成功");
		System.out.println(gson.toJson(v));
	}
}
