package com.ssm.manager.service.common;

public enum CommonEnum {
	
	
	SELLER("结算服务器",10,"118.31.59.128"),
	
	BUYER("钱包服务器",20,"116.62.194.136"),
	
	SUCCESS("默认用户",1000,"权限分组"),
	
	TEST_PCLIGHTNING_PASS("passwd",0, "2124");
	

	private String key;
	private int value;
	private String desc;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private CommonEnum(String key, int value, String desc) {
		this.key = key;
		this.value = value;
		this.desc = desc;
	}

	public String getKey() {
		return key;
	}

	public int getValue() {
		return value;
	}



	


}
