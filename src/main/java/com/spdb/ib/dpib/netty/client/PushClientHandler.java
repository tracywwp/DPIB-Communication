package com.spdb.ib.dpib.netty.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.ReadTimeoutException;

import com.spdb.ib.dpib.logs.LogManager;

/**
 * 客户端业务类
 * 
 * @version 1.0
 * @date 19 5 2014
 * @author T-wuwp
 * 
 */
public class PushClientHandler extends SimpleChannelUpstreamHandler {
	private NettyClient client;

	public PushClientHandler() {
	}

	public PushClientHandler(NettyClient client) {
		this.client = client;
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof ChannelStateEvent) {
			LogManager.info(e.toString());
		}
		super.handleUpstream(ctx, e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		// Print out the line received from the server.
		String message = e.getMessage().toString();
		LogManager.info("服务器返回数据:" + message);
		client.responseMessage = client.responseMessage + message + " ";
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		if (e.getCause() instanceof ReadTimeoutException) {
			// 关闭连接
			e.getChannel()
					.write("<Envelope><Header><H1>5168168</H1><H2>1001</H2><H3>9999</H3><H4>链接超时</H4></Header></Envelope>");
			client.delayMessage = "<Envelope><Header><H1>5168168</H1><H2>1001</H2><H3>9999</H3><H4>链接超时</H4></Header></Envelope>";
			e.getChannel().close().awaitUninterruptibly();
			e.getFuture().addListener(ChannelFutureListener.CLOSE);
		} else {
			LogManager.warn(Level.WARNING
					+ "Unexpected exception from downstream." + e.getCause());
			e.getChannel().close();
		}
	}

}
