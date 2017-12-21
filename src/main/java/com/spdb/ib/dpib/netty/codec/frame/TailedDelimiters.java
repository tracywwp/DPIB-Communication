package com.spdb.ib.dpib.netty.codec.frame;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * 接受消息以什么符号结束
 * @author T-wuwp
 *
 */
public final class TailedDelimiters {
	//#####
	public static ChannelBuffer[] poundSignDelimiter() {
		return (new ChannelBuffer[] { ChannelBuffers.wrappedBuffer(new byte[] {
				35, 35, 35, 35, 35 }) });
	}

	private TailedDelimiters() {
	}
}
