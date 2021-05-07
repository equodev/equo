package com.equo.server.offline.api.filters;

import java.nio.charset.Charset;

import org.apache.http.entity.ContentType;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;

public interface IModifiableResponse {

	default FullHttpResponse generateModifiedResponse() {
		FullHttpResponse originalFullHttpResponse = getOriginalFullHttpResponse();
		String contentTypeHeader = originalFullHttpResponse.headers().get("Content-Type");
		ContentType contentType = ContentType.parse(contentTypeHeader);
		Charset charset = contentType.getCharset();

		ByteBuf content = originalFullHttpResponse.content();

		byte[] data = new byte[content.readableBytes()];
		content.readBytes(data);

		String responseToTransform = createStringFromData(data, charset);

		String transformedResponse = modifyOriginalResponse(responseToTransform);

		byte[] bytes = createDataFromString(transformedResponse, charset);
		ByteBuf transformedContent = Unpooled.buffer(bytes.length);
		transformedContent.writeBytes(bytes);

		DefaultFullHttpResponse transformedHttpResponse = new DefaultFullHttpResponse(
				originalFullHttpResponse.getProtocolVersion(), originalFullHttpResponse.getStatus(),
				transformedContent);
		transformedHttpResponse.headers().set(originalFullHttpResponse.headers());
		HttpHeaders.setContentLength(transformedHttpResponse, bytes.length);

		return transformedHttpResponse;
	}

	static String createStringFromData(byte[] data, Charset charset) {
		return (charset == null) ? new String(data) : new String(data, charset);
	}

	static byte[] createDataFromString(String string, Charset charset) {
		return (charset == null) ? string.getBytes() : string.getBytes(charset);
	}

	FullHttpResponse getOriginalFullHttpResponse();

	boolean isModifiable();

	String modifyOriginalResponse(String responseToTransform);

}
