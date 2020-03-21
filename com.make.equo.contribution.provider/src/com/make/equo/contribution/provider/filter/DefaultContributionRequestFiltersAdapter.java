package com.make.equo.contribution.provider.filter;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.entity.ContentType;

import com.make.equo.contribution.api.ResolvedContribution;
import com.make.equo.contribution.api.resolvers.IEquoContributionUrlResolver;
import com.make.equo.server.offline.api.filters.IModifiableResponse;
import com.make.equo.server.offline.api.filters.OfflineRequestFiltersAdapter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class DefaultContributionRequestFiltersAdapter extends OfflineRequestFiltersAdapter
		implements IModifiableResponse {

	protected IEquoContributionUrlResolver urlResolver;

	private String customJsScripts;
	private String customStyles;
	private List<String> equoContributionsJsApis;
	private List<String> equoContributionStyles;
	private String contributedFilePath;

	public DefaultContributionRequestFiltersAdapter(HttpRequest originalRequest, ResolvedContribution contribution,
			IEquoContributionUrlResolver urlResolver, String contributedFilePath) {
		super(originalRequest);
		this.urlResolver = urlResolver;
		equoContributionsJsApis = contribution.getScripts();
		equoContributionStyles = contribution.getStyles();
		customJsScripts = contribution.getCustomScripts(originalRequest.getUri());
		customStyles = contribution.getCustomStyles(originalRequest.getUri());
		this.contributedFilePath = contributedFilePath;
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

		String transformedResponse = addCustomJsScriptsAndStyles(responseToTransform);

		byte[] bytes = IModifiableResponse.createDataFromString(transformedResponse, charset);
		ByteBuf transformedContent = Unpooled.buffer(bytes.length);
		transformedContent.writeBytes(bytes);

		return super.buildResponse(transformedContent, "text/html; charset=utf-8");
	}

	private String addCustomJsScriptsAndStyles(String responseToTransform) {
		StringBuilder customResponse = new StringBuilder(responseToTransform);
		customResponse.append("\n");
		for (String jsApi : equoContributionsJsApis) {
			customResponse.append(jsApi);
		}
		for (String style : equoContributionStyles) {
			customResponse.append(style);
		}
		customResponse.append(customJsScripts);
		customResponse.append(customStyles);
		return customResponse.toString();
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
