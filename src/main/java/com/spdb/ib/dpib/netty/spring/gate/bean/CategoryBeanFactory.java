package com.spdb.ib.dpib.netty.spring.gate.bean;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.DefaultChannelGroup;

import com.spdb.ib.dpib.netty.message.MessagePack;

/**
 * 返回消息及业务处理实现类
 * @author T-wuwp
 *
 */
public class CategoryBeanFactory extends TaskBeanFactory {
	private MessagePack messagePack;
    
	@Override
	public MessagePack handleMessage(String msg,Channel channel,DefaultChannelGroup channelGroup) {
		messagePack.setMsg(msg);
		messagePack.setChannel(channel);
		messagePack.setChannelGroup(channelGroup);
		return messagePack;
	}

	public MessagePack getMessagePack() {
		return messagePack;
	}

	public void setMessagePack(MessagePack messagePack) {
		this.messagePack = messagePack;
	}

}
