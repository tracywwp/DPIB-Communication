package com.spdb.ib.dpib.socket.nioserver;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServerTest {
	public static ExecutorService es = Executors.newFixedThreadPool(200);
	private static int port = 8848;

	public void setUp() throws Exception {
	}

	public void tearDown() throws Exception {
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		ApplicationContext context=new ClassPathXmlApplicationContext(
				"classpath*:/applicationContext-communication-test.xml");
		System.out.println(context.getBean("springServer"));
	}
}
