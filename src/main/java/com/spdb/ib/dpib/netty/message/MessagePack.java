package com.spdb.ib.dpib.netty.message;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;

/**
 * 消息及业务处理抽象实体类
 * 
 * @version 1.0
 * @date 23 5 2014
 * @author T-wuwp
 * 
 */
public abstract class MessagePack {
	protected String msg;
    protected String tranCode;
    protected DefaultChannelGroup channelGroup;
    protected ServerSocketChannelFactory channelFactory;
    protected Channel channel;
	public MessagePack() {
	}

	/**
	 * use for socket message
	 * 
	 * @param msg
	 * @param socketId
	 */
	public MessagePack(String msg) {
		this.msg = msg;
	}

	/**
	 * use for socket message
	 * 
	 * @param msg
	 * @param socketId
	 */

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 业务处理抽象方法
	 * 
	 * @param message
	 */
	public abstract String onHandler(MessagePack message);
	/**
	 * server回复后的继续操作
	 */
	public abstract void afterReplyDone();
	/**
	 * server回复客户端超时或者其他异常的操作
	 */
	public abstract void replyException(Exception e);
	public String getTranCode() {                                                                                                                   
		return tranCode;
	}
    
	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}

	public DefaultChannelGroup getChannelGroup() {
		return channelGroup;
	}

	public void setChannelGroup(DefaultChannelGroup channelGroup) {
		this.channelGroup = channelGroup;
	}

	public ServerSocketChannelFactory getChannelFactory() {
		return channelFactory;
	}

	public void setChannelFactory(ServerSocketChannelFactory channelFactory) {
		this.channelFactory = channelFactory;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
    
}