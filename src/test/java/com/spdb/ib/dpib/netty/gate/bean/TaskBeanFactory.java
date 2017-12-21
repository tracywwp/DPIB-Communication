package com.spdb.ib.dpib.netty.gate.bean;

import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.spdb.ib.dpib.netty.message.MessagePack;
import com.spdb.ib.dpib.netty.message.bussiness.CategoryMsg;
import com.spdb.ib.dpib.netty.message.bussiness.DepositCategoryMsg;

/**
 * 获取spring容器管理的对象
 * 
 * @author T-wuwp
 * 
 */
public class TaskBeanFactory {

	private static ApplicationContext context;

	private static byte[] bytesync = new byte[0];

	public static ApplicationContext getContextInstance() {
		if (context == null) {
			synchronized (bytesync) {
				context = new ClassPathXmlApplicationContext(
						"classpath*:/applicationContext-jms-ibmmq.xml");
			}
		}
		return context;
	}

	public static Object getBean(String beanName) {
		return TaskBeanFactory.getContextInstance().getBean(beanName);
	}

	public static MessagePack handleMessage(String msg, MessageEvent e,
			DefaultChannelGroup channelGroup,
			ServerSocketChannelFactory channelFactory, int port, String clientIp,int clientPort) {
//		if (port == 8849) {
//			// 托管请求
//			MessagePack messagePack = new DepositCategoryMsg(msg,
//					e.getChannel(), channelGroup,
//					channelFactory, clientIp,clientPort);
//			return messagePack;
//		} else {
			// 网站请求
			MessagePack messagePack = new CategoryMsg(msg, e.getChannel(),
					 channelGroup, channelFactory, clientIp,clientPort);
			return messagePack;
		//}

	}

}
