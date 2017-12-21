package com.spdb.ib.dpib.netty.spring.factory;

import java.util.HashMap;

import com.spdb.ib.dpib.common.SpringContextUtil;
import com.spdb.ib.dpib.netty.message.MessagePack;
import com.spdb.ib.dpib.netty.spring.server.AbstractSingleTran;
import com.spdb.ib.dpib.netty.spring.server.ServerTranFactory;

/**
 * 
 * @author T-wuwp 交易的工厂实现
 */
public class ServerTranFactoryImp implements ServerTranFactory {
	HashMap<String, String> sopTranMap;

	@Override
	public AbstractSingleTran getServerHandler(String trancode) {
		return (AbstractSingleTran) SpringContextUtil.getBean(sopTranMap
				.get(trancode));
	}

	@Override
	public MessagePack getMessagePack() {
		return (MessagePack) SpringContextUtil.getBean("messagePack");
	}

	public HashMap<String, String> getSopTranMap() {
		return sopTranMap;
	}

	public void setSopTranMap(HashMap<String, String> sopTranMap) {
		this.sopTranMap = sopTranMap;
	}

}
