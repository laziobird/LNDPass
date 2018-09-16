package com.ssm.manager.service;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ssm.manager.pojo.RmbToUsd;

@SuppressWarnings("deprecation")
public class WebHttpService {

	static WebHttpService one = new WebHttpService();
	
	static RmbToUsd rmbToUsd = new RmbToUsd();

	public static WebHttpService getInstance() {
		return one;
	}
	
	public static RmbToUsd getRmbToUsd() {
		return rmbToUsd;
	}	

	public static String doPost(String url, Map<String, String> key) {

		HttpClient client = getHttpClient();
		HttpPost post = new HttpPost(url);
		String response = null;
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();  
	        if(key!=null){  
	            for (Entry<String, String> entry : key.entrySet()) {  
	                //添加参数
	                params.add( new BasicNameValuePair(entry.getKey(),entry.getValue()) );  
	            }         
	        }			
			post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == 200 || res.getStatusLine().getStatusCode() == 201) {
				String result = EntityUtils.toString(res.getEntity());// 返回json格式：
				//System.out.println(result);
				return result;
			} else {
				System.err.println(res);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}

	private static CloseableHttpClient getHttpClient() {
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
		ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
		registryBuilder.register("http", plainSF);
		// 指定信任密钥存储对象和连接套接字工厂
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			// 信任任何链接
			TrustStrategy anyTrustStrategy = new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
					return true;
				}
			};
			SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy)
					.build();
			LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			registryBuilder.register("https", sslSF);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		Registry<ConnectionSocketFactory> registry = registryBuilder.build();
		// 设置连接管理器
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
		// connManager.setDefaultConnectionConfig(connConfig);
		// connManager.setDefaultSocketConfig(socketConfig);

		
        RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();  
        
		// 构建客户端
		return HttpClientBuilder.create().setConnectionManager(connManager).setDefaultRequestConfig(globalConfig) .build();
  
	}

	public static String doGet(String url) {

		HttpClient client = getHttpClient();

		HttpGet get = new HttpGet(url);

		//String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36";

        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0"); // 设置请求头消息User-Agent		
		

		//get.setHeader("User-Agent", userAgent);

		try {
			HttpResponse res = client.execute(get);
			System.out.println(res.getStatusLine().getStatusCode());
			if (res.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(res.getEntity());// 返回json格式：
				//System.out.println(result);
				return result;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return "";
	}

	
    
    public static void parserInvestHtml(String content) throws ParseException, IOException {
    	System.out.println("======================Invest.com==============================");
    	 
        Document doc = Jsoup.parse(content);
        Elements links = doc.select("div[class=top bold inlineblock]");
        for (Element e : links) {
        	String price = e.getElementById("last_last").text();
            System.out.println(price);
            rmbToUsd.setInvestPrice(price);
          
        	String chaJia = e.select("span[class=arial_20 greenFont   pid-961728-pc]").get(0).text();
            System.out.println(chaJia);
            rmbToUsd.setInvestChaJia(chaJia);
            
        	String chaBi = e.select("span[class=arial_20 greenFont  pid-961728-pcp parentheses]").get(0).text();
            System.out.println(chaBi);           
            rmbToUsd.setInvestChaBi(chaBi);
            System.out.println("=============================================================");
        }
        System.out.println("=============================================================");        
        Elements links2 = doc.select("span[class=lighterGrayFont noBold]");
        for (int i = 0;i< links2.size();i++) {
        	Element e = links2.get(i);
        	Element b = e.nextElementSibling();
        	if(i==0){
        		String zuoTian = b.text();
        		rmbToUsd.setInvestZuoTian(zuoTian);
        		System.out.println(zuoTian); 
        	}
        	if(i==1){
        		
        	}
        	if(i==2){
        		String low = b.select("span[class=inlineblock pid-961728-low]").get(0).text();
        		String high = b.select("span[class=inlineblock pid-961728-high]").get(0).text();
        		System.out.println(low+"-"+high); 
        		rmbToUsd.setInvestHigh(high);
        		rmbToUsd.setInvestLow(low);
        	}
        	
        }
        
        System.out.println("=============================================================");        
        
    }	
    
    
    
    
    public static void parserHbscHtml(String content) throws ParseException, IOException {
    	System.out.println("======================Hbsc==============================");
    	 
        Document doc = Jsoup.parse(content);
        Element link = doc.select("tr[class=hsbcTableRow03]").get(1);
      
        String buy  = link.select("td[class=ForRatesColumn02]").get(1).text();
        String sell  = link.select("td[class=ForRatesColumn02]").get(3).text();    
        rmbToUsd.setHbscSell(sell);
        rmbToUsd.setHbscBuy(buy);
        
        System.out.println(sell+"-"+buy); 
        
        System.out.println("=============================================================");        
        
    }    
    
    
    public static void parserBankofchina(String content) throws ParseException, IOException {
    	System.out.println("=====================BankOfChina=================================");
    	 
        Document doc = Jsoup.parse(content);
        Element links = doc.select("div[class=BOC_main publish]").get(0).select("tr").get(1);
        Element links2 = links.select("td").get(3);
        
        String sell = links2.text();
        Float sellf = Float.parseFloat(sell)/100;
        rmbToUsd.setBankofchinaSell(sellf.toString());
        System.out.println(sellf);
       
        
    }	    
	
	
	public static void main(String[] args) throws Throwable {

		
		
		Map<String, String> key = new HashMap<String,String>();
		key.put("pjname", "1316");
        System.out.println(key);
		String bankofChina = WebHttpService.getInstance().doPost("http://srh.bankofchina.com/search/whpj/search.jsp", key);
		parserBankofchina(bankofChina);

	}
}
