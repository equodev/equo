package com.make.equo.poc;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;

import org.osgi.service.component.annotations.Component;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.monaco.EquoMonacoEditor;

@Component
public class EquoApp implements IEquoApplication {

	@Override
	public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder) {
		try {
			EquoMonacoEditor.addLspServer(Arrays.asList("node", "/home/lean/.nvm/versions/node/v13.7.0/bin/html-languageserver", "--stdio"),
					Collections.singleton("html2"));
			return appBuilder.plainApp("index.html")
					.start();

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



}
