package com.spdb.ib.dpib.netty.spring.server;

import com.spdb.ib.dpib.netty.message.MessagePack;

/**
 * 
 * @author T-wuwp 根据不同的交易代码返回对应的Server
 */
public interface ServerTranFactory {
	public AbstractSingleTran getServerHandler(String trancode);
	public MessagePack getMessagePack();
}
