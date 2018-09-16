package com.ssm.manager.service;

import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.jdbc.StringUtils;
import com.ssm.manager.pojo.Invoice;
import com.ssm.manager.service.common.CommonEnum;

import sun.misc.BASE64Encoder;

public class ClightningChargeHttpService {
	
	static ClightningChargeHttpService one = new ClightningChargeHttpService();
	
	public static ClightningChargeHttpService getInstance(){
		return one;
	}
	/**
	 * post请求
	 * 
	 * @param url
	 * @param json
		curl http://127.0.0.1:9112/info?api-token=mememe -u api-token:mememe
		{"id":"03853aae7448f14f67823a57c9f104054349d077ebe96c125ec20a715cdbe0e07c","port":9735,"address":[],"version":"v0.5.2-2016-11-21-2512-gb0c2e3c","blockheight":1292779,"network":"testnet"}r
	 * @return
	 */

	public static JsonObject doPost(String url, String jsonstr) {

		
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
		
		/**
        // 设置BasicAuth  
        CredentialsProvider provider = new BasicCredentialsProvider();  
        // Create the authentication scope  
        AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);  
        // Create credential pair，在此处填写用户名和密码  
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("eclair-cli", "changeit");  
        // Inject the credentials  
        provider.setCredentials(scope, credentials);  
        // Set the default credentials provider  
        httpClientBuilder.setDefaultCredentialsProvider(provider); 		
        
        **/
		
		String authUserAndPassword = "api-token:mememe";
		HttpClient client = httpClientBuilder.build();

		HttpPost post = new HttpPost(url);
		JsonParser parser = new JsonParser();
		JsonObject response = null;
		try {
			
			if(StringUtils.isNullOrEmpty(jsonstr)){
			}else{
				StringEntity s = new StringEntity(jsonstr);
				//s.setContentEncoding("UTF-8");
				//s.setContentType("application/json");// 发送json数据需要设置contentType
				post.setHeader("Content-Type", "application/json;charset=UTF-8");             
				post.setEntity(s);					
			}
			post.addHeader("Authorization", "Basic " + new BASE64Encoder().encode(authUserAndPassword.getBytes()));
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == 200 || res.getStatusLine().getStatusCode() == 201) {
				String result = EntityUtils.toString(res.getEntity());// 返回json格式：
				response = parser.parse(result).getAsJsonObject();
			}else{
				System.err.println(res);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}
	
	
	
	public static JsonObject doGet(String url) {

		
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
		
		
		String authUserAndPassword = "api-token:mememe";
		HttpClient client = httpClientBuilder.build();

		HttpGet get = new HttpGet(url);
		JsonParser parser = new JsonParser();
		JsonObject response = null;
		try {
			get.addHeader("Authorization", "Basic " + new BASE64Encoder().encode(authUserAndPassword.getBytes()));
			HttpResponse res = client.execute(get);
			if (res.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(res.getEntity());// 返回json格式：
				response = parser.parse(result).getAsJsonObject();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}	

	public static JsonObject getNodeInfo(){
		JsonObject re =  ClightningChargeHttpService.doGet("http://"+CommonEnum.SELLER.getDesc()+":9112/info");
		System.out.println(re);
		return re;
	}
	
	

	
	public  JsonObject getCreatInvoice(Invoice invoice){
	
		Gson gson = new Gson();
		if(StringUtils.isNullOrEmpty(invoice.getDescription())){
			invoice.setDescription(" from jiangzhiwei payment project , 票据生成时间: "+Calendar.getInstance().getTime());
		}else{
			invoice.setDescription(invoice.getDescription() + " | 票据生成时间: "+Calendar.getInstance().getTime());
		}	
		JsonObject re =  ClightningChargeHttpService.doPost("http://"+CommonEnum.SELLER.getDesc()+":9112/invoice",gson.toJson(invoice));
		System.out.println(re);
		return re;
	}	
	
	public static void main(String[] args) throws Throwable {
		EclairPostJson epj = new EclairPostJson();	
		//getinfo
		//epj.setMethod("getinfo");
		//epj.setJsonrpc("2.0");
		//epj.setParams(new ArrayList());
		//Gson gson = new Gson();
		//System.out.println(ClightningHttpService.doPost("http://47.98.167.148:8080/", gson.toJson(epj)));;
		ClightningChargeHttpService.getNodeInfo();
//		Invoice invoice = new Invoice();
//		invoice.setMsatoshi(1000);
//		invoice.setExpiry(3600 * 30);
//		ClightningChargeHttpService.getInstance().getCreatInvoice(invoice);
	}
}
