package com.spdb.ib.dpib.netty.codec.string;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

public class CommunicationDecoder extends OneToOneDecoder {
	public CommunicationDecoder() {
		this(Charset.defaultCharset());
	}

	public CommunicationDecoder(Charset charset) {
		if (charset == null) {
			throw new NullPointerException("charset");
		} else {
			this.charset = charset;
			return;
		}
	}

	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		if (!(msg instanceof ChannelBuffer))
			return msg;
		else
			return ((ChannelBuffer) msg).toString(charset);
	}

	private final Charset charset;
}
