package com.spdb.ib.dpib.netty.spring.gate.bean;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.DefaultChannelGroup;

import com.spdb.ib.dpib.netty.message.MessagePack;
/**
 * 返回消息及业务处理实现类的抽象类
 * @author T-wuwp
 *
 */
public abstract class TaskBeanFactory {
	public abstract MessagePack handleMessage(String msg,Channel channel,DefaultChannelGroup channelGroup);
	
}
