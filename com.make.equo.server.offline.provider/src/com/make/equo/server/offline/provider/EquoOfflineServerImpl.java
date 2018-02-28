package com.make.equo.server.offline.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
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

import org.apache.http.entity.ContentType;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.make.equo.server.offline.api.IEquoOfflineServer;
import com.make.equo.server.offline.api.IHttpRequestFilter;

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

	private static final String HTTPS_PROTOCOL = "https";
	private static final String HTTP_PROTOCOL = "http";
	private static final String FILE_NAMES_TO_CONTENT_TYPES_FILE_NAME = "fileNamesToContentTypes.properties";
	private static final String equoCachePathName = "cache_equo";
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
		System.out.println("Initializing Equo Offline Server...");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (lines != null) {
				lines.close();
			}
		}
		// try {
		// FileInputStream in = new FileInputStream(getCachePath());
		// ObjectInputStream objIn = new ObjectInputStream(in);
		// cacheOffline = (HashMap) objIn.readObject();
		// in.close();
		// objIn.close();
		// } catch (IOException | ClassNotFoundException e) {
		// System.out.println("Initializing cache offline...");
		// e.printStackTrace();
		// cacheOffline = new HashMap<>();
		// }
	}

	private Properties loadPropertyFile(String propertyFilePath) throws IOException {
		Properties result = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(propertyFilePath);
			result.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	@Override
	public void saveRequestResponse(HttpRequest originalRequest, HttpObject httpObject) {
		if (httpObject instanceof FullHttpResponse) {
			FullHttpResponse fullResponse = (FullHttpResponse) httpObject;
			int code = fullResponse.getStatus().code();
			// if (code >= 200 && code < 300) {
			// String host = originalRequest.headers().get(Names.HOST);
			// String referer = originalRequest.headers().get(Names.REFERER);
			HttpResponse duplicatedResponse = (HttpResponse) fullResponse.duplicate().retain();
			// String uri = originalRequest.getUri();
			// if (code == 202) {
			// System.out.println("code is " + code + " with response " +
			// duplicatedResponse);
			// }
			// System.out.println("save uri is " + uri);
			// System.out.println("request is " + originalRequest);
			// System.out.println("request is " + originalRequest.toString());
			// String suffix = host + uri;
			FullHttpRequest fullHttpRequest = (FullHttpRequest) originalRequest;
			String requestUniqueId = getRequestUniqueId(fullHttpRequest);
			saveStartPageIfPossible(fullHttpRequest, duplicatedResponse, requestUniqueId);
			// if (referer != null) {
			// URI refererUri = URI.create(referer);
			// String refererUriPath = refererUri.getPath();
			// // System.out.println("referer path is " + refererUriPath);
			// lastVisitedPage = refererUriPath;
			// }
			cacheOffline.put(requestUniqueId, duplicatedResponse);
		}
		// }
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
//			if (httpRequest.getUri().contains("pathEvaluator")) {
//				System.out.println("request payload is " + requestPayload + " and uri is " + fullHttpRequest.getUri());
//			}
			return requestId + "_" + requestPayload;
		}
	}

	private FullHttpRequest applyHttpRequestFilters(FullHttpRequest httpRequest) {
		for (IHttpRequestFilter httpRequestFilter : httpRequestFilters) {
			httpRequest = (FullHttpRequest) httpRequestFilter.applyFilter(httpRequest);
		}
		return httpRequest;
	}

	private void saveStartPageIfPossible(FullHttpRequest originalRequest, HttpResponse httpResponse, String requestUniqueId) {
		Optional<String> proxiedUrl = getProxiedUrl(originalRequest);
		if (originalRequest.getUri().contains("/browse")) {
			System.out.println("es browse...");
		}
		if (proxiedUrl.isPresent() && isApage(httpResponse)) {
			startPageRequest = requestUniqueId;
		}
	}

	private boolean isApage(HttpResponse httpResponse) {
		String contentTypeHeader = httpResponse.headers().get(Names.CONTENT_TYPE);
		return (contentTypeHeader != null && (contentTypeHeader.startsWith("text/html")
				|| contentTypeHeader.startsWith("application/xhtml+xml")));
	}

	// TODO only save these contents if there is internet connection, if not, the
	// things are already saved. No need to save them.
	@Deactivate
	public void stop() {
		System.out.println("Stopping Equo Offline Server... ");
		// try {
		saveStartPageToFile();
		for (String originalRequestUniqueId : cacheOffline.keySet()) {
			// check if response is an offlineresponse
			FullHttpResponse httpResponse = (FullHttpResponse) cacheOffline.get(originalRequestUniqueId);
			String contentTypeHeader = httpResponse.headers().get(Names.CONTENT_TYPE);
			int code = httpResponse.getStatus().code();
			httpResponse = parseResponseIfNecessary(httpResponse, contentTypeHeader);

			ByteBuf content = httpResponse.content();
			byte[] data = new byte[content.readableBytes()];
			content.readBytes(data);

			// File newResponsePath = new File(getCachePath() + File.separator +
			// requestPath);
			// if (!newResponsePath.exists()) {
			try {
//				String originalRequestUniqueId = getRequestUniqueId(originalRequestUniqueId);
				// File parentFile = newResponsePath.getParentFile();
				// if (!parentFile.exists()) {
				// parentFile.mkdirs();
				// }
				// String fileName = newResponsePath.getName();
				String fileNameHash = getFileNameHash(originalRequestUniqueId);
				if (originalRequestUniqueId.contains("preflight")) {
					System.out.println("when saving preflight!");
					System.out.println("request is is " + originalRequestUniqueId);
					System.out.println("preflight output file is " + fileNameHash);
				}
				if (originalRequestUniqueId.contains("switch")) {
					System.out.println("when saving switch!");
					System.out.println("when saving request is " + originalRequestUniqueId);
					System.out.println("when saving switch output file is " + fileNameHash);
				}
				File outputFile = new File(getCachePath() + File.separator + fileNameHash);
				// file.createNewFile();
				FileOutputStream fos = new FileOutputStream(outputFile);
				fos.write(data);
				fos.close();
				if (contentTypeHeader != null) {
					fileNamesToContentTypes.put(fileNameHash, contentTypeHeader);
				}
				fileNamesToStatusCodes.put(fileNameHash, Integer.toString(code));
			} catch (IOException | NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
			// }
			// httpResponse.retain();
		}
		savePropertyFile(fileNamesToContentTypes, "File Names to Content Types", getFileNamesToContentTypesFilePath());
		savePropertyFile(fileNamesToStatusCodes, "File Names to Status Codes", getFileNamesToStatusCodesFilePath());
		cacheOffline.clear();
		// FileOutputStream fout = new FileOutputStream("/Users/seba/cache_off.seb");
		// ObjectOutputStream oos = new ObjectOutputStream(fout);
		// oos.writeObject(cacheOffline);
		// oos.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	// TODO parse of response and the saving of it could be done in another thread.
	private FullHttpResponse parseResponseIfNecessary(FullHttpResponse fullResponse, String contentTypeHeader) {
		if (contentTypeHeader != null
				&& (contentTypeHeader.startsWith("text/") || contentTypeHeader.startsWith("application/xhtml+xml")
						|| contentTypeHeader.startsWith("application/json")
						|| contentTypeHeader.startsWith("application/javascript"))) {
			ContentType contentType = ContentType.parse(contentTypeHeader);
			Charset charset = contentType.getCharset();

			ByteBuf content = fullResponse.content();

			byte[] data = new byte[content.readableBytes()];
			content.readBytes(data);

			String responseToTransform = createStringFromData(data, charset);
			responseToTransform = responseToTransform.replaceAll(HTTPS_PROTOCOL, HTTP_PROTOCOL);

			byte[] bytes = createDataFromString(responseToTransform, charset);
			ByteBuf transformedContent = Unpooled.buffer(bytes.length);
			transformedContent.writeBytes(bytes);

			DefaultFullHttpResponse transformedResponse = new DefaultFullHttpResponse(fullResponse.getProtocolVersion(),
					fullResponse.getStatus(), transformedContent);
			transformedResponse.headers().set(fullResponse.headers());
			HttpHeaders.setContentLength(transformedResponse, bytes.length);

			return transformedResponse;
		}
		return fullResponse;
	}

	private void savePropertyFile(Properties propertyFile, String title, String propertyFilePath) {
		OutputStream output = null;
		try {
			output = new FileOutputStream(propertyFilePath);
			propertyFile.store(output, title);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
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
				e1.printStackTrace();
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
//		System.out.println("viene por aca");
		String requestUniqueId = null;
		if (startPageRequest != null) {
			requestUniqueId = startPageRequest;
//			System.out.println("no es null lastvisited es " + startPageRequest);
			startPageRequest = null;
		} else {
			// System.out.println("es null lastvisited");
			requestUniqueId = getRequestUniqueId((FullHttpRequest) originalRequest);
		}
		// if (cacheOffline.containsKey(uri)) {
		// return cacheOffline.get(uri);
		// }
		// int lastDirectoryIndex = uri.lastIndexOf("/");
		// String outputFileName = uri.substring(lastDirectoryIndex);
		// System.out.println("outputFileName is " + outputFileName);
		// String directoryParentPath = uri.substring(0, lastDirectoryIndex);
		// System.out.println("directoryParentPath is " + directoryParentPath);
		try {
			// System.out.println("original request is " + originalRequest);
			String fileNameHash = getFileNameHash(requestUniqueId);
			// System.out.println("request is is " + request);
			String outputFilePath = getCachePath() + File.separator + fileNameHash;
			if (requestUniqueId.contains("preflight")) {
				System.out.println("when loading request is " + requestUniqueId);
				System.out.println("preflight output file is " + outputFilePath);
				ByteBuf content = ((FullHttpRequest) originalRequest).content();
				byte[] data = new byte[content.readableBytes()];
				content.readBytes(data);
				String payload = new String(data);
				System.out.println("when loading the payload is " + payload);
			}
			if (requestUniqueId.contains("switch")) {
				System.out.println("when loading request is " + requestUniqueId);
				System.out.println("switch output file is " + outputFilePath);
				ByteBuf content = ((FullHttpRequest) originalRequest).content();
				byte[] data = new byte[content.readableBytes()];
				content.readBytes(data);
				String payload = new String(data);
				System.out.println("when loading the payload is " + payload);
			}
			// System.out.println("outputFilePath is " + outputFilePath);
			FileInputStream inputStream = new FileInputStream(outputFilePath);
			byte[] bytes = ByteStreams.toByteArray(inputStream);
			ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
			// final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
			String contentType = fileNamesToContentTypes.getProperty(fileNameHash);
			String statusCode = fileNamesToStatusCodes.getProperty(fileNameHash);
//			System.out.println("Content type is " + contentType);
//			System.out.println("Status code is " + statusCode);
			inputStream.close();
			HttpResponse buildResponse = buildResponse(buffer, contentType, Integer.valueOf(statusCode));
			cacheOffline.put(requestUniqueId, buildResponse);
			return buildResponse;
			// cacheOffline = (HashMap) objIn.readObject();
			// in.close();
			// objIn.close();
			// return cacheOffline.get(uri);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			 e.printStackTrace();
		}
		return null;
	}

	private String parseUriIfNecessary(HttpRequest originalRequest) {
		String uri = originalRequest.getUri();
		if (uri.startsWith(HTTP_PROTOCOL)) {
			String host = originalRequest.headers().get(Names.HOST);
			return uri.replace(HTTP_PROTOCOL + "://" + host, "");
		}
		return uri;
	}

	protected HttpResponse buildResponse(ByteBuf buffer, String contentType, Integer statusCode) {
		HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
				new HttpResponseStatus(statusCode, ""), buffer);
		HttpHeaders.setContentLength(response, buffer.readableBytes());
		if (!Strings.isNullOrEmpty(contentType)) {
			HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE, contentType);
		}
		return response;
	}

	// protected HttpResponse buildResponse(ByteBuf buffer1, String contentType) {
	// ByteBuf buffer = Unpooled.wrappedBuffer("Offline response".getBytes());
	// HttpResponse response = new DefaultFullHttpResponse(
	// HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
	// HttpHeaders.setContentLength(response, buffer.readableBytes());
	// HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE,
	// "text/html");
	// return response;
	// }

	private String getCachePath() {
		File equoCacheDir = new File(System.getProperty("user.home"), equoCachePathName);
		if (!equoCacheDir.exists()) {
			equoCacheDir.mkdirs();
		}
		return equoCacheDir.getAbsolutePath();
	}

	// TODO this method is repeated in EquoHttpModifierFilte...refactor it
	private String createStringFromData(byte[] data, Charset charset) {
		return (charset == null) ? new String(data) : new String(data, charset);
	}

	// TODO this method is repeated in EquoHttpModifierFilte...refactor it
	private byte[] createDataFromString(String string, Charset charset) {
		return (charset == null) ? string.getBytes() : string.getBytes(charset);
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

}
