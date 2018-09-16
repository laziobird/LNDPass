package com.ssm.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ssm.manager.pojo.User;
import com.ssm.manager.service.BitcoinRPCService;
import com.ssm.manager.service.UserService;

@Controller  
public class UserController {  
	@Autowired
	UserService userService;
	
	@RequestMapping(value="/tologin",method = RequestMethod.GET)
	public String tologin(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		return "login";
	}
	
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public String login(HttpServletRequest request,Model model,@ModelAttribute User user) {
		model.addAttribute("contextPath", request.getContextPath());
		HttpSession session = request.getSession();
		String passwd = userService.getUserByName(user.getUserName()).getPassWord();
	    if((user.getUserName()!=null)&&(StringUtils.equals(passwd, user.getPassWord())))
	    {  
	        //登陆成功  
	    	session.setAttribute("uname",user.getUserName());  
	    	model.addAttribute("balance", BitcoinRPCService.getInstance().getbalance(""));
	        return "donate";  
	    }else{  
	    	model.addAttribute("err", "用户名密码错误！");
	        //登陆失败  
	        return "login";  
	    } 
	}	
	
     @RequestMapping(value="/user/{userId}/roles/{roleId}",method = RequestMethod.GET)  
     public String getLogin(@PathVariable("userId") String userId,  
         @PathVariable("roleId") String roleId){  
         System.out.println("User Id : " + userId);  
         System.out.println("Role Id : " + roleId);  
         return "hello";  
     }  
     @RequestMapping(value="/javabeat/{regexp1:[a-z-]+}",  
           method = RequestMethod.GET)  
     public String getRegExp(@PathVariable("regexp1") String regexp1){  
           System.out.println("URI Part 1 : " + regexp1);  
           return "hello";  
     }  
}

