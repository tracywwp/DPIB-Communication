package com.spdb.ib.dpib.netty.client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.spdb.ib.dpib.logs.LogManager;

/**
 * 客户端
 * 
 * @version 1.0
 * @date 05 6 2014
 * @author T-wuwp
 * 
 */
public class NettyClient {
	/**
	 * 服务端ip
	 */
	private String host;
	/**
	 * 服务端端口
	 */
	private int port;
	/**
	 * 发送消息后缀
	 */
	private final String buffer = "\r\n";
	/**
	 * 发送服务端消息
	 */
	private String message = "";
	/**
	 * 获取服务端消息
	 */
	public static String responseMessage = "";
	public static String delayMessage=null;
	public NettyClient() {
	}

	/**
	 * 构造器
	 * 
	 * @param host
	 * @param port
	 * @param message
	 */
	public NettyClient(String host, int port, String message) {
		this.host = host;
		this.port = port;
		this.message = message;
	}

	public String StartClient() {
		Channel channel = null;
		ClientBootstrap bootstrap = null;
		try {
			LogManager.info("客户端开始启动并连接服务器..........");
			// 构建客户端
			bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
					Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool()));

			// 绑定客户端数据读写类
			bootstrap.setPipelineFactory(new PushClientPipelineFactory(
					new NettyClient()));

			// 开始连接服务端
			ChannelFuture future = bootstrap.connect(new InetSocketAddress(
					host, port));
           
			// 等待连接服务端，确定是否成功和失败
			channel = future.awaitUninterruptibly().getChannel();
			if (!future.isSuccess()) {
				future.getCause().printStackTrace();
				bootstrap.releaseExternalResources();
				LogManager.info("客户端未连接到服务器,请查看ip,端口号是否正确.......");
				channel.close().awaitUninterruptibly();
				// 关闭所有线程池并退出
				bootstrap.releaseExternalResources();
				return "0";
			}

			LogManager.info("客户端连接服务器成功..........");
			ChannelFuture lastWriteFuture = null;
			while (true) {
				LogManager.info("客户端向服务器发送数据:" + message);
				lastWriteFuture = channel.write(message + buffer);
				channel.getCloseFuture().awaitUninterruptibly();
				break;
			}
			// 等待信息发送成功后再关闭连接
			if (lastWriteFuture != null) {
				lastWriteFuture.awaitUninterruptibly();
			}
			if(null!=delayMessage&&delayMessage.length()>0){
				responseMessage=delayMessage;
			}
			LogManager.info("client responseMessage:" + responseMessage);
			return responseMessage;

		} catch (Exception e) {
			LogManager.info("客户端并连接服务器发生异常,请查看ip,端口号是否正确.......");
			channel.close().awaitUninterruptibly();
			bootstrap.releaseExternalResources();
		} finally {
			// 关闭连接
			channel.close().awaitUninterruptibly();
			// 关闭线程池并推出
			bootstrap.releaseExternalResources();
			responseMessage = "";
		}
		return null;

	}

}
