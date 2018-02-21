package com.make.equo.server.offline.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.entity.ContentType;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.google.common.io.ByteStreams;
import com.make.equo.server.offline.api.IEquoOfflineServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
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
	private static final String lastVisitedPageFileName = "lastVisitedPage";
	private static final String FILE_NAMES_TO_STATUS_CODES_FILE_NAME = "fileNamesToStatusCodes.properties";
	private Map<String, HttpResponse> cacheOffline;
	private Properties fileNamesToContentTypes;
	private Properties fileNamesToStatusCodes;
	private String lastVisitedPage;

	@Activate
	public void start() {
		System.out.println("Initializing Equo Offline Server...");
		cacheOffline = new HashMap<>();
		fileNamesToContentTypes = new Properties();
		fileNamesToStatusCodes = new Properties();
		Stream<String> lines = null;
		try {
			lines = Files.lines(Paths.get(getLastVisitedPagePath()), StandardCharsets.UTF_8);
			lines.forEach(uri -> {
				lastVisitedPage = uri;
			});
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
	public void save(HttpRequest originalRequest, HttpObject httpObject) {
		if (httpObject instanceof FullHttpResponse) {
			FullHttpResponse fullResponse = (FullHttpResponse) httpObject;
			int code = fullResponse.getStatus().code();
			if (code >= 200 && code < 300) {
				// String host = originalRequest.headers().get(Names.HOST);
				String referer = originalRequest.headers().get(Names.REFERER);
				HttpResponse duplicatedResponse = (HttpResponse) fullResponse.duplicate().retain();
				String uri = originalRequest.getUri();
				if (code == 202) {
					System.out.println("code is " + code + " with response " + duplicatedResponse);
				}
				System.out.println("save uri is " + uri);
				System.out.println("request is " + originalRequest);
				System.out.println("request is " + originalRequest.toString());
				// String suffix = host + uri;
				if (referer != null) {
					URI refererUri = URI.create(referer);
					String refererUriPath = refererUri.getPath();
					// System.out.println("referer path is " + refererUriPath);
					lastVisitedPage = refererUriPath;
				}
				cacheOffline.put(uri, duplicatedResponse);
			}
		}
	}

	@Deactivate
	public void stop() {
		System.out.println("Stopping Equo Offline Server... ");
		// try {
		saveLastVisitedPage();
		for (String requestPath : cacheOffline.keySet()) {
			// check if response is an offlineresponse
			FullHttpResponse httpResponse = (FullHttpResponse) cacheOffline.get(requestPath);
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
				// File parentFile = newResponsePath.getParentFile();
				// if (!parentFile.exists()) {
				// parentFile.mkdirs();
				// }
				// String fileName = newResponsePath.getName();
				String fileNameHash = getFileNameHash(requestPath);
				File outputFile = new File(getCachePath() + File.separator + fileNameHash);
				// file.createNewFile();
				FileOutputStream fos = new FileOutputStream(outputFile);
				fos.write(data);
				fos.close();
				fileNamesToContentTypes.put(fileNameHash, contentTypeHeader);
				fileNamesToStatusCodes.put(fileNameHash, Integer.toString(code));
			} catch (IOException | NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				&& (contentTypeHeader.startsWith("text/") || contentTypeHeader.startsWith("application/json")
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

	private void saveLastVisitedPage() {
		if (lastVisitedPage != null) {
			String lastVisitedPagePath = getLastVisitedPagePath();
			File lastVisitedPageFile = new File(lastVisitedPagePath);
			if (lastVisitedPageFile.exists()) {
				lastVisitedPageFile.delete();
			}
			try (PrintWriter out = new PrintWriter(lastVisitedPagePath)) {
				out.println(lastVisitedPage);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}

	private String getLastVisitedPagePath() {
		return getCachePath() + File.separator + lastVisitedPageFileName;
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
		System.out.println("viene por aca");
		String uri = null;
		if (lastVisitedPage != null) {
			uri = lastVisitedPage;
			System.out.println("no es null lastvisited es " + lastVisitedPage);
			lastVisitedPage = null;
		} else {
			System.out.println("es null lastvisited");
			uri = originalRequest.getUri();
		}
		uri = parseUriIfNecessary(originalRequest, uri);
		// if (cacheOffline.containsKey(uri)) {
		// return cacheOffline.get(uri);
		// }
		// int lastDirectoryIndex = uri.lastIndexOf("/");
		// String outputFileName = uri.substring(lastDirectoryIndex);
		// System.out.println("outputFileName is " + outputFileName);
		// String directoryParentPath = uri.substring(0, lastDirectoryIndex);
		// System.out.println("directoryParentPath is " + directoryParentPath);
		try {
			System.out.println("original request is " + originalRequest);
			String fileNameHash = getFileNameHash(uri);
			System.out.println("uri is " + uri);
			String outputFilePath = getCachePath() + File.separator + fileNameHash;
			System.out.println("outputFilePath is " + outputFilePath);
			FileInputStream inputStream = new FileInputStream(outputFilePath);
			byte[] bytes = ByteStreams.toByteArray(inputStream);
			ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
			// final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
			String contentType = fileNamesToContentTypes.getProperty(fileNameHash);
			String statusCode = fileNamesToStatusCodes.getProperty(fileNameHash);
			if (uri.equals("/browse")) {
				System.out.println("file hash is " + outputFilePath);
			}
			System.out.println("Content type is " + contentType);
			System.out.println("Status code is " + statusCode);
			inputStream.close();
			HttpResponse buildResponse = buildResponse(buffer, contentType, Integer.valueOf(statusCode));
			cacheOffline.put(uri, buildResponse);
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

	private String parseUriIfNecessary(HttpRequest originalRequest, String uri) {
		if (uri.startsWith(HTTP_PROTOCOL)) {
			String host = originalRequest.headers().get(Names.HOST);
			return uri.replace(HTTP_PROTOCOL + "://" + host, "");
		}
		return uri;
	}

	protected HttpResponse buildResponse(ByteBuf buffer, String contentType, Integer statusCode) {
		HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, new HttpResponseStatus(statusCode, ""), buffer);
		HttpHeaders.setContentLength(response, buffer.readableBytes());
		HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE, contentType);
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
}
