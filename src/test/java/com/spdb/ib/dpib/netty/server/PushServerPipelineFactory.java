package com.spdb.ib.dpib.netty.server;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.HashedWheelTimer;

import com.spdb.ib.dpib.netty.message.MessageManager;

/**
 * 服务端数据读写类，工厂类
 * 
 * @version 1.0
 * @date 19 5 2014
 * @author T-wuwp
 * 
 */
public class PushServerPipelineFactory implements ChannelPipelineFactory {
	
	private DefaultChannelGroup channelGroup;
	private MessageManager messageManager;
	private ServerSocketChannelFactory channelFactory;
	private int port;

	public PushServerPipelineFactory() {
	}

	public PushServerPipelineFactory(DefaultChannelGroup channelGroup) {
		this.channelGroup = channelGroup;
	}

	public PushServerPipelineFactory(DefaultChannelGroup channelGroup,
			ServerSocketChannelFactory channelFactory) {
		this.channelGroup = channelGroup;
		this.channelFactory = channelFactory;
	}

	public PushServerPipelineFactory(DefaultChannelGroup channelGroup,
			MessageManager messageManager,
			ServerSocketChannelFactory channelFactory, int port) {
		this.channelGroup = channelGroup;
		this.messageManager = messageManager;
		this.channelFactory = channelFactory;
		this.port = port;
	}

	/**
	 * 构建服务端责任链
	 */
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		//pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
//		pipeline.addLast("protobufDecoder",
//				new ProtobufDecoder());
//		pipeline.addLast("protobufEncoder",
//				new ProtobufEncoder());
//		pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
		pipeline.addLast("readTimeOut", new ReadTimeoutHandler(
				new HashedWheelTimer(), 120));
		pipeline.addLast("handler", new PushServerHandler(channelGroup,
				messageManager, channelFactory, port));
		return pipeline;
	}

	protected ChannelPipelineFactory createPushServerPipelineFactory(
			DefaultChannelGroup allChannels) {
		return new PushServerPipelineFactory(allChannels);
	}
}
