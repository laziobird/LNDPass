package com.ssm.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.ssm.manager.controller.response.APIVo;
import com.ssm.manager.controller.response.ServletUtils;
import com.ssm.manager.pojo.Invoice;
import com.ssm.manager.pojo.User;
import com.ssm.manager.service.BitcoinRPCService;
import com.ssm.manager.service.PythonClightningHttpService;

@Controller
@RequestMapping("/api")
public class APIController {

	@RequestMapping(value = "/mobile/login", method = RequestMethod.GET)
	@ResponseBody
	public void login(HttpServletRequest request, HttpServletResponse response, @ModelAttribute User user) {
		HttpSession session = request.getSession();
		APIVo apivo = new APIVo();
		if (StringUtils.isNotBlank(user.getUserName()) && StringUtils.isNotBlank(user.getPassWord())
				&& ((user.getUserName().equals("ln") && user.getPassWord().equals("hello2018"))
						|| (user.getUserName().equals("test") && user.getPassWord().equals("hello2018")))) {
			// 登陆成功
			session.setAttribute("uname", user.getUserName());
			apivo.setSuccess(true);
			apivo.setMsg("登录成功");
			apivo.setResult(user);
		} else {
			apivo.setSuccess(false);
			apivo.setMsg("用户名密码错误！");
		}
		ServletUtils.write(apivo, response);
	}

	@RequestMapping(value = "/listaccounts", method = RequestMethod.GET)
	@ResponseBody
	public void listaccounts(HttpServletResponse response) {
		APIVo apivo = new APIVo();
		// 登陆成功
		apivo.setSuccess(true);
		apivo.setMsg("查询成功");
		apivo.setResult(BitcoinRPCService.getInstance().listAccounts());
		ServletUtils.write(apivo, response);
	}

	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	@ResponseBody
	public void pay(HttpServletRequest request,@RequestParam String  bolt11,HttpServletResponse response) {	
		APIVo apivo = new APIVo();
		try {
			JsonObject re = PythonClightningHttpService.getInstance().pay(bolt11);		
			System.out.println(" bolt11 :"+bolt11+" | re:"+re);
			apivo.setResult(re);
			apivo.setSuccess(true);
		} catch (Exception e) {
			apivo.setSuccess(false);
			apivo.setMsg(e.getMessage());
		}

		ServletUtils.write(apivo, response);
	}	
		
	
	
	@RequestMapping(value = "/listtransactions", method = RequestMethod.GET)
	@ResponseBody
	public void listtransactions(HttpServletResponse response) {
		APIVo apivo = new APIVo();
		// 登陆成功
		apivo.setSuccess(true);
		apivo.setMsg("买家比特币交易汇总");
		apivo.setResult(BitcoinRPCService.getInstance().listTransactions());
		ServletUtils.write(apivo, response);
	}	
	
	@RequestMapping(value = "/listLNtransactions", method = RequestMethod.GET)
	@ResponseBody
	public void listLNtransactions(HttpServletResponse response) {
		APIVo apivo = new APIVo();
		// 登陆成功
		apivo.setSuccess(true);
		apivo.setMsg("闪电网络买家目前交易汇总");
		apivo.setResult(PythonClightningHttpService.getInstance().listLNtransactions());
		ServletUtils.write(apivo, response);
	}	
	/**
	 * 获取所有服务器节点
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/listServerNodes", method = RequestMethod.GET)
	@ResponseBody
	public void listinvoices(HttpServletRequest request, HttpServletResponse response) {
		APIVo apivo = new APIVo();		
		try {
			apivo.setResult(PythonClightningHttpService.getInstance().getListNode().toString());
			apivo.setSuccess(true);
		} catch (Exception e) {
			apivo.setCode(-1);
			apivo.setMsg(e.getMessage());
		}
		ServletUtils.write(apivo, response);		
	}		
	
	@RequestMapping(value = "/addInvoice", method = RequestMethod.POST)
	@ResponseBody
	public void addInvoice(HttpServletRequest request,@ModelAttribute Invoice invoice, HttpServletResponse response) {	
		APIVo apivo = new APIVo();	
		try {
			JsonObject re = PythonClightningHttpService.getInstance().invoiceAPI(invoice);
			if(re.get("code").getAsBoolean()){
				System.out.println(" :"+re.toString());
				String bolt11 = re.get("data").getAsJsonObject().get("bolt11").getAsString();
				apivo.setSuccess(true);	
				apivo.setResult(bolt11);
			}else{
				apivo.setMsg(re.get("err").getAsString());
			}			
		} catch (Exception e) {
			apivo.setCode(-1);
			apivo.setMsg(e.getMessage());
		}

		ServletUtils.write(apivo, response);
	}	
	
	
	@RequestMapping(value = "/wallet/{userName}", method = RequestMethod.GET)
	@ResponseBody
	public void getWallet(@PathVariable("userName") String userName,HttpServletResponse response) {
		APIVo apivo = new APIVo();	
		System.out.println("User Id : " + userName);
		try {
			apivo.setSuccess(true);
			apivo.setResult(BitcoinRPCService.getInstance().getbalance("jiangzhiwei"));
		} catch (Exception e) {
			apivo.setCode(-1);
			apivo.setMsg(e.getMessage());
		}		
		ServletUtils.write(apivo, response);
	}		
	
}
