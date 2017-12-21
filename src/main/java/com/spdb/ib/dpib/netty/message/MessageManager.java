package com.spdb.ib.dpib.netty.message;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.springframework.core.task.TaskExecutor;

import com.spdb.ib.dpib.logs.LogManager;
import com.spdb.ib.dpib.process.manager.ProcessManager;

/**
 * 消息存、取管理类
 * 
 * @version 1.0
 * @date 19 5 2014
 * @author T-wuwp
 * 
 */
public class MessageManager {

	public int queenPool = 0;
	/**
	 * 存储客户端消息队列
	 */
	public static LinkedBlockingQueue<MessagePack> receQueen;
	private int reStartThreadCount = 0;
	/**
	 * 创建线程池类
	 */
	private TaskExecutor taskExecutor;

	public MessageManager() {
		if (queenPool == 0) {
			receQueen = new LinkedBlockingQueue<MessagePack>(
					ProcessManager.queuePool);
		} else {
			receQueen = new LinkedBlockingQueue<MessagePack>(queenPool);
		}

	}

	public void start() {
		taskExecutor.execute(new PushRecvThread());
	}

	/**
	 * 读取消息队列数据线程
	 * 
	 * @author T-wuwp
	 * 
	 */
	private class PushRecvThread implements Runnable {
		public void run() {
			for (int i = 0; i <= 10; i++) {
				taskExecutor.execute(new MessageThread());
			}
		}

		class MessageThread implements Runnable {
			public void run() {
				while (true) {
					final MessagePack message = waitForProcessMessage();
					ChannelFuture future = null;
					try {
						if (message != null) {
							// 利用多态执行继承MessagePack的子类方法
							String response = message.onHandler(message);
							future = message.channel.write(response + "\r\n");
							future.addListener(new ChannelFutureListener(){

								@Override
								public void operationComplete(ChannelFuture arg0)
										throws Exception {
									try {
										// 正常完成后继续执行的业务
										message.afterReplyDone();
									} catch (Exception e) {
										message.replyException(e);
									} finally {
										message.channel.close();
									}
								}
								
							});
						}
					} catch (Exception e) {
						String errorMessage = e.getMessage().toString();
						future = message.channel.write(errorMessage);
						future.addListener(ChannelFutureListener.CLOSE);
						LogManager.error(errorMessage);
					}
				}
			}
		}
	}

	/**
	 * 从消息队列获取消息
	 * 
	 * @return
	 */
	public MessagePack waitForProcessMessage() {
		MessagePack message = null;
		while (message == null) {
			try {
				// 从队列中取继承MessagePack的实例
				message = receQueen.take();
			} catch (InterruptedException e) {
				// TODO log
			}
		}
		return message;
	}

	/**
	 * 往消息队列存消息
	 * 
	 * @param message
	 * @return
	 */
	public void addSocketMessage(MessagePack message) {
		if (message != null) {
			try {
				boolean success = receQueen.offer(message, 1,
						TimeUnit.MILLISECONDS);
				if (false == success) {
					// maybe PushRecvThread is break,restart the thread again
					if (reStartThreadCount < 10) {
						taskExecutor.execute(new PushRecvThread());
						reStartThreadCount++;
					}
				} else {
				}
			} catch (InterruptedException e) {
				// TODO log
			}
		}
		return;
	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

}
