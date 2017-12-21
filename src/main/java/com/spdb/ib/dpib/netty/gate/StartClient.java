package com.spdb.ib.dpib.netty.gate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.spdb.ib.dpib.logs.LogManager;
import com.spdb.ib.dpib.netty.client.NettyClient;

public class StartClient {
	/**
	 * 最大线程池
	 */
	public static ExecutorService es = Executors.newFixedThreadPool(200);
	
	public static String startClient(String host, int port, String message) {
		try {
			NettyClient client = new NettyClient(host, port, message);
			LogManager.info("Client starting success...");
			return client.StartClient();
		} catch (Exception e) {
			LogManager.error("Client starting fail...");
			return "0";
		}
	}

}
