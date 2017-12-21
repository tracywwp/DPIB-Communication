package com.spdb.ib.dpib.mq.jms.producer.impl;

import java.util.Collection;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.spdb.ib.dpib.mq.jms.producer.ProducerService;

/**
 * 
 * @author T-wuwp 向mq队列发送消息
 */
@Component
public class ProducerServiceImpl implements ProducerService {
	// jms 监听器
	private JmsTemplate jmsTemplate;
	@Autowired
	private TaskExecutor taskExecutor;

	public void sendSingle(String message, Destination destination,boolean isSendAsync) {
		sendMessage(message, destination,isSendAsync);
	}

	public void sendBatch(Collection<?> messages, Destination destination) {
		Assert.notNull(messages, "param 'messages' can't be null !");
		Assert.notEmpty(messages, "param 'message' can't be empty !");
		for (Object message : messages) {
			if (null != message && message instanceof String) {
				//sendSingle(String.valueOf(message), destination);
			}
		}
	}

	private void sendMessage(final String message, Destination destination,boolean isSendAsync) {
		final Destination sendDest = destination;
		// 异步发送消息
		if (isSendAsync) {
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					send(message, sendDest);
				}
			});
		} else {
			send(message, destination);
		}
	}

	private void send(final String message, Destination destination) {
		this.jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				try {
					System.out.println("---------------生产者发了一个消息成功--------："
							+ message);
					return session.createTextMessage(message);
				} catch (Exception e) {
					System.out.println("---------------生产者发了一个消息失败--------："
							+ message);
					return null;
				}
			}

		});
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	@Resource
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	
}
