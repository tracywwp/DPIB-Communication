package com.spdb.ib.dpib.mq.jms.receive;

import javax.jms.Destination;

public interface ConsumerService {
	String receive(Destination adapterQueue);
}
