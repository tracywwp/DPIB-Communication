package com.spdb.ib.dpib.mq.jms.listener;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.listener.SessionAwareMessageListener;

/**
 * 
 * @author T-wuwp SessionAwareMessageListener的设计就是为了方便我们在接收到消息后发送一个回复的消息，
 *         它同样为我们提供了一个处理接收到的消息的onMessage方法
 *         ，但是这个方法可以同时接收两个参数，一个是表示当前接收到的消息Message， 另一个就是可以用来发送消息的Session对象
 */
public class ConsumerSessionAwareMessageListener implements
		SessionAwareMessageListener<TextMessage> {

	private Destination destination;

	public void onMessage(TextMessage message, Session session)
			throws JMSException {
		System.out.println("收到一条消息");
		System.out.println("消息内容是：" + message.getText());
		MessageProducer producer = session.createProducer(destination);
		Message textMessage = session
				.createTextMessage("ConsumerSessionAwareMessageListener。。。");
		producer.send(textMessage);
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}
}
