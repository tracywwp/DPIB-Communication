package com.spdb.ib.dpib.mq.jms.test;

import javax.jms.Destination;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.spdb.ib.dpib.mq.jms.producer.ProducerService;
import com.spdb.ib.dpib.mq.jms.receive.ConsumerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-jms-ibmmq.xml")
public class ProducerConsumerTest {

	@Autowired
	private ProducerService producerService;
	
	 @Autowired
	 private ConsumerService consumerService;
	
	@Autowired
	@Qualifier("adapterQueue")
	private Destination adapterQueue;
    
	@Test
	public void testMessageSend() {
		producerService.sendSingle("发出某商品交易请求报文数据====", adapterQueue, false);
		//consumerService.receive(adapterQueue);
	}
    
}
