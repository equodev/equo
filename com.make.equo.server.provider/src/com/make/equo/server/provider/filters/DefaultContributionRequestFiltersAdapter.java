package com.make.equo.server.provider.filters;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.entity.ContentType;

import com.make.equo.server.contribution.resolvers.IEquoContributionUrlResolver;
import com.make.equo.server.offline.api.filters.IModifiableResponse;
import com.make.equo.server.offline.api.filters.OfflineRequestFiltersAdapter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class DefaultContributionRequestFiltersAdapter extends OfflineRequestFiltersAdapter implements IModifiableResponse {

	protected IEquoContributionUrlResolver urlResolver;
	
	private String customJsScripts;
	private String contributedFilePath;
	private List<String> equoContributionsJsApis;

	public DefaultContributionRequestFiltersAdapter(HttpRequest originalRequest, IEquoContributionUrlResolver urlResolver,
			List<String> equoContributionsJsApis, String customJsScripts, String contributedFilePath) {
		super(originalRequest);
		this.urlResolver = urlResolver;
		this.contributedFilePath = contributedFilePath;
		this.equoContributionsJsApis = equoContributionsJsApis;
		this.customJsScripts = customJsScripts;
	}

	@Override
	public HttpResponse clientToProxyRequest(HttpObject httpObject) {
		URL resolvedUrl = urlResolver.resolve(contributedFilePath);
		return super.buildHttpResponse(resolvedUrl);
	}
	
	@Override
	protected HttpResponse buildResponse(ByteBuf buffer, String defaultContentType) {
		String contentFile = buffer.toString(Charset.defaultCharset());
		byte[] data = contentFile.getBytes();

		ContentType contentType = ContentType.parse("text/html; charset=utf-8");
		Charset charset = contentType.getCharset();

		String responseToTransform = IModifiableResponse.createStringFromData(data, charset);

		String transformedResponse = addCustomJsScripts(responseToTransform);

		byte[] bytes = IModifiableResponse.createDataFromString(transformedResponse, charset);
		ByteBuf transformedContent = Unpooled.buffer(bytes.length);
		transformedContent.writeBytes(bytes);

		return super.buildResponse(transformedContent, "text/html; charset=utf-8");
	}

	private String addCustomJsScripts(String responseToTransform) {
		StringBuilder customResponseWithScripts = new StringBuilder(responseToTransform);
		customResponseWithScripts.append("\n");
		for (String jsApi : equoContributionsJsApis) {
			customResponseWithScripts.append(jsApi);
		}
		customResponseWithScripts.append(customJsScripts);
		return customResponseWithScripts.toString();
	}

	@Override
	public FullHttpResponse getOriginalFullHttpResponse() {
		return null;
	}

	@Override
	public boolean isModifiable() {
		return true;
	}

	@Override
	public String modifyOriginalResponse(String responseToTransform) {
		return null;
	}
}
