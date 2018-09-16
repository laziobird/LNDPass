package com.ssm.manager.service;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import sun.misc.BASE64Encoder;

public class EclairHttpService {
	

	/**
	 * post请求
	 * 
	 * @param url
	 * @param json
		curl --user :changeit -X POST \
		  http://127.0.0.1:8080/ \
		  -H 'content-type: application/json' \
		  -d '{
		    "jsonrpc": "2.0",
		    "method": "connect",
		    "params": [
		      "03489fd6d2a6d757b91ffcb327942ce43f3c921f52879f98c8066e0474994973a3@120.27.124.61:9735"
		    ]
		}'
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
		
		String authUserAndPassword = ":changeit";
		HttpClient client = httpClientBuilder.build();

		HttpPost post = new HttpPost(url);
		JsonParser parser = new JsonParser();
		JsonObject response = null;
		try {
			StringEntity s = new StringEntity(jsonstr);
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");// 发送json数据需要设置contentType
			post.setEntity(s);		
			post.addHeader("Authorization", "Basic " + new BASE64Encoder().encode(authUserAndPassword.getBytes()));
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(res.getEntity());// 返回json格式：
				response = parser.parse(result).getAsJsonObject();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}

	public static void main(String[] args) throws Throwable {
		EclairPostJson epj = new EclairPostJson();
		
		
		//getinfo
		//epj.setMethod("getinfo");
		//epj.setJsonrpc("2.0");
		//epj.setParams(new ArrayList());
		
		epj.setMethod("allnodes");
		epj.setJsonrpc("2.0");
		epj.setParams(new ArrayList());		
		
		
		
		Gson gson = new Gson();
		System.out.println(EclairHttpService.doPost("http://47.98.167.148:8080/", gson.toJson(epj)));;
	}
}
