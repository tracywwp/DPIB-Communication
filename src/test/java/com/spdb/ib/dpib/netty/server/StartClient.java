package com.spdb.ib.dpib.netty.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.spdb.ib.dpib.logs.LogManager;

public class StartClient {

	/**
	 * 连接服务端
	 * 
	 * @param msg
	 * 
	 */
	public static void startClient(String msg, String ip, int port) {
		try {

			long start = System.currentTimeMillis();
			Client client = new Client(msg, port, ip);
			client.start();
			long end = System.currentTimeMillis();
			System.out.println("time:" + (end - start) / 1000 + " 发送的消息：" + msg);
		} catch (Exception e) {
			LogManager.error("连接服务器失败......");
		}
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		// TODO Auto-generated method stub
		Socket socket = new Socket("127.0.0.1",8848);
		socket.setSoTimeout(3 * 60 * 1000);
		InputStream input = socket.getInputStream();
		OutputStream output = socket.getOutputStream();
		byte[] testByte = new byte[5];
		testByte[0]=1;
		testByte[1]=2;
		testByte[2]=3;
		testByte[3]=4;
		output.write(testByte);
	
		byte[] testByte2 = new byte[2];
		input.read(testByte2);
		System.out.println("0="+testByte2[0]+"1="+testByte2[1]);
		socket.close();
	}
}
