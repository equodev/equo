package com.make.equo.server.offline.provider;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class OfflineHttpResponse extends DefaultHttpResponse implements HttpContent {

	private final ByteBuf content;
	private final boolean validateHeaders;

	public OfflineHttpResponse(HttpVersion version, HttpResponseStatus status) {
		this(version, status, Unpooled.buffer(0));
	}

	public OfflineHttpResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content) {
		this(version, status, content, true);
	}

	public OfflineHttpResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content,
			boolean validateHeaders) {
		super(version, status, validateHeaders);
		if (content == null) {
			throw new NullPointerException("content");
		}
		this.content = content;
		this.validateHeaders = validateHeaders;
	}

	@Override
	public ByteBuf content() {
		return content;
	}

	@Override
	public int refCnt() {
		return content.refCnt();
	}

	@Override
	public HttpContent retain() {
		content.retain();
		return this;
	}

	@Override
	public HttpContent retain(int increment) {
		content.retain(increment);
		return this;
	}

	@Override
	public boolean release() {
		return content.release();
	}

	@Override
	public boolean release(int decrement) {
		return content.release(decrement);
	}

	@Override
	public DefaultHttpResponse setProtocolVersion(HttpVersion version) {
		super.setProtocolVersion(version);
		return this;
	}

	@Override
	public DefaultHttpResponse setStatus(HttpResponseStatus status) {
		super.setStatus(status);
		return this;
	}

	@Override
	public HttpContent copy() {
		OfflineHttpResponse copy = new OfflineHttpResponse(getProtocolVersion(), getStatus(), content().copy(),
				validateHeaders);
		copy.headers().set(headers());
		return copy;
	}

	@Override
	public HttpContent duplicate() {
		OfflineHttpResponse duplicate = new OfflineHttpResponse(getProtocolVersion(), getStatus(),
				content().duplicate(), validateHeaders);
		duplicate.headers().set(headers());
		return duplicate;
	}

}
