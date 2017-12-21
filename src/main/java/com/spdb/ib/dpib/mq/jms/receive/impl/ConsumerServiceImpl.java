package com.spdb.ib.dpib.mq.jms.receive.impl;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.spdb.ib.dpib.mq.jms.receive.ConsumerService;
/**
 * 
 * @author T-wuwp
 *往队列接受消息
 */
@Component
public class ConsumerServiceImpl implements ConsumerService{
    private JmsTemplate jmsTemplate;
	
    private Destination adapterQueue;
    
	@Override
	public  String receive(Destination adapterQueue) {
		TextMessage message=(TextMessage)jmsTemplate.receive(adapterQueue);
		
		try {  
            System.out.println(">>接收到的消息>>"+message.getText());  
            return message.getText();
        } catch (JMSException e) {  
            e.printStackTrace();  
            return null;
        }  

	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}
    
	@Resource
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public Destination getAdapterQueue() {
		return adapterQueue;
	}
    
	@Resource
	public void setAdapterQueue(Destination adapterQueue) {
		this.adapterQueue = adapterQueue;
	}

	

}
