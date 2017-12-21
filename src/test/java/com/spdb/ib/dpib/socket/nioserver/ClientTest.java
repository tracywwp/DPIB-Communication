package com.spdb.ib.dpib.socket.nioserver;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.junit.Test;

import com.spdb.ib.dpib.logs.LogManager;
import com.spdb.ib.dpib.netty.gate.StartClient;
import com.spdb.ib.dpib.netty.server.NettyClient;

public class ClientTest extends TestCase {

	/**
	 * 
	 * @param host
	 * @param port
	 * @param message
	 * @return
	 */
	@Test
	public void testClientStart() throws UnsupportedEncodingException {
		String message =
		"-1";
		System.out.println(StartClient.startClient("10.112.71.35", 8848,
				message.toString()));
		//NettyClient client = new NettyClient("10.112.71.34", 8848, b);
		//LogManager.info("Client starting success...");
		// System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Envelope><Header><Fund_Id>7500020</Fund_Id><Trade_Code>1021</Trade_Code><Error_Code></Error_Code><Error_Message></Error_Message></Header><Body><record><Data_Date>2014-05-23</Data_Date><Data_Type>0</Data_Type><Member_Id>2323</Member_Id><Sub_Account>acount2323</Sub_Account></record></Body></Envelope>".length());
	}

	public static void main(String[] args) {
		byte[] sendc = { (byte) 0x00, (byte) 0xa4, (byte) 0x30, (byte) 0x30,
				(byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30,
				(byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30,
				(byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30,
				(byte) 0x30, (byte) 0x30, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x31, (byte) 0x32,
				(byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36,
				(byte) 0x37, (byte) 0x38, (byte) 0x39, (byte) 0x30,
				(byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x30, (byte) 0x30,
				(byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x30,
				(byte) 0x30, (byte) 0x30, (byte) 0x30, (byte) 0x01,
				(byte) 0x30, (byte) 0x00, (byte) 0x30, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x31,
				(byte) 0x61, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x30, (byte) 0x32, (byte) 0x31, (byte) 0x00,
				(byte) 0x39, (byte) 0x39, (byte) 0x37, (byte) 0x39,
				(byte) 0x39, (byte) 0x39, (byte) 0x39, (byte) 0x37,
				(byte) 0x39, (byte) 0x30, (byte) 0x30, (byte) 0x38,
				(byte) 0x44, (byte) 0x53, (byte) 0x30, (byte) 0x36,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x58, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x39, (byte) 0x39, (byte) 0x37,
				(byte) 0x39, (byte) 0x37, (byte) 0x38, (byte) 0x32,
				(byte) 0x35, (byte) 0x38, (byte) 0x34, (byte) 0x30,
				(byte) 0x38, (byte) 0x32, (byte) 0x30, (byte) 0x31,
				(byte) 0x33, (byte) 0x30, (byte) 0x33, (byte) 0x30,
				(byte) 0x31, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x01, (byte) 0x30, (byte) 0x04, (byte) 0x44,
				(byte) 0x53, (byte) 0x30, (byte) 0x38, (byte) 0x10,
				(byte) 0x35, (byte) 0x38, (byte) 0x30, (byte) 0x31,
				(byte) 0x38, (byte) 0x30, (byte) 0x30, (byte) 0x31,
				(byte) 0x33, (byte) 0x30, (byte) 0x30, (byte) 0x36,
				(byte) 0x30, (byte) 0x31, (byte) 0x31, (byte) 0x30, (byte) 0x00 };
		System.out.println("sendc length "+sendc.length);
		NettyClient client = new NettyClient("10.112.18.43", 6065, sendc);
		client.StartClient();
	}

}
