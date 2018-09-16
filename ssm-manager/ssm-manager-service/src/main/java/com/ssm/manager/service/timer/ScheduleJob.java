package com.ssm.manager.service.timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleJob {
	
	public static void start(){
		ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
		long delay = 10;
		long interval = 60*15;
		//每隔 15分钟执行一次
		service.scheduleAtFixedRate(new FetchRMBJobTask(), delay, interval, TimeUnit.SECONDS);		
		service.scheduleAtFixedRate(new FetchRMBInvestingJobTask(), delay+5, interval, TimeUnit.SECONDS);	
	}
	
	
	
	public static void main(String[] args) {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
		long delay = 10;
		long interval = 60*15;
		//每隔 15分钟执行一次
		service.scheduleAtFixedRate(new FetchRMBJobTask(), delay, interval, TimeUnit.SECONDS);
	}

}
