package com.make.equo.server.offline.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import javax.xml.bind.DatatypeConverter;

import org.littleshoot.proxy.HttpFiltersAdapter;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.make.equo.server.offline.api.IEquoOfflineServer;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;
import com.make.equo.server.offline.api.filters.IModifiableResponse;
import com.make.equo.server.offline.provider.utils.IConstants;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

@Component
public class EquoOfflineServerImpl implements IEquoOfflineServer {

	private static final String FILE_NAMES_TO_CONTENT_TYPES_FILE_NAME = "fileNamesToContentTypes.properties";
	private static final String equoCachePathName = ".cache_equo";
	private static final String startPageFileName = "lastVisitedPage";
	private static final String FILE_NAMES_TO_STATUS_CODES_FILE_NAME = "fileNamesToStatusCodes.properties";
	private Map<String, HttpResponse> cacheOffline;
	private Properties fileNamesToContentTypes;
	private Properties fileNamesToStatusCodes;
	private String startPageRequest;
	private List<String> proxiedUrls;
	private List<IHttpRequestFilter> httpRequestFilters;

	@Activate
	public void start() {
		cacheOffline = new HashMap<>();
		proxiedUrls = new ArrayList<>();
		httpRequestFilters = new ArrayList<>();
		fileNamesToContentTypes = new Properties();
		fileNamesToStatusCodes = new Properties();
		Stream<String> lines = null;
		try {
			startPageRequest = new String(Files.readAllBytes(Paths.get(getStartPagePath())));
			fileNamesToContentTypes = loadPropertyFile(getFileNamesToContentTypesFilePath());
			fileNamesToStatusCodes = loadPropertyFile(getFileNamesToStatusCodesFilePath());
		} catch (IOException e) {
			// Nothing to log
		} finally {
			if (lines != null) {
				lines.close();
			}
		}
	}

