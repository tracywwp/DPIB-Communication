package com.spdb.ib.dpib.netty.client;

import static org.jboss.netty.channel.Channels.pipeline;

import java.nio.charset.Charset;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.HashedWheelTimer;

import com.spdb.ib.dpib.netty.codec.frame.TailedDelimiters;

/**
 * 客户端数据读取类，工厂类
 * 
 * @version 1.0
 * @date 19 5 2014
 * @author T-wuwp
 * 
 */
public class PushClientPipelineFactory implements ChannelPipelineFactory {
	private NettyClient client;

	public PushClientPipelineFactory() {
	}

	public PushClientPipelineFactory(NettyClient client) {
		this.client = client;
	}

	/**
	 * 构建客户端责任链
	 */
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		pipeline.addLast("frameDecoder", new DelimiterBasedFrameDecoder(8192,
				Delimiters.lineDelimiter()));
		pipeline.addLast("decoder", new StringDecoder(Charset.forName("GBK")));
		pipeline.addLast("encoder", new StringEncoder(Charset.forName("GBK")));
		pipeline.addLast("readTimeOut", new ReadTimeoutHandler(
				new HashedWheelTimer(), 120));
		pipeline.addLast("handler", new PushClientHandler(client));
		return pipeline;
	}

}
