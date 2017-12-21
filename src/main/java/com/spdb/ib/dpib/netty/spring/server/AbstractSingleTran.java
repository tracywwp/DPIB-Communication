package com.spdb.ib.dpib.netty.spring.server;


/**
 * 业务操作抽象类
 * 
 * @author T-wuwp
 * 
 */
public abstract class AbstractSingleTran {

	/**
	 * server回复后的继续操作
	 */
	public abstract void afterReplyDone();

	/**
	 * server回复客户端超时或者其他异常的操作
	 */
	public abstract void replyException(Exception e);

	public abstract String doTrade();

}
