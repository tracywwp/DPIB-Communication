package com.spdb.ib.dpib.netty.message;


/**
 * 业务处理类
 * @author T-wuwp
 *
 */
public class CategoryMsgService  extends MessagePack{
   
	
	public String onHandler(MessagePack message) {
		System.out.println("CategoryMsgService onHandler=====");
		String response=message.getMsg();
		return response;
	}

	@Override
	public void afterReplyDone() {
		System.out.println("afterReplyDone");
		
	}

	@Override
	public void replyException(Exception e) {
		System.out.println("replyException");
		
	}

}
