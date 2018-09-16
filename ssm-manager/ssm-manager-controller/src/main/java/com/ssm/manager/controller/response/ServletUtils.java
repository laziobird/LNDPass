package com.ssm.manager.controller.response;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ServletUtils {
	/**
	 * 解决方案编码UTF-8
	 * @param data
	 * @param response
	 */
	public static void write(Object data,HttpServletResponse response){
		Gson gson = new Gson();				
		response.setHeader("Cache-Control", "no-cache");   
        response.setContentType("text/json;charset=UTF-8");  
        response.setCharacterEncoding("UTF-8");  	

		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(gson.toJson(data));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}		
				
	}
	
	public static void write(JsonObject data,HttpServletResponse response){	
		response.setHeader("Cache-Control", "no-cache");   
        response.setContentType("text/json;charset=UTF-8");  
        response.setCharacterEncoding("UTF-8");  
		PrintWriter out;
		try {
			out = response.getWriter();
			out.write(data.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}		
				
	}	
	

}
