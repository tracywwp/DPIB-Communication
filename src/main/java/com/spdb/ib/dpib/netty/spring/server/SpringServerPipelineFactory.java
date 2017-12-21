package com.spdb.ib.dpib.netty.spring.server;

import static org.jboss.netty.channel.Channels.pipeline;

import java.nio.charset.Charset;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.HashedWheelTimer;

import com.spdb.ib.dpib.netty.codec.string.CommunicationDecoder;
import com.spdb.ib.dpib.netty.codec.string.CommunicationEncoder;

/**
 * 服务端数据读写类，工厂类
 * 
 * @version 1.0
 * @date 19 5 2014
 * @author T-wuwp
 * 
 */
public class SpringServerPipelineFactory implements ChannelPipelineFactory {
	
	//private ServerSocketChannelFactory channelFactory;
    private SpringServerHandler serverHandler;
    /*
     * 单位：秒
     */
    private int hashedWheelTime;
    
	public SpringServerPipelineFactory() {
	}

	/**
	 * 构建服务端责任链
	 */
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		pipeline.addLast("frameDecoder", new DelimiterBasedFrameDecoder(
				1000000, Delimiters.lineDelimiter()));
		pipeline.addLast("decoder",
				new CommunicationDecoder(Charset.forName("GBK")));
		pipeline.addLast("encoder",
				new CommunicationEncoder(Charset.forName("GBK")));
		pipeline.addLast("readTimeOut", new ReadTimeoutHandler(
				new HashedWheelTimer(), hashedWheelTime));
		pipeline.addLast("handler", serverHandler);
		return pipeline;
	}

	public SpringServerHandler getServerHandler() {
		return serverHandler;
	}

	public void setServerHandler(SpringServerHandler serverHandler) {
		this.serverHandler = serverHandler;
	}

	public int getHashedWheelTime() {
		return hashedWheelTime;
	}

	public void setHashedWheelTime(int hashedWheelTime) {
		this.hashedWheelTime = hashedWheelTime;
	}

	
}
