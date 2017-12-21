package com.spdb.ib.dpib.netty.message.bussiness;

import java.util.Arrays;
import java.util.concurrent.RejectedExecutionException;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;

import com.spdb.ib.dpib.logs.LogManager;
import com.spdb.ib.dpib.netty.message.MessagePack;

public class CategoryMsg extends MessagePack {
	
	private DefaultChannelGroup channelGroup;
	private ServerSocketChannelFactory channelFactory;
	private Channel channel;
	private String clientIp;
	private int clientPort;
	
	public CategoryMsg() {
	}

	public CategoryMsg(String msg, Channel channel,
			DefaultChannelGroup channelGroup,
			ServerSocketChannelFactory channelFactory, String clientIp,int clientPort) {
		this.msg=msg;
		this.channel = channel;
		this.channelGroup = channelGroup;
		this.channelFactory = channelFactory;
		this.clientIp = clientIp;
		this.clientPort = clientPort;
	}

	public CategoryMsg(String msg, Channel channel,
			DefaultChannelGroup channelGroup) {
		this.msg=msg;
		this.channel = channel;
		this.channelGroup = channelGroup;
	}

	@Override
	public String onHandler(MessagePack message) {
		ChannelFuture future;
		boolean close = false;
		String response = "";
		boolean stop = false;
		String depositResponse = "";
		String[] requests = null;
		try {
			// 客户端数据
			String request = message.getMsg();
			LogManager.info("获取客户端数据:" + request);
			if (request.length() == 0) {
				response = "Please send something.";
				close = true;
			} else {
				// 停止服务
				if (request.trim().equals("-1")) {
					stop = true;
					response = "1";
					close = true;
					LogManager.info("server stopped success....." + request);
				} else if (request.trim().indexOf("<") != -1) {
					response ="";

					LogManager.info("response:" + response);
					if (response.contains("#")) {
						requests = response.split("##");
						LogManager.info(requests.length + ""
								+ Arrays.toString(requests));
						// 托管请求
						String depositResponseMessage="";
						
						if (null != depositResponseMessage
								&& depositResponseMessage.length() > 0) {
							depositResponse ="";
//							 depositResponse = msgConvertFactory
//									 .getExchangeMsg(
//									 "0000000000000274<Envelope><Header><H1>G00005</H1><H2>1001</H2></Header><Body><record><B1>00</B1><B2>10000000</B2></record><record><B1>00</B1><B2>10000000</B2><B3>2</B3></record><record><B1>00</B1><B2>10000000</B2><B3>2</B3></record></Body></Envelope>",
//									 "1", requests[3]);
							LogManager.info("depositResponse:"
									+ depositResponse);
							close = true;
						} else {
							depositResponse = "托管无返回信息====";
							LogManager.info("depositResponse:"
									+ depositResponse);
							close = true;
						}
					}

				}

			}
			if (response.equals("")) {
				response = "请输入正确格式的报文=====";
				close = true;
			}
			// We do not need to write a ChannelBuffer here.
			// We know the encoder inserted at TelnetPipelineFactory will do the
			// conversion.

			if (null != depositResponse && depositResponse.length() > 0) {
				future = channel.write(depositResponse+"\r\n");
			} else {
				future = channel.write(response+"\r\n");
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
		return depositResponse;
	}

	

	public DefaultChannelGroup getChannelGroup() {
		return channelGroup;
	}

	public void setChannelGroup(DefaultChannelGroup channelGroup) {
		this.channelGroup = channelGroup;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	@Override
	public void afterReplyDone() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replyException(Exception e) {
		// TODO Auto-generated method stub
		
	}

}
