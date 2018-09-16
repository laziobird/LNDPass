package com.ssm.manager.pojo;

public class Invoice {

	int msatoshi; //1 msatoshi = 0.00000001 btc 
	int expiry; // 3600 == a hour
	String description;
	public int getMsatoshi() {
		return msatoshi;
	}
	public void setMsatoshi(int msatoshi) {
		this.msatoshi = msatoshi;
	}
	public int getExpiry() {
		return expiry;
	}
	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Invoice [msatoshi=" + msatoshi + ", expiry=" + expiry + ", description=" + description + "]";
	}
	
}
