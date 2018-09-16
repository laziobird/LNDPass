package com.ssm.manager.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 */
public class LoginHandlerIntercepter implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        //测试先跨域
        response.setHeader("Access-Control-Allow-Origin", "*");		
		String requestURI = request.getRequestURI();	
		
		if ( requestURI.indexOf("h5") > 0  && requestURI.indexOf("login") < 0 ) {
			// 目前加上捐赠页面
			HttpSession session = request.getSession();
			String uname = (String) session.getAttribute("uname");
			if (uname != null) {
				// 登陆成功的用户
				return true;
			} else {
				// 没有登陆，转向登陆界面
				request.getRequestDispatcher("/h5/tologin").forward(request, response);
				return false;
			}
		} 

		return true;
	}

}
