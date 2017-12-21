package com.spdb.ib.dpib.netty.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.WriteCompletionEvent;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.handler.timeout.ReadTimeoutException;

import com.spdb.ib.dpib.logs.LogManager;
import com.spdb.ib.dpib.netty.gate.bean.TaskBeanFactory;
import com.spdb.ib.dpib.netty.message.MessageManager;

/**
 * netty服务端处理接受数据，发送数据handler类，生命周期类
 * 
 * @version 1.0
 * @date 19 5 2014
 * @author T-wuwp
 * 
 */
@ChannelHandler.Sharable
public class PushServerHandler extends SimpleChannelUpstreamHandler {

	// 为了监控添加的监控变量
	public static final AtomicInteger curr_conns = new AtomicInteger(0);

	// 为了监控添加的监控变量
	public static final AtomicInteger receiveCount = new AtomicInteger(0);

	// 为了监控添加的监控变量
	public static final AtomicInteger authCount = new AtomicInteger(0);

	/**
	 * 消息管理类
	 */
	private MessageManager messageManager;
	private DefaultChannelGroup channelGroup;
	private ServerSocketChannelFactory channelFactory;
	public static String requestMessage = "";
	private int port;
	private String clientIp;
	private int clientPort;

	public PushServerHandler() {
	}

	public PushServerHandler(DefaultChannelGroup channelGroup,
			ServerSocketChannelFactory channelFactory) {
		this.channelGroup = channelGroup;
		this.channelFactory = channelFactory;
	}

	public PushServerHandler(DefaultChannelGroup channelGroup,
			MessageManager messageManager,
			ServerSocketChannelFactory channelFactory, int port) {
		this.channelGroup = channelGroup;
		this.messageManager = messageManager;
		this.channelFactory = channelFactory;
		this.port = port;
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof ChannelStateEvent) {
			LogManager.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}

	/**
	 * On open we manage some statistics, and add this connection to the channel
	 * group.
	 * 
	 * @param channelHandlerContext
	 * @param channelStateEvent
	 * @throws Exception
	 */
	@Override
	public void channelOpen(ChannelHandlerContext channelHandlerContext,
			ChannelStateEvent channelStateEvent) throws Exception {
		// 以原子方式将当前值加 1
		curr_conns.incrementAndGet();
		channelGroup.add(channelHandlerContext.getChannel());
		channelHandlerContext.sendUpstream(channelStateEvent);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		InetSocketAddress insocket = (InetSocketAddress) ctx.getChannel()
				.getRemoteAddress();
		this.clientIp = insocket.getAddress().getHostAddress();
		this.clientPort = insocket.getPort();
		System.out.println("Welcome to "
				+ InetAddress.getLocalHost().getHostName() + " "
				+ InetAddress.getLocalHost().getHostAddress());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		try {
			if (!ctx.getChannel().isConnected()) {
				LogManager
						.warn("Failed to write any response because the channel is not connected any more. Maybe the client has closed the connection? ");
				return;
			}
			System.out.println(e.getMessage());
			byte[] command = (byte[])e.getMessage();
			System.out.println("sendc length "+command.length);
			System.out.println(command[0]+" "+command[1]);
			ChannelFuture future=e.getChannel().write(command);
			e.getChannel().close();
			future.addListener(ChannelFutureListener.CLOSE);
//			messageManager.addSocketMessage(TaskBeanFactory.handleMessage(
//					command, e, channelGroup,
//					channelFactory, port, clientIp, clientPort));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * On close we manage some statistics, and remove this connection from the
	 * channel group.
	 * 
	 * @param channelHandlerContext
	 * @param channelStateEvent
	 * @throws Exception
	 */
	@Override
	public void channelClosed(ChannelHandlerContext channelHandlerContext,
			ChannelStateEvent channelStateEvent) throws Exception {
		channelGroup.remove(channelHandlerContext.getChannel());
		requestMessage = "";
	}

	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		ctx.sendDownstream(e);
	}

	public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e)
			throws Exception {

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		// 对读取超时异常进行判断
		if (e.getCause() instanceof ReadTimeoutException) {
			// 关闭连接
			e.getChannel()
					.write("<Envelope><Header><H1>5168168</H1><H2>1001</H2><H3>9999</H3><H4>链接超时</H4></Header></Envelope>");
			e.getChannel().close().awaitUninterruptibly();
			e.getFuture().addListener(ChannelFutureListener.CLOSE);
		} else {
			LogManager.error(Level.WARNING
					+ "Unexpected exception from downstream." + e.getCause());
			e.getChannel().close();
		}
	}

	public MessageManager getMessageManager() {
		return messageManager;
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}

}
