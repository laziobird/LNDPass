package com.ssm.manager.controller;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.ssm.manager.service.timer.ScheduleJob;

@Service
public class StartServerListener implements ApplicationListener<ContextRefreshedEvent>{

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		ScheduleJob.start();		
	}

}
