package com.spdb.ib.dpib.netty.server;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.task.TaskExecutor;

import com.spdb.ib.dpib.logs.LogManager;
import com.spdb.ib.dpib.netty.message.MessageManager;
import com.spdb.ib.dpib.process.manager.ProcessManager;

public class StartServer {
	/**
	 * 最大线程池
	 */
	public static ExecutorService es = Executors
			.newFixedThreadPool(ProcessManager.CPU_NUMBER
					* ProcessManager.MAX_THREADS);
	private int port;
	private MessageManager messageManager;
	private TaskExecutor taskExecutor;

	/**
	 * 
	 * @param port
	 *            端口
	 * @param msgConvertFactory
	 *            转换报文接口类
	 * @param messageManager
	 *            消息管理类
	 * @param maxThreads
	 *            线程大小
	 * @param queenPool
	 *            队列大小
	 * @return
	 */
	public static int startNettyServer(int port, MessageManager messageManager,
			int maxThreads, int queenPool) {
		messageManager.queenPool = queenPool;
		try {
			Callable c1 = new NettyServer(port, messageManager);
			if (maxThreads > 0) {
				es = Executors.newFixedThreadPool(ProcessManager.CPU_NUMBER
						* maxThreads);
			}
			// Future future=taskExecutor.submit(c1);
			Future future = es.submit(c1);
			System.out.println("Server starting  port..." + port);
			return Integer.parseInt(future.get().toString());
		} catch (Exception e) {
			LogManager.error("Server starting fail...");
			return 0;
		}

	}

	public static int startNettyServer(int port, int maxThreads, int queenPool) {
		// messageManager.queenPool = queenPool;
		try {
			Callable c1 = new NettyServer(port);
			if (maxThreads > 0) {
				es = Executors.newFixedThreadPool(ProcessManager.CPU_NUMBER
						* maxThreads);
			}
			// Future future=taskExecutor.submit(c1);
			Future future = es.submit(c1);
			System.out.println("Server starting  port..." + port);
			return Integer.parseInt(future.get().toString());
		} catch (Exception e) {
			LogManager.error("Server starting fail...");
			return 0;
		}

	}

	public static void main(String[] args) {
		// ApplicationContext context=new ClassPathXmlApplicationContext(
		// "classpath*:/applicationContext-spring-communication.xml");
		// MessageManager
		// messageManager=(MessageManager)context.getBean("messageManager");
		System.out.println(startNettyServer(8848, 60, 500));
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

}
