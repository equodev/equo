package com.make.equo.eclipse.testapp;

import java.util.Arrays;
import java.util.Collections;

import org.osgi.service.component.annotations.Component;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;

@Component
public class TestApp implements IEquoApplication {

	@Override
	public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder) {
		// EquoMonacoEditor.addLspWsServer("ws://127.0.0.1:3000/sampleServer",
		// Collections.singleton("json"));
		EquoMonacoEditor.addLspServer(Arrays.asList("node",
				"'/home/leandro/Descargas/jsonrpc-ws-proxy/yaml-language-server/out/server/src/server.js'", "--stdio"),
				Collections.singleton("json"));
		return null;
	}

}
