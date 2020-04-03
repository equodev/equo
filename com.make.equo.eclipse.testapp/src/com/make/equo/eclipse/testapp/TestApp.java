package com.make.equo.eclipse.testapp;

import java.util.Collections;

import org.osgi.service.component.annotations.Component;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.monaco.EquoMonacoEditor;

@Component
public class TestApp implements IEquoApplication {

	@Override
	public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder) {
		EquoMonacoEditor.addLspServer("ws://127.0.0.1:3000/sampleServer", Collections.singleton("json"));
		return null;
	}

}
