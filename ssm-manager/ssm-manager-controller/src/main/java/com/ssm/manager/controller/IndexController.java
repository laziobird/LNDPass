package com.ssm.manager.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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
import com.ssm.manager.pojo.Invoice;
import com.ssm.manager.service.BitcoinRPCService;
import com.ssm.manager.service.PythonClightningHttpService;

@Controller
public class IndexController {


	@RequestMapping("/")
	public String index(HttpServletRequest request,Model model) {
		return donate(request,model) ;
	}

	@RequestMapping("/donate")
	public String donate(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		return "donate";
	}
	
	@RequestMapping("/otcbtc")
	public String otcbtc(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		return "otcbtc";
	}	
	
	@RequestMapping("/about")
	public String about(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		return "about";
	}	
	
	@RequestMapping(value = "/invoice", method = RequestMethod.POST)
	@ResponseBody
	public String invoice(HttpServletRequest request,@ModelAttribute Invoice invoice) {			
		JsonObject re = PythonClightningHttpService.getInstance().invoice(invoice);
		String bolt11 = re.get("bolt11").getAsString();
		System.out.println(" bolt11 :"+bolt11);
		return bolt11;
		//return "lntb2n1pddfq77pp5se9tja43avqfq82gsa2ghqwpszdtweeyrg82jpfu8dqa5suwzq5sdqgve5hyum5cqpxh0p2ptjgw2vzn3q2pmdl8su0jk4q40czmr5hm7avttpv26wrne847qmvnpyq8s0pyrzfrmu4lgf65s2ju8nu63epkj6hmz4krsdz7pqqdkendr";
	}
	
	
	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	@ResponseBody
	public String pay(HttpServletRequest request,@RequestParam String  bolt11) {			
		JsonObject re = PythonClightningHttpService.getInstance().pay(bolt11);		
		System.out.println(" bolt11 :"+bolt11+" | re:"+re);
		//return bolt11;
		return re.toString();
	}	
	
	
	
	@RequestMapping(value = "/topay", method = RequestMethod.GET)
	public String pay(HttpServletRequest request,Model model,@RequestParam String  bolt11) {	
    	model.addAttribute("contextPath", request.getContextPath());		
		JsonObject decodepay = PythonClightningHttpService.getInstance().decodepayInvoice(bolt11);
		model.addAttribute("bolt11", bolt11);
		model.addAttribute("decodepay", decodepay);
		model.addAttribute("balance", BitcoinRPCService.getInstance().getbalance(""));
		System.out.println(" bolt11 :"+bolt11+" | decodepay:"+decodepay);
		return "pay";	
	}	
	
	
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getTest(HttpServletRequest request,Model model) {
    	model.addAttribute("contextPath", request.getContextPath());
        return "login";
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
		return "myorder";
	}	

	
	
	
	
	

}
