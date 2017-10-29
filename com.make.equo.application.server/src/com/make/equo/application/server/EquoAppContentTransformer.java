package com.make.equo.application.server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.proxy.AfterContentTransformer;

public class EquoAppContentTransformer extends AfterContentTransformer {

	private String jsScripts;
	private HttpServletRequest clientRequest;

	public EquoAppContentTransformer(HttpServletRequest clientRequest, String jsScripts) {
		this.clientRequest = clientRequest;
		this.jsScripts = jsScripts;
	}

	@Override
	public boolean transform(Source source, Sink sink) throws IOException {
		org.eclipse.jetty.util.IO.copy(source.getInputStream(), sink.getOutputStream());
		if (!jsScripts.isEmpty()) {
			String scriptPath = jsScripts.replaceAll(EquoHttpProxy.PORT, Integer.toString(clientRequest.getLocalPort()));
			InputStream stream = new ByteArrayInputStream(scriptPath.getBytes(StandardCharsets.UTF_8.name()));
			org.eclipse.jetty.util.IO.copy(stream, sink.getOutputStream());
		}
		return true;
	}

}
