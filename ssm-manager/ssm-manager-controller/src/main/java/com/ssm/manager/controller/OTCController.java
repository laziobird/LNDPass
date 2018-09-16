package com.ssm.manager.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ssm.manager.pojo.User;
import com.ssm.manager.service.BitcoinRPCService;
import com.ssm.manager.service.PythonClightningHttpService;

@Controller
@RequestMapping("/otc")
public class OTCController {



	@RequestMapping(value="/tologin",method = RequestMethod.GET)
	public String tologin(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		return "otc/login";
	}
	
	
	@RequestMapping(value="/topost",method = RequestMethod.GET)
	public String topost(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		return "otc/post";
	}	
	
	
	@RequestMapping(value="/tobuy",method = RequestMethod.GET)
	public String tobuy(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		return "otcbtc";
	}		
	
	
	@RequestMapping(value="/tosell",method = RequestMethod.GET)
	public String tosell(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		return "otcbtc";
	}	
	
	
	@RequestMapping(value="/buyone",method = RequestMethod.GET)
	public String buyone(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		return "otc/buyone";
	}		
	
	
	@RequestMapping(value="/help1",method = RequestMethod.GET)
	public String help1(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		return "otc/help1";
	}		
	
	
	@RequestMapping(value="/help2",method = RequestMethod.GET)
	public String help2(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		return "otc/help2";
	}		
	
	
	@RequestMapping(value="/wallet",method = RequestMethod.GET)
	public String wallet(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		return "otc/wallet";
	}		
	
	
	//static JsonObject listNodes = PythonClightningHttpService.getInstance().getListNode();
	
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public String login(HttpServletRequest request,Model model,@ModelAttribute User user) {
		model.addAttribute("contextPath", request.getContextPath());
		HttpSession session = request.getSession();
	    if((user.getUserName().equals("otcUser")&&user.getPassWord().equals("hello2018"))
	    		||(user.getUserName().equals("otcTest")&&user.getPassWord().equals("hello2018")))
	    {  
	        //登陆成功  
	    	session.setAttribute("otcName",user.getUserName());     	
	        return "otcbtc";  
	    }else{  
	    	model.addAttribute("otcerr", "用户名密码错误！");
	        //登陆失败  
	        return "otc/login";  
	    } 
	}	

	@RequestMapping(value="/index5",method = RequestMethod.GET)
	public String index(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
    	model.addAttribute("balance", BitcoinRPCService.getInstance().getbalance(""));
    	model.addAttribute("alladdress", BitcoinRPCService.getInstance().getaddressesbyaccount(""));
		model.addAttribute("nodes", PythonClightningHttpService.getInstance().getListNode());			
		return "h5/login";
	}	
	

	
	@RequestMapping("/myorder")
	public String myorder(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		JsonArray list = PythonClightningHttpService.getInstance().listLNtransactions().get("payments").getAsJsonArray();
	    //过滤处理，按时间倒排，显示中文时间
		ArrayList<JsonObject> newList = new ArrayList<JsonObject>();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		for (JsonElement jsonElement : list) {
			JsonObject jo = jsonElement.getAsJsonObject();
			//if(jo.get("status").equals("complete")){
				Date date = new Date();
				date.setTime(jo.get("created_at").getAsLong());
				jo.addProperty("chinesetime",simpleDateFormat.format(date));
				newList.add(jo);
			//}
		}

		Collections.reverse(newList);
		model.addAttribute("payments", newList);
		return "h5/myorder";
	}	

	


	
	

}
