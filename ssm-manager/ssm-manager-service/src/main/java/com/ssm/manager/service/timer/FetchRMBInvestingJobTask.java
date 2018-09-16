package com.ssm.manager.service.timer;

import com.ssm.manager.service.WebHttpService;

public class FetchRMBInvestingJobTask implements Runnable {

	@Override
	public void run() {
		
		try {
			System.out.println("---------------FetchRMBInvestingJobTask start !--------------");
			WebHttpService instance = WebHttpService.getInstance();
			String investing =instance.doGet("https://cn.investing.com/currencies/usd-cnh");	
			System.out.println(investing);
			instance.parserInvestHtml(investing);			
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	

	}

}
