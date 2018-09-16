package com.ssm.manager.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ssm.manager.controller.response.APIVo;
import com.ssm.manager.controller.response.ServletUtils;
import com.ssm.manager.pojo.User;
import com.ssm.manager.service.BitcoinRPCService;
import com.ssm.manager.service.PythonClightningHttpService;

@Controller
@RequestMapping("/h5")
public class H5Controller {

	@RequestMapping("/app")
	public String app(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		return "h5/app";
	}

	@RequestMapping(value="/tologin",method = RequestMethod.GET)
	public String tologin(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		return "h5/login";
	}
	
	//static JsonObject listNodes = PythonClightningHttpService.getInstance().getListNode();
	
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public String login(HttpServletRequest request,Model model,@ModelAttribute User user) {
		model.addAttribute("contextPath", request.getContextPath());
		HttpSession session = request.getSession();
	    if((user.getUserName().equals("ln")&&user.getPassWord().equals("hello2018"))
	    		||(user.getUserName().equals("测试")&&user.getPassWord().equals("hello2018")))
	    {  
	        //登陆成功  
	    	session.setAttribute("uname",user.getUserName());  
	    	model.addAttribute("balance", BitcoinRPCService.getInstance().getbalance(""));	
			model.addAttribute("alladdress", BitcoinRPCService.getInstance().getaddressesbyaccount(""));	    	
			model.addAttribute("nodes", PythonClightningHttpService.getInstance().getListNode());	    	
	        return "h5/login";  
	    }else{  
	    	model.addAttribute("err", "用户名密码错误！");
	        //登陆失败  
	        return "h5/login";  
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

	
	
	@RequestMapping("/liststore")
	public String listStore(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());

		return "h5/liststore";
	}		
	
	
	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	public String String(HttpServletRequest requests,Model model) {	
		try {
			//默认找一个永不过期票据
			//10000 1000小时
			String bolt11 = "lntb100n1pd0zwjqpp5q8sckxvffre024mg8yjezy8gv9zcqp55pjdaq7llyydpkm26vmqsdqyd4jsxqxpz25gqcqpx6hy3sevpevvp5dr8szkchku5cp4ssl2v2vnmz987gm29nrt0x58rqqyqxxjh4edw67nvu4pg0w4nljxn30nmf3paqnq9ylk30e83shqpqmlgpj";
			JsonObject re = PythonClightningHttpService.getInstance().pay(bolt11);		
			System.out.println(" bolt11 :"+bolt11+" | re:"+re);
			model.addAttribute("preimage",re);
		} catch (Exception e) {
		
		}
		return "h5/pay";
	}		
	

	
	

}
