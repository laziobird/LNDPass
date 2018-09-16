package com.ssm.manager.service.timer;

import java.util.HashMap;
import java.util.Map;

import com.ssm.manager.service.WebHttpService;

public class FetchRMBJobTask implements Runnable {

	@Override
	public void run() {
		
		try {
			System.out.println("---------------FetchRMBJobTask start !--------------");
			WebHttpService instance = WebHttpService.getInstance();

			Map<String, String> key = new HashMap<String,String>();
			key.put("pjname", "1316");
	        System.out.println(key);
			String bankofChina = instance.doPost("http://srh.bankofchina.com/search/whpj/search.jsp", key);
			instance.parserBankofchina(bankofChina);	
			
			
			String hbsc = WebHttpService.getInstance().doGet("https://www.hsbc.com.cn/1/2/!ut/p/c5/04_SB8K8xLLM9MSSzPy8xBz9CP0os3gDd-NQv1BDg2AXA1-PEE_zICNDAwgAykdiyrsi5InQ7ezu6GFi7gPkh3m6GniaOJkYmPq6GRp4GhPQHVyVEe_sp-_nkZ-bql-QGxpR7qioCAAh9cxV/dl3/d3/L0lDU0lKSWdrbUNTUS9JUFJBQUlpQ2dBek15cXpHWUEhIS80QkVqOG8wRmxHaXQtYlhwQUh0Qi83XzBHM1VOVTEwU0QwTUhUSTdIRU8wMDAwMDAwLzVBcFJrODg0NjAwMDMvc2Eu/");	
			instance.parserHbscHtml(hbsc);	
			
			System.out.println(instance.getRmbToUsd().toString());
			
						
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	

	}

}
