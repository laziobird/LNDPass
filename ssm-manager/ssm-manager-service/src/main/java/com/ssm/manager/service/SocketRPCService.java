package com.ssm.manager.service;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

import com.ssm.manager.pojo.Invoice;

public class SocketRPCService {
	/**
	 * clightning 基于C INET socket 通信
	 */

	static SocketRPCService one = new SocketRPCService();

	public static SocketRPCService getInstance() {
		return one;
	}

	public  String query(String inputStr, String ip, int port) {
		DataInputStream netInputStream;
		DataOutputStream netOutputStream;
		Socket sc;
		int fileLength;
		fileLength = (int) inputStr.length();
		String str = "";
		try {
			sc = new Socket(ip, port);
			System.out.println("Socket: " + sc.getLocalPort());			
			netInputStream = new DataInputStream(sc.getInputStream());
			netOutputStream = new DataOutputStream(sc.getOutputStream());
			///////////////////// 1.send file length//////////////////////
			netOutputStream.write(Integer.toString(fileLength).getBytes());
			netOutputStream.writeChars(inputStr);

			byte[] reByte = readStream(netInputStream);
			str = new String(reByte);
			System.out.println("str: " + str);
			netInputStream.close();
			netOutputStream.close();
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
			str = e.getMessage();
		}
		return str;
	}

	/**
	 * 读取流
	 * 
	 * @param inStream
	 * @return 字节数组
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}

	
	
	public static void main(String[] args) throws Throwable {
		EclairPostJson epj = new EclairPostJson();
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

	}
}
