package com.spdb.ib.dpib.netty.codec.string;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class CommunicationEncoder extends OneToOneEncoder {
	public CommunicationEncoder() {
		this(Charset.defaultCharset());
	}

	public CommunicationEncoder(Charset charset) {
		if (charset == null) {
			throw new NullPointerException("charset");
		} else {
			this.charset = charset;
			return;
		}
	}

	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		if (msg instanceof String)
			return ChannelBuffers.copiedBuffer(ctx.getChannel().getConfig()
					.getBufferFactory().getDefaultOrder(), (String) msg,
					charset);
		else
			return msg;
	}

	private final Charset charset;
}
