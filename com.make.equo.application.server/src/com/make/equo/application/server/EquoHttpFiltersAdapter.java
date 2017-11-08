package com.make.equo.application.server;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.entity.ContentType;
import org.littleshoot.proxy.HttpFiltersAdapter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class EquoHttpFiltersAdapter extends HttpFiltersAdapter {

	private static final String URL_PATH = "urlPath";
	private static final String PATH_TO_STRING_REG = "PATHTOSTRING";
	private List<String> jsScripts;
	private static final String URL_SCRIPT_SENTENCE = "<script src=\"urlPath\"></script>";
	private static final String LOCAL_SCRIPT_SENTENCE = "<script src=\"PATHTOSTRING\" defer></script>";
	private String appUrl;

	public EquoHttpFiltersAdapter(String appUrl, HttpRequest originalRequest, List<String> jsScripts) {
		super(originalRequest);
		this.appUrl = appUrl;
		this.jsScripts = jsScripts;
	}

	@Override
	public HttpResponse clientToProxyRequest(HttpObject httpObject) {
		if (httpObject instanceof FullHttpRequest) {
			FullHttpRequest httpRequest = (FullHttpRequest) httpObject;
			// httpRequest.headers().remove("Host");
			// httpRequest.headers().add("Host", "www.netflix.com");
			// httpRequest.setUri(url);
			if (!httpRequest.getUri().contains("netflix")) {
				// ByteBuf buffer;
				// try {
				//// buffer = Unpooled.wrappedBuffer("Hola mundo".getBytes("UTF-8"));
				//// HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
				// HttpResponseStatus.OK, buffer);
				//// HttpHeaders.setContentLength(response, buffer.readableBytes());
				//// HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE,
				// "text/html");
				// return null;
				// } catch (UnsupportedEncodingException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				return null;
			}
		}
		return null;
	}

	@Override
	public HttpResponse proxyToServerRequest(HttpObject httpObject) {
		return null;
	}

	@Override
	public HttpObject serverToProxyResponse(HttpObject httpObject) {
		if (httpObject instanceof FullHttpResponse && appUrl.contains(originalRequest.headers().get(Names.HOST))
				&& ((FullHttpResponse) httpObject).getStatus().code() == HttpResponseStatus.OK.code()) {
			FullHttpResponse fullResponse = (FullHttpResponse) httpObject;
			String contentTypeHeader = fullResponse.headers().get("Content-Type");
			if (contentTypeHeader != null && contentTypeHeader.startsWith("text/")) {
				ContentType contentType = ContentType.parse(contentTypeHeader);
				Charset charset = contentType.getCharset();

				ByteBuf content = fullResponse.content();

				byte[] data = new byte[content.readableBytes()];
				content.readBytes(data);

				String responseToTransform = createStringFromData(data, charset);
				StringBuilder customResponseWithScripts = new StringBuilder(responseToTransform);
				customResponseWithScripts.append(convertJsScriptsToString());

				byte[] bytes = createDataFromString(customResponseWithScripts.toString(), charset);
				ByteBuf transformedContent = Unpooled.buffer(bytes.length);
				transformedContent.writeBytes(bytes);

				DefaultFullHttpResponse transformedResponse = new DefaultFullHttpResponse(
						fullResponse.getProtocolVersion(), fullResponse.getStatus(), transformedContent);
				transformedResponse.headers().set(fullResponse.headers());
				HttpHeaders.setContentLength(transformedResponse, bytes.length);

				return transformedResponse;
			}
		}
		return httpObject;
	}

	@Override
	public HttpObject proxyToClientResponse(HttpObject httpObject) {
		// TODO: implement your filtering here ????
		return httpObject;
	}

	private String convertJsScriptsToString() {
		if (jsScripts.isEmpty()) {
			return "";
		}
		String newLineSeparetedScripts = jsScripts.stream().map(string -> generateScriptSentence(string))
				.collect(Collectors.joining("\n"));
		return newLineSeparetedScripts;
	}

	private String generateScriptSentence(String scriptPath) {
		try {
			if (isLocalScript(scriptPath)) {
				// URL url = new URL(scriptPath);
				// File file = new File(FileLocator.resolve(url).toURI());
				// String absolutePath = file.getAbsolutePath();

				String scriptSentence = LOCAL_SCRIPT_SENTENCE.replaceAll(PATH_TO_STRING_REG, appUrl + "/" + scriptPath);
				return scriptSentence;
			} else {
				URL url = new URL(scriptPath);
				String scriptSentence = URL_SCRIPT_SENTENCE.replaceAll(URL_PATH, url.toString());
				return scriptSentence;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return "";
	}

	private boolean isLocalScript(String scriptPath) {
		String scriptPathLoweredCase = scriptPath.trim().toLowerCase();
		return scriptPathLoweredCase.startsWith(EquoHttpProxyServer.LOCAL_FILE_PROTOCOL);
	}

	private String createStringFromData(byte[] data, Charset charset) {
		return (charset == null) ? new String(data) : new String(data, charset);
	}

	private byte[] createDataFromString(String string, Charset charset) {
		return (charset == null) ? string.getBytes() : string.getBytes(charset);
	}

}
