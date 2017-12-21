package com.spdb.ib.dpib.netty.spring.start.server;

import org.springframework.core.task.TaskExecutor;

import com.spdb.ib.dpib.logs.LogManager;
import com.spdb.ib.dpib.netty.spring.server.SpringNettyServer;

public class StartServer {
	private TaskExecutor taskExecutor;
	private SpringNettyServer springServer;
	private String port;

	public void startSpringServer() {
		try {
			Thread t = new Thread(springServer);
			springServer.setPort(Integer.parseInt(port));
			taskExecutor.execute(t);
			LogManager.info("Server starting success...");
		} catch (Exception e) {
			e.printStackTrace();
			LogManager.error("Server starting fail...");
		}
	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public SpringNettyServer getSpringServer() {
		return springServer;
	}

	public void setSpringServer(SpringNettyServer springServer) {
		this.springServer = springServer;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

}
