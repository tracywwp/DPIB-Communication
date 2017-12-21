package com.spdb.ib.dpib.mq.jms.producer;

import javax.jms.Destination;

public interface ProducerService {
     void sendSingle(String message, Destination destination,boolean isSendAsync);
}
