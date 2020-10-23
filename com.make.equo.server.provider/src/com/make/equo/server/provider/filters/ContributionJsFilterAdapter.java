package com.make.equo.server.provider.filters;

import static com.make.equo.server.provider.EquoHttpProxyContribution.PROXY_CONTRIBUTION_NAME;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.activation.MimetypesFileTypeMap;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.google.common.io.ByteStreams;
import com.make.equo.contribution.api.resolvers.IEquoContributionUrlResolver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class ContributionJsFilterAdapter extends HttpFiltersAdapter {

	private IEquoContributionUrlResolver urlResolver;
	private String loggerLevel;

	public ContributionJsFilterAdapter(HttpRequest originalRequest, IEquoContributionUrlResolver urlResolver,
			String loggerLevel) {
		super(originalRequest);
		this.urlResolver = urlResolver;
		this.loggerLevel = loggerLevel;
	}

	public HttpResponse clientToProxyRequest(HttpObject httpObject) {
		String requestUri = originalRequest.getUri();
		String fileName = requestUri.substring(
				requestUri.indexOf(PROXY_CONTRIBUTION_NAME) + PROXY_CONTRIBUTION_NAME.length(), requestUri.length());
		URL resolvedUrl = urlResolver.resolve(fileName);
		return buildHttpResponse(resolvedUrl);
	}

	private HttpResponse buildHttpResponse(URL resolvedUrl) {
		try {
			final URLConnection connection = resolvedUrl.openConnection();
			try (InputStream inputStream = connection.getInputStream()) {
				byte[] bytes = ByteStreams.toByteArray(inputStream);
				ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
				final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
				String fileName = resolvedUrl.getFile().substring(1);
				String contentType = fileTypeMap.getContentType(fileName);
				return buildResponse(buffer, contentType);
			}
		} catch (IOException e) {
			// TODO log exception
			ByteBuf buffer;
			try {
				buffer = Unpooled.wrappedBuffer("".getBytes("UTF-8"));
				return buildResponse(buffer, "text/html");
			} catch (UnsupportedEncodingException e1) {
				// TODO log exception
				return null;
			}
		}
	}

	private HttpResponse buildResponse(ByteBuf buffer, String contentType) {
		String contentFile = buffer.toString(Charset.defaultCharset());
		String responseAsString = String.format(contentFile, loggerLevel);
		byte[] bytes = responseAsString.getBytes();
		ByteBuf newBuffer = Unpooled.wrappedBuffer(bytes);
		HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, newBuffer);
		HttpHeaders.setContentLength(response, newBuffer.readableBytes());
		HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE, contentType);
		return response;
	}

}
