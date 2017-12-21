package com.spdb.ib.dpib.netty.message.bussiness;

import java.util.concurrent.RejectedExecutionException;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;

import com.spdb.ib.dpib.logs.LogManager;
import com.spdb.ib.dpib.process.manager.MessagePack;

/**
 * 托管请求报文处理
 * 
 * @author T-wuwp
 * 
 */
public class DepositCategoryMsg extends MessagePack {
	private static final String MESSAGE_NAME = "JSMESSAGE"; // 消息名称
	/**
	 * 发送消息后缀
	 */
	private final String buffer = "\r\n";
	private Channel channel;
	private DefaultChannelGroup channelGroup;
	private ServerSocketChannelFactory channelFactory;
	private String clientIp;
	private int clientPort;

	public DepositCategoryMsg() {
	}

	public DepositCategoryMsg(String msg, Channel channel,
			DefaultChannelGroup channelGroup,
			ServerSocketChannelFactory channelFactory, String clientIp,
			int clientPort) {
		super(msg);
		this.channel = channel;
		this.channelGroup = channelGroup;
		this.channelFactory = channelFactory;
		this.clientIp = clientIp;
		this.clientPort = clientPort;
	}

	@Override
	public void onHandler(MessagePack message) {
		ChannelFuture future;
		boolean close = false;
		String response = "";
		boolean stop = false;
		String depositResponse = "";
		try {
			// 客户端数据
			String request = message.getMsg();
			LogManager.info("获取客户端数据:" + request);
			if (request.length() == 0) {
				response = "Please send something." + buffer;
				close = true;
			} else {
				// 前置接受托管的数据并处理
				response =""; 
				if (response.contains("#")) {
					String[] responseArray = response.split("##");
					//将前置处理的数据发送给网站
					String sResponse = "";
					if (null != sResponse && sResponse.length() > 0) {
						String msg =""; 
						depositResponse = msg;
						LogManager.info("depositResponse:" + depositResponse);
						close = true;
					}else{
						depositResponse = "网站无返回数据====";
						LogManager.info("depositResponse:" + depositResponse);
						close = true;
					}
				} else {
					depositResponse = "报文格式错误或者数据解析错误====";
					LogManager.info("depositResponse:" + depositResponse);
					close = true;
				}
			}

			// We do not need to write a ChannelBuffer here.
			// We know the encoder inserted at TelnetPipelineFactory will do the
			// conversion.
			if (null != depositResponse && depositResponse.length() > 0) {
				future = channel.write(depositResponse);
			} else {
				future = channel.write(response);
			}
			// Close the connection after sending 'Have a good day!'
			// if the client has sent 'bye'.
			if (close) {
				future.addListener(ChannelFutureListener.CLOSE);
			}
			if (stop) {
				ChannelGroupFuture groupFuture = channelGroup.close();
				groupFuture.awaitUninterruptibly();
				channelFactory.releaseExternalResources();
				LogManager.info("关闭socket connection======");
			}
			LogManager.info("发送给客户端消息成功！");

		} catch (RejectedExecutionException re) {
			String errorMessage = re.getMessage().toString();
			future = channel.write(errorMessage);
			future.addListener(ChannelFutureListener.CLOSE);
			LogManager.error("work shutdown......");
		} catch (Exception ex) {
			String errorMessage = ex.getMessage().toString();
			future = channel.write(errorMessage);
			future.addListener(ChannelFutureListener.CLOSE);
			LogManager.error(errorMessage);
		}
	}

	@Override
	public String getName() {
		return MESSAGE_NAME;
	}

}
