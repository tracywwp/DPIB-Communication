package com.spdb.ib.dpib.netty.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.spdb.ib.dpib.logs.LogManager;

/**
 * nio client
 * 
 * @version 1.0
 * @author T-wuwp
 * 
 */
public class Client extends Thread {
	private String sendMsg;
	private int port;
	private String ip;

	public Client() {
	}

	public Client(String sendMsg, int port, String ip) {
		this.sendMsg = sendMsg;
		this.port = port;
		this.ip = ip;
	}

	public void run() {
		Socket client = null;
		DataOutputStream out = null;
		DataInputStream in = null;
		try {
			client = new Socket(ip, port);
			// client.setSoTimeout(10000);
			out = new DataOutputStream((client.getOutputStream()));
			String query = sendMsg;
			byte[] request = sendMsg.getBytes();
			out.write(request);
			out.flush();
			client.shutdownOutput();
			in = new DataInputStream(client.getInputStream());
			// 接受服务器的反馈
			BufferedReader br = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			System.out.println(br.read());
			String msg = null;
			System.out.println("从服务器获取数据：" + br.readLine());
			 while ((msg = br.readLine()) != null) {
			 System.out.println("从服务器获取数据：" + msg);
			 }
			in.close();
			out.close();
			client.close();
		} catch (Exception e) {
			LogManager.info("从服务器获取数据失败.......：" + e.getMessage());
		}
	}

}
