package com.ssm.manager.service;

import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ssm.manager.pojo.Invoice;
import com.ssm.manager.service.common.CommonEnum;

public class PythonClightningHttpService {
	/**
	 * clightning的python实现
	 * https://github.com/ElementsProject/lightning/tree/master/contrib/pylightning
	 * 启动HTTP Service 通过http 访问服务
	 */
	static PythonClightningHttpService one = new PythonClightningHttpService();
	//Server 对应卖家Node
	static final String SERVER_URL = "http://"+CommonEnum.SELLER.getDesc()+":9100";
	//对应买家Node
	static final String BUYER_URL = "http://"+CommonEnum.BUYER.getDesc()+":9100";
	
	public static PythonClightningHttpService getInstance() {
		return one;
	}

	public static JsonObject doPost(String url, String jsonstr) {

		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		HttpClient client = httpClientBuilder.build();
		HttpPost post = new HttpPost(url);
		JsonParser parser = new JsonParser();
		JsonObject response = null;
		try {

			if (StringUtils.isEmpty(jsonstr)) {
			} else {
				StringEntity s = new StringEntity(jsonstr);
				post.setHeader("Content-Type", "application/json;charset=UTF-8");
				post.setEntity(s);
			}
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == 200 || res.getStatusLine().getStatusCode() == 201) {
				String result = EntityUtils.toString(res.getEntity());// 返回json格式：
				response = parser.parse(result).getAsJsonObject();
			} else {
				System.err.println(res);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return response;
	}

	public static JsonObject doGet(String url) {

		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		HttpClient client = httpClientBuilder.build();
		HttpGet get = new HttpGet(url);
		JsonParser parser = new JsonParser();
		JsonObject response = null;
		try {
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
	/**
	 * 获取all 闪电节点
	 * @return
	 */
	public JsonObject getListNode() {
		JsonObject re = PythonClightningHttpService.doGet(SERVER_URL+"?method=listinvoices&passwd=2124");		
		JsonObject reRemoteBuyer = PythonClightningHttpService
				.doGet(BUYER_URL+"?method=listinvoices&passwd=2124");
		System.out.println("reRemoteBuyer:" + reRemoteBuyer);
		JsonObject all = new JsonObject();
		all.add("WalletNode", reRemoteBuyer);
		all.add("PAASNode", re);
		System.out.println("getListNode: "+all);
		return all;
	}

	public JsonObject getListPeers() {
		JsonObject re = PythonClightningHttpService.doGet("http://127.0.0.1:9100?method=listpeers&passwd=2124");
		System.out.println(re);
		JsonObject all = new JsonObject();
		all.add("JiangZhiwei@PAAS Channel Info", re);
		return all;
	}

	public JsonObject invoice(Invoice invoice) {
		/**
		 * if (StringUtils.isEmpty(invoice.getDescription())) {
		 * invoice.setDescription(" from jiangzhiwei payment project , 票据生成时间: "
		 * + Calendar.getInstance().getTime()); } else { }
		 **/
		JsonObject re = new JsonObject();
		if (invoice.getMsatoshi() > 10000) {
			re.addProperty("err", "money is too large <=10000");
			return re;
		}
		invoice.setDescription(StringUtils.trimAllWhitespace(Calendar.getInstance().getTime().toString()));
		String url = SERVER_URL+"?method=invoice&" + CommonEnum.TEST_PCLIGHTNING_PASS.getKey() + "="
				+ CommonEnum.TEST_PCLIGHTNING_PASS.getDesc() + "&content=" + invoice.getDescription().trim()
				+ "&bmount=" + invoice.getMsatoshi();
		System.out.println(url);
		re = PythonClightningHttpService.doGet(url);
		System.out.println(re);
		return re;
	}
	/**
	 * 卖家Node生成付款票据
	 * @param invoice
	 * @return
	 */
	public JsonObject invoiceAPI(Invoice invoice) {
		JsonObject re = new JsonObject();
		re.addProperty("code", false);
		if (invoice.getMsatoshi() > 10000) {
			re.addProperty("err", "money is too large <=10000");
			return re;
		}
		invoice.setDescription(StringUtils.trimAllWhitespace(Calendar.getInstance().getTime().toString()));
		String url = SERVER_URL+"?method=invoice&" + CommonEnum.TEST_PCLIGHTNING_PASS.getKey() + "="
				+ CommonEnum.TEST_PCLIGHTNING_PASS.getDesc() + "&content=" + invoice.getDescription().trim()
				+ "&bmount=" + invoice.getMsatoshi();
		re.addProperty("code", true);
		re.addProperty("url", url);
		re.add("data", PythonClightningHttpService.doGet(url));
		return re;
	}

	public JsonObject pay(String bolt11) {
		String url = BUYER_URL+"?method=pay&passwd=2124&bolt11=" + bolt11;
		JsonObject re = PythonClightningHttpService.doGet(url);
		System.out.println(re);
		return re;
	}

	public JsonObject decodepayInvoice(String bolt11) {
		String url = "http://127.0.0.1:9100?method=decodepay&passwd=2124" + "&bolt11=" + bolt11;
		JsonObject re = PythonClightningHttpService.doGet(url);
		System.out.println(re);
		return re;
	}

	public JsonObject listLNtransactions() {
		String url = BUYER_URL+"?method=listpayments&passwd=2124";
		JsonObject re = PythonClightningHttpService.doGet(url);
		System.out.println(re);
		return re;
	}

	public static void main(String[] args) throws Throwable {
		//EclairPostJson epj = new EclairPostJson();
		// getinfo
		// epj.setMethod("getinfo");
		// epj.setJsonrpc("2.0");
		// epj.setParams(new ArrayList());
		// Gson gson = new Gson();
		// System.out.println(ClightningHttpService.doPost("http://47.98.167.148:8080/",
		// gson.toJson(epj)));;
		// ClightningHttpService.getNodeInfo();
		Invoice invoice = new Invoice();
		invoice.setMsatoshi(1000);
		invoice.setExpiry(3600 * 30);
		String t = StringUtils.trimAllWhitespace(Calendar.getInstance().getTime().toString());
		System.out.println(t);
	}
}
