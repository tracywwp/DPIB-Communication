package com.spdb.ib.dpib.netty.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.spdb.ib.dpib.logs.LogManager;
import com.spdb.ib.dpib.netty.message.MessageManager;

public class NettyServer implements Callable {
	/**
	 * 服务器端口
	 */
	private int port;
	/**
	 * 发送消息字节大小
	 */
	private static final int tcpSendBufferSize = 10000000;
	/**
	 * 获取消息字节大小
	 */
	private static final int tcpReceiveBufferSize = 10000000;
	
	/**
	 * 消息管理类
	 */
	private MessageManager messageManager;

	public NettyServer() {
	}

	public NettyServer(int port) {
		this.port = port;
	}

	public NettyServer(int port, MessageManager messageManager) {
		this.port = port;
		this.messageManager = messageManager;
	}

	

	@Override
	public Object call() throws Exception {
		try {
			// 需要监听的端口，即tcp连接建立的端口
			InetSocketAddress addr = new InetSocketAddress(port);
			// Executors.newCachedThreadPool()的解释：
			// 缓冲线程执行器，产生一个大小可变的线程池。
			// 当线程池的线程多于执行任务所需要的线程的时候，对空闲线程（即60s没有任务执行）进行回收；
			// 当执行任务的线程数不足的时候，自动拓展线程数量。因此线程数量是JVM可创建线程的最大数目。
			ServerSocketChannelFactory channelFactory = new NioServerSocketChannelFactory(
					Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool());
			// 创建一个新的服务并取名字
			DefaultChannelGroup allChannels = new DefaultChannelGroup(
					"pushServerChannelGroup");
			// PushServerPipelineFactory作为一个ChannelPipelineFactory产生的工厂类，我们可以把需要执行的Handler进行配置
			ChannelPipelineFactory pipelineFactory = null;

			pipelineFactory = new PushServerPipelineFactory(allChannels,
					messageManager, channelFactory, port);

			ServerBootstrap bootstrap = new ServerBootstrap(channelFactory);
			// 服务器新连接建立的时候，新的ChannelPipeline会通过我们定义的ChannelPipelineFactory产生，其实是调用了getPipeline()方法。
			bootstrap.setPipelineFactory(pipelineFactory);

			if (tcpReceiveBufferSize != -1) {
				bootstrap.setOption("child.receiveBufferSize",
						tcpReceiveBufferSize);
			}
			if (tcpSendBufferSize != -1) {
				bootstrap.setOption("child.sendBufferSize", tcpSendBufferSize);
			}

			bootstrap.setOption("reuseAddress", false);
			bootstrap.setOption("child.reuseAddress", false);
			bootstrap.setOption("child.keepAlive", false);
			bootstrap.setOption("child.tcpNoDelay", true);

			LogManager
					.info(" =================== start server =====================");
			Channel serverChannel = bootstrap.bind(addr);
			allChannels.add(serverChannel);
			LogManager.info(" =================== server started success bind port:"
					+ port + "=========");
			return 1;
		} catch (Exception e) {
			LogManager.error("..........server started error..........");
			return 0;
		}
	}
}
