package com.ssm.manager.service;

import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.mysql.jdbc.StringUtils;

public class BitcoinRPCService {

	final String cred = new String(Base64.getEncoder().encode(("foo" + ":" + "bar").getBytes()));
	
	final String BUYER_URL = "116.62.194.136";
	
	final String SELLER_URL = "118.31.59.128";
	
	Map<String, String> headers = new HashMap<String, String>(1);

	static BitcoinRPCService one = new BitcoinRPCService();

	public static BitcoinRPCService getInstance() {
		return one;
	}

	public BitcoinRPCService() {
		// 身份认证
		headers.put("Authorization", "Basic " + cred);
	}

	Object getBitcoinAPIbyName(String url,String method,Object[] objs) {
		Object re = new Object();
		try {
			JsonRpcHttpClient client = new JsonRpcHttpClient(
					new URL("http://"+url+":18332"), headers);
			re =  client.invoke(method,objs, Object.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}

	
	
	Object getBitcoinBuyerAPIbyName(String method,Object[] objs) {
		Object re = new Object();
		try {
			JsonRpcHttpClient client = new JsonRpcHttpClient(
					new URL("http://"+BUYER_URL+":18332"), headers);
			re =  client.invoke(method,objs, Object.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}

	
	//比特币交易
	public Object listTransactions(){
		return getBitcoinBuyerAPIbyName("listtransactions",  new Object[] {});
	}		
	
	public Object listAccounts(){
		return getBitcoinAPIbyName(SELLER_URL,"listaccounts",  new Object[] {});
	}		
	
	
	public Object addAddressbyAccount(String account){
		return getBitcoinAPIbyName(SELLER_URL,"getnewaddress",  new Object[] {account});
	}		
	
	public Object getmininginfo(){
		return getBitcoinAPIbyName(SELLER_URL,"getmininginfo",  new Object[] {});
	}
	
	public Object getwalletinfo(){
		return getBitcoinAPIbyName(SELLER_URL,"getwalletinfo",  new Object[] {});
	}	
	
	
	
	public Object getaddressesbyaccount(String account){
		if(StringUtils.isNullOrEmpty(account))
			return getBitcoinAPIbyName(SELLER_URL,"getaddressesbyaccount",  new Object[] {""});
		else
			return getBitcoinAPIbyName(SELLER_URL,"getaddressesbyaccount",  new Object[] {account});
		
	}	
	
	public Double getbalance(String account){
		return  (Double)getBitcoinAPIbyName(SELLER_URL,"getbalance",  new Object[] { account });
	}	
	
	public Double getbuyerbalance(String account){
		return  (Double)getBitcoinAPIbyName(BUYER_URL,"getbalance",  new Object[] { account });
	}	

	public static void main(String[] args){
		
//		String result = (String) client.invoke("getblockhash", new Object[] { 1 }, Object.class);
//		System.out.println("blockhash:" + result);
//		System.out.println(
//				"blockhashinfo:" + client.invoke("getblock", new Object[] { result }, Object.class).toString());
//		System.out.println("balance:" + client.invoke("getbalance", new Object[] {}, Object.class));
//
//		Object one = client.invoke("getmininginfo", new Object[] {}, Object.class);
//		System.out.println("getmininginfo:" + one.toString());
//
//		System.out.println("getwalletinfo:" + client.invoke("getwalletinfo", new Object[] {}, Object.class));
//		System.out.println(
//				"服务器钱包:" + client.invoke("getaddressesbyaccount", new Object[] { "" }, Object.class).toString());
	    
		
		//System.out.println(BitcoinRPCService.getInstance().addAddressbyAccount("jiangzhiwei"));
		Object re = BitcoinRPCService.getInstance().getaddressesbyaccount("");
		System.out.println(re);
	}
}
