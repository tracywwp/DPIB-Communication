package com.spdb.ib.dpib.netty.spring.server;


public class SingleTran3691 extends AbstractSingleTran{
	int i = 0;

	@Override
	public void afterReplyDone() {
		i++;
		System.out.println("AccountNoi=" + i);
		System.out.println("After reply Done");

	}

	@Override
	public void replyException(Exception e) {
		e.printStackTrace();
		i = i++;
		System.out.println("AccountNoi=" + i);
		System.out.println("reply Exception");

	}

	@Override
	public String doTrade() {
		System.out.println("input doTrade=== ");
		//String response = message;
		return null;
	}

	

}
