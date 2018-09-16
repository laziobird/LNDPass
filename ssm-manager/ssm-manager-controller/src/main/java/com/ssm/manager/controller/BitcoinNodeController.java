package com.ssm.manager.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssm.manager.service.BitcoinRPCService;
import com.ssm.manager.service.PythonClightningHttpService;

@Controller
public class BitcoinNodeController {

	@RequestMapping("/bitcoinAPI")
	public String donate(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		model.addAttribute("balance", BitcoinRPCService.getInstance().getbalance(""));
		model.addAttribute("peers", PythonClightningHttpService.getInstance().getListPeers().toString());		
		return "paasapi";
	}
	
	
	@RequestMapping("/bitcoinAPI/walletInfo")
	public String walletInfo(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		model.addAttribute("wallet", BitcoinRPCService.getInstance().getwalletinfo());
		return "paasapiwallet";
	}
	
	@RequestMapping("/bitcoinAPI/miningInfo")
	public String miningInfo(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		model.addAttribute("mining", BitcoinRPCService.getInstance().getmininginfo());
		return "paasapimining";
	}	
	
	@RequestMapping("/bitcoinAPI/addressInfo")
	public String addressInfo(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		//所有地址
		model.addAttribute("alladdress", BitcoinRPCService.getInstance().getaddressesbyaccount(""));
		String account = "jiangzhiwei";
		model.addAttribute("address", BitcoinRPCService.getInstance().getaddressesbyaccount(account));
		model.addAttribute("account", account);
		return "paasapiaddress";
	}
	
	@RequestMapping("/bitcoinAPI/nodesInfo")
	public String nodesInfo(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());
		//所以地址
		model.addAttribute("nodes", PythonClightningHttpService.getInstance().getListNode());
		return "paasapinodes";
	}	
	

}
