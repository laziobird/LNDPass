package com.ssm.manager.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ssm.manager.service.WebHttpService;

@Controller
@RequestMapping("/tool")
public class ToolController {


	
	@RequestMapping(value="/usdList",method = RequestMethod.GET)
	public String login(HttpServletRequest request,Model model) {
		model.addAttribute("contextPath", request.getContextPath());	
		model.addAttribute("usdList", WebHttpService.getRmbToUsd());	
	    return "tool/usdList";  
	    
	}	

	

	

	
	

}