	private Properties loadPropertyFile(String propertyFilePath) throws IOException {
		Properties result = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(propertyFilePath);
			result.load(input);
		} catch (IOException ex) {
			// TODO log the exception
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					// TODO log the exception
				}
			}
		}
		return result;
	}

	@Override
	public void saveRequestResponse(HttpRequest originalRequest, HttpObject httpObject) {
		if (httpObject instanceof FullHttpResponse) {
			FullHttpResponse fullResponse = (FullHttpResponse) httpObject;
			HttpResponse duplicatedResponse = (HttpResponse) fullResponse.duplicate().retain();
			FullHttpRequest fullHttpRequest = (FullHttpRequest) originalRequest;
			String requestUniqueId = getRequestUniqueId(fullHttpRequest);
			saveStartPageIfPossible(fullHttpRequest, duplicatedResponse, requestUniqueId);
			cacheOffline.put(requestUniqueId, duplicatedResponse);
		}
	}

	private String getRequestUniqueId(FullHttpRequest fullHttpRequest) {
		FullHttpRequest httpRequest = applyHttpRequestFilters(fullHttpRequest);
		String requestId = parseUriIfNecessary(fullHttpRequest);
		if (fullHttpRequest.getMethod() == HttpMethod.GET) {
			return requestId;
		} else {
			httpRequest = (FullHttpRequest) httpRequest.duplicate().retain();
			ByteBuf content = httpRequest.content();
			byte[] data = new byte[content.readableBytes()];
			content.readBytes(data);
			String requestPayload = new String(data);
			httpRequest.release();
			return requestId + "_" + requestPayload;
		}
	}

	private FullHttpRequest applyHttpRequestFilters(FullHttpRequest httpRequest) {
		for (IHttpRequestFilter httpRequestFilter : httpRequestFilters) {
			httpRequest = (FullHttpRequest) httpRequestFilter.applyFilter(httpRequest);
		}
		return httpRequest;
	}

	private void saveStartPageIfPossible(FullHttpRequest originalRequest, HttpResponse httpResponse,
			String requestUniqueId) {
		Optional<String> proxiedUrl = getProxiedUrl(originalRequest);
		if (proxiedUrl.isPresent() && isApage(httpResponse)) {
			startPageRequest = requestUniqueId;
		}
	}

	private boolean isApage(HttpResponse httpResponse) {
		String contentTypeHeader = httpResponse.headers().get(Names.CONTENT_TYPE);
		return (contentTypeHeader != null && (contentTypeHeader.startsWith("text/html")
				|| contentTypeHeader.startsWith("application/xhtml+xml")));
	}

	@Deactivate
	public void stop() {
		saveStartPageToFile();
		for (String originalRequestUniqueId : cacheOffline.keySet()) {
			FullHttpResponse httpResponse = (FullHttpResponse) cacheOffline.get(originalRequestUniqueId);
			String contentTypeHeader = httpResponse.headers().get(Names.CONTENT_TYPE);
			int code = httpResponse.getStatus().code();
			httpResponse = parseResponseIfNecessary(httpResponse, contentTypeHeader);

			ByteBuf content = httpResponse.content();
			byte[] data = new byte[content.readableBytes()];
			content.readBytes(data);

			try {
				String fileNameHash = getFileNameHash(originalRequestUniqueId);
				File outputFile = new File(getCachePath() + File.separator + fileNameHash);
				FileOutputStream fos = new FileOutputStream(outputFile);
				fos.write(data);
				fos.close();
				if (contentTypeHeader != null) {
					fileNamesToContentTypes.put(fileNameHash, contentTypeHeader);
				}
				fileNamesToStatusCodes.put(fileNameHash, Integer.toString(code));
			} catch (IOException | NoSuchAlgorithmException e) {
				// TODO Log the exception, or maybe better log the file that wasn't found
			}
		}
		savePropertyFile(fileNamesToContentTypes, "File Names to Content Types", getFileNamesToContentTypesFilePath());
		savePropertyFile(fileNamesToStatusCodes, "File Names to Status Codes", getFileNamesToStatusCodesFilePath());
		cacheOffline.clear();
	}

	private FullHttpResponse parseResponseIfNecessary(FullHttpResponse fullResponse, String contentTypeHeader) {
		IModifiableResponse fromHttpsToHttpResponse = new FromHttpsToHttpResponse(fullResponse);
		if (fromHttpsToHttpResponse.isModifiable()) {
			return fromHttpsToHttpResponse.generateModifiedResponse();
		}
		return fullResponse;
	}

	private void savePropertyFile(Properties propertyFile, String title, String propertyFilePath) {
		OutputStream output = null;
		try {
			output = new FileOutputStream(propertyFilePath);
			propertyFile.store(output, title);
		} catch (IOException io) {
			// TODO log the exception
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					// TODO log the exception
				}
			}

		}
	}

	private String getFileNamesToContentTypesFilePath() {
		return getCachePath() + File.separator + FILE_NAMES_TO_CONTENT_TYPES_FILE_NAME;
	}

	private String getFileNamesToStatusCodesFilePath() {
		return getCachePath() + File.separator + FILE_NAMES_TO_STATUS_CODES_FILE_NAME;
	}

	private void saveStartPageToFile() {
		if (startPageRequest != null) {
			String startPagePath = getStartPagePath();
			File startPageFile = new File(startPagePath);
			if (startPageFile.exists()) {
				startPageFile.delete();
			}
			try (PrintWriter out = new PrintWriter(startPagePath)) {
				out.print(startPageRequest);
			} catch (FileNotFoundException e1) {
				// TODO log the exception
			}
		}
	}

	private String getStartPagePath() {
		return getCachePath() + File.separator + startPageFileName;
	}

	private String getFileNameHash(String fileName) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(fileName.getBytes());
		byte[] digest = messageDigest.digest();
		String fileNameHash = DatatypeConverter.printHexBinary(digest);
		return fileNameHash;
	}

	@Override
	public HttpResponse getOfflineResponse(HttpRequest originalRequest) throws IOException {
		String requestUniqueId = null;
		if (startPageRequest != null) {
			requestUniqueId = startPageRequest;
			startPageRequest = null;
		} else {
			requestUniqueId = getRequestUniqueId((FullHttpRequest) originalRequest);
		}
		try {
			String fileNameHash = getFileNameHash(requestUniqueId);
			String outputFilePath = getCachePath() + File.separator + fileNameHash;
			FileInputStream inputStream = new FileInputStream(outputFilePath);
			byte[] bytes = ByteStreams.toByteArray(inputStream);
			ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
			String contentType = fileNamesToContentTypes.getProperty(fileNameHash);
			String statusCode = fileNamesToStatusCodes.getProperty(fileNameHash);
			inputStream.close();
			HttpResponse buildResponse = buildResponse(buffer, contentType, Integer.valueOf(statusCode));
			cacheOffline.put(requestUniqueId, buildResponse);
			return buildResponse;
		} catch (NoSuchAlgorithmException e) {

		}
		return null;
	}

	private String parseUriIfNecessary(HttpRequest originalRequest) {
		String uri = originalRequest.getUri();
		if (uri.startsWith(IConstants.HTTP_PROTOCOL)) {
			String host = originalRequest.headers().get(Names.HOST);
			return uri.replace(IConstants.HTTP_PROTOCOL + "://" + host, "");
		}
		return uri;
	}

	private HttpResponse buildResponse(ByteBuf buffer, String contentType, Integer statusCode) {
		HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
				new HttpResponseStatus(statusCode, ""), buffer);
		HttpHeaders.setContentLength(response, buffer.readableBytes());
		if (!Strings.isNullOrEmpty(contentType)) {
			HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE, contentType);
		}
		return response;
	}

	private String getCachePath() {
		File equoCacheDir = new File(System.getProperty("user.home"), equoCachePathName);
		if (!equoCacheDir.exists()) {
			equoCacheDir.mkdirs();
		}
		return equoCacheDir.getAbsolutePath();
	}

	@Override
	public void setProxiedUrls(List<String> urls) {
		proxiedUrls.addAll(urls);
	}

	// TODO refactor it, it's repeated in EquoHttpProxyServer
	private Optional<String> getProxiedUrl(FullHttpRequest originalRequest) {
		return proxiedUrls.stream().filter(url -> containsHeader(url, originalRequest)).findFirst();
	}

	// TODO refactor it, it's repeated in EquoHttpProxyServer
	private boolean containsHeader(String url, FullHttpRequest originalRequest) {
		String host = originalRequest.headers().get(Names.HOST);
		if (host.indexOf(":") != -1) {
			return url.contains(host.substring(0, host.indexOf(":")));
		} else {
			return url.contains(host);
		}
	}

	@Override
	public void addHttpRequestFilter(IHttpRequestFilter httpRequestFilter) {
		httpRequestFilters.add(httpRequestFilter);
	}

	@Override
	public HttpFiltersAdapter getOfflineHttpFiltersAdapter(HttpRequest originalRequest) {
		return new OfflineEquoHttpFiltersAdapter(originalRequest, this);
	}

}
