package com.make.equo.server.offline.provider;

import com.make.equo.server.offline.api.filters.IModifiableResponse;
import com.make.equo.server.offline.provider.utils.IConstants;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Names;

public class FromHttpsToHttpResponse implements IModifiableResponse {

	private FullHttpResponse originalFullHttpResponse;

	public FromHttpsToHttpResponse(FullHttpResponse originalFullHttpResponse) {
		this.originalFullHttpResponse = originalFullHttpResponse;
	}

	@Override
	public boolean isModifiable() {
		String contentTypeHeader = originalFullHttpResponse.headers().get(Names.CONTENT_TYPE);
		return contentTypeHeader != null
				&& (contentTypeHeader.startsWith("text/") || contentTypeHeader.startsWith("application/xhtml+xml")
						|| contentTypeHeader.startsWith("application/json")
						|| contentTypeHeader.startsWith("application/javascript"));
	}

	@Override
	public String modifyOriginalResponse(String responseToTransform) {
		return responseToTransform.replaceAll(IConstants.HTTPS_PROTOCOL, IConstants.HTTP_PROTOCOL);
	}

	@Override
	public FullHttpResponse getOriginalFullHttpResponse() {
		return originalFullHttpResponse;
	}

}
